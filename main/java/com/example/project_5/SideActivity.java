package com.example.project_5;


import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_5.model.Side;
import com.example.project_5.model.SideOption;
import com.example.project_5.model.Size;
import com.example.project_5.model.Ordering;

/**
 * Activity for customizing and ordering side items.
 * @authors
 * Anirudh Deveram, Karthik Penumetch
 */
public class SideActivity extends AppCompatActivity {

    private Spinner spinnerSideOption;
    private RadioGroup radioGroupSize;
    private RadioButton rbSmall, rbMedium, rbLarge;
    private EditText etQuantity;
    private TextView tvPrice;
    private Button btnAddToOrder, btnCancel;
    private ImageView ivSideImage;

    private Side side;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side);

        // Initialize side model
        side = new Side();

        // Initialize UI components
        initializeViews();
        setupSpinner();
        setupListeners();
        updatePrice();
    }

    /**
     * Initialize all the view components
     */
    private void initializeViews() {
        spinnerSideOption = findViewById(R.id.spinnerSideOption);
        radioGroupSize = findViewById(R.id.radioGroupSize);
        rbSmall = findViewById(R.id.rbSmall);
        rbMedium = findViewById(R.id.rbMedium);
        rbLarge = findViewById(R.id.rbLarge);
        etQuantity = findViewById(R.id.etQuantity);
        tvPrice = findViewById(R.id.tvPrice);
        btnAddToOrder = findViewById(R.id.btnAddToOrder);
        btnCancel = findViewById(R.id.btnCancel);
        ivSideImage = findViewById(R.id.ivSideImage);

        // Set default selections
        rbSmall.setChecked(true);
        etQuantity.setText("1");
    }

    /**
     * Set up the side option spinner
     */
    private void setupSpinner() {
        ArrayAdapter<SideOption> sideAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, SideOption.values());
        sideAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSideOption.setAdapter(sideAdapter);
    }

    /**
     * Set up event listeners
     */
    private void setupListeners() {
        // Side option spinner listener
        spinnerSideOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SideOption selectedSideOption = (SideOption) parent.getItemAtPosition(position);
                side.setSide(selectedSideOption);
                updateSideImage(selectedSideOption);
                updatePrice();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Size radio group listener
        radioGroupSize.setOnCheckedChangeListener((group, checkedId) -> {
            Size selectedSize;
            if (checkedId == R.id.rbMedium) {
                selectedSize = Size.MEDIUM;
            } else if (checkedId == R.id.rbLarge) {
                selectedSize = Size.LARGE;
            } else {
                selectedSize = Size.SMALL;
            }
            side.setSize(selectedSize);
            updatePrice();
        });

        // Quantity change listener
        etQuantity.setOnEditorActionListener((v, actionId, event) -> {
            updateQuantity();
            return false;
        });

        // Button listeners
        btnAddToOrder.setOnClickListener(v -> addToOrder());
        btnCancel.setOnClickListener(v -> finish());
    }

    /**
     * Updates the side image based on selected option
     */
    private void updateSideImage(SideOption sideOption) {
        int imageResId = 0;

        // Set appropriate image based on side option
        switch (sideOption) {
            case CHIPS:
                imageResId = R.drawable.side_chips;
                break;
            case FRIES:
                //imageResId = R.drawable.side_fries;
                break;
            case ONION_RINGS:
                //imageResId = R.drawable.side_onion_rings;
                break;
            case APPLE_SLICES:
                imageResId = R.drawable.side_apple_slices;
                break;
            default:
                imageResId = R.mipmap.ic_launcher;
                break;
        }

        ivSideImage.setImageResource(imageResId);
    }

    /**
     * Updates the quantity from the EditText
     */
    private void updateQuantity() {
        try {
            int quantity = Integer.parseInt(etQuantity.getText().toString());
            if (quantity < 1) quantity = 1;
            if (quantity > 10) quantity = 10;
            side.setQuantity(quantity);
            etQuantity.setText(String.valueOf(quantity));
        } catch (NumberFormatException e) {
            etQuantity.setText("1");
            side.setQuantity(1);
        }
        updatePrice();
    }

    /**
     * Updates the price display
     */
    private void updatePrice() {
        tvPrice.setText(String.format("$%.2f", side.cost()));
    }

    /**
     * Adds the side to the current order
     */
    private void addToOrder() {
        // Ensure quantity is updated
        updateQuantity();

        // Get the singleton instance
        Ordering order = Ordering.getInstance();

        // Add side to order
        order.getCurrent().addItem(side);

        // Show confirmation toast
        Toast.makeText(this, R.string.item_added, Toast.LENGTH_SHORT).show();

        // Close activity
        finish();
    }
}