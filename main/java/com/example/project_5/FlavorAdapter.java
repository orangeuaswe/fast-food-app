package com.example.project_5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project_5.model.Flavor;

/**
 * Adapter for displaying beverage flavors in a RecyclerView.
 * Each flavor is displayed with a unique image and name.
 * @authors
 * Anirudh Deveram, Karthik Penumetch
 */
public class FlavorAdapter extends RecyclerView.Adapter<FlavorAdapter.FlavorViewHolder> {

    private final Flavor[] flavors;
    private final Context context;
    private OnFlavorSelectedListener listener;

    /**
     * Interface for flavor selection callback.
     */
    public interface OnFlavorSelectedListener {
        void onFlavorSelected(Flavor flavor);
    }

    /**
     * Constructor for FlavorAdapter.
     *
     * @param context The application context
     * @param flavors Array of flavors to display
     */
    public FlavorAdapter(Context context, Flavor[] flavors) {
        this.context = context;
        this.flavors = flavors;
    }

    /**
     * Sets the flavor selection listener.
     *
     * @param listener The listener to set
     */
    public void setOnFlavorSelectedListener(OnFlavorSelectedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public FlavorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_flavor, parent, false);
        return new FlavorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlavorViewHolder holder, int position) {
        Flavor flavor = flavors[position];
        holder.tvFlavorName.setText(flavor.toString());

        // Set the appropriate image based on the flavor
        int imageResourceId = getImageResourceForFlavor(flavor);
        holder.ivFlavorImage.setImageResource(imageResourceId);

        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onFlavorSelected(flavor);
            }
        });
    }

    @Override
    public int getItemCount() {
        return flavors.length;
    }

    /**
     * ViewHolder for flavor items.
     */
    static class FlavorViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFlavorImage;
        TextView tvFlavorName;

        FlavorViewHolder(View itemView) {
            super(itemView);
            ivFlavorImage = itemView.findViewById(R.id.ivFlavorImage);
            tvFlavorName = itemView.findViewById(R.id.tvFlavorName);
        }
    }

    /**
     * Helper method to get the appropriate drawable resource ID for each flavor.
     *
     * @param flavor The flavor to get an image for
     * @return The resource ID of the flavor's image
     */

    private int getImageResourceForFlavor(Flavor flavor) {

        switch (flavor) {
            case COLA:
                return R.drawable.cola;
            case DIET_COLA:
                return R.drawable.diet_cola;
            case LEMON_LIME:
                return R.drawable.lemon_lime;
            case ROOT_BEER:
                return R.drawable.root_beer;
            case ORANGE_JUICE:
                return R.drawable.orange_juice;
            case GRAPE:
                return R.drawable.grape;
            case STRAWBERRY:
                return R.drawable.strawberry;
            case CHERRY:
                return R.drawable.cherry;
            case LEMONADE:
                return R.drawable.lemonade;
            case ICED_TEA:
                return R.drawable.iced_tea;
            case GREEN_TEA:
                return R.drawable.green_tea;
            case PEACH_TEA:
                return R.drawable.peach_tea;
            case COFFEE:
                return R.drawable.coffee;
            case CHOCOLATE_MILK:
                return R.drawable.chocolate_milk;
            case WATER:
                return R.drawable.water;
            default:
                return R.drawable.default_drink;
        }


    }


}