package com.example.project_5;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_5.model.Beverage;
import com.example.project_5.model.Flavor;
import com.example.project_5.model.Size;
import com.example.project_5.model.Ordering;

/**
 * Activity for customizing and ordering beverages.
 * @authors
 * Anirudh Deveram, Karthik Penumetch
 */
public class BeverageActivity extends AppCompatActivity {

    private Spinner spinnerFlavor;
    private RadioGroup radioGroupSize;
    private RadioButton rbSmall, rbMedium, rbLarge;
    private EditText etQuantity;
    private TextView tvPrice;
    private Button btnAddToOrder, btnCancel;
    private ImageView ivBeverageImage;

    private Beverage beverage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beverage);

        // Initialize beverage model
        beverage = new Beverage();

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
        spinnerFlavor = findViewById(R.id.spinnerFlavor);
        radioGroupSize = findViewById(R.id.radioGroupSize);
        rbSmall = findViewById(R.id.rbSmall);
        rbMedium = findViewById(R.id.rbMedium);
        rbLarge = findViewById(R.id.rbLarge);
        etQuantity = findViewById(R.id.etQuantity);
        tvPrice = findViewById(R.id.tvPrice);
        btnAddToOrder = findViewById(R.id.btnAddToOrder);
        btnCancel = findViewById(R.id.btnCancel);
        ivBeverageImage = findViewById(R.id.ivBeverageImage);



        // Set default selections
        rbSmall.setChecked(true);
        etQuantity.setText("1");
    }

    /**
     * Set up the flavor spinner
     */
    private void setupSpinner() {
        ArrayAdapter<Flavor> flavorAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, Flavor.values());
        flavorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFlavor.setAdapter(flavorAdapter);
    }

    /**
     * Set up event listeners
     */
    private void setupListeners() {
        // Flavor spinner listener
        spinnerFlavor.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                Flavor selectedFlavor = (Flavor) parent.getItemAtPosition(position);
                beverage.setFlavor(selectedFlavor);
                updateBeverageImage(selectedFlavor);
                updatePrice();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
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
            beverage.setSize(selectedSize);
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
     * Updates the beverage image based on selected flavor
     */
    private void updateBeverageImage(Flavor flavor) {
        int imageResId;

        // Set appropriate image based on flavor
        switch (flavor) {
            case COLA:
                imageResId = R.drawable.cola;
                break;
            case DIET_COLA:
                imageResId = R.drawable.diet_cola;
                break;
            case WATER:
                imageResId = R.drawable.water;
                break;
            case COFFEE:
                imageResId = R.drawable.coffee;
                break;
            // Add cases for other flavors as needed
            default:
                imageResId = R.drawable.default_drink;
                break;
        }

        ivBeverageImage.setImageResource(imageResId);
    }

    /**
     * Updates the quantity from the EditText
     */
    private void updateQuantity() {
        try {
            int quantity = Integer.parseInt(etQuantity.getText().toString());
            if (quantity < 1) quantity = 1;
            if (quantity > 10) quantity = 10;
            beverage.setQuantity(quantity);
            etQuantity.setText(String.valueOf(quantity));
        } catch (NumberFormatException e) {
            etQuantity.setText("1");
            beverage.setQuantity(1);
        }
        updatePrice();
    }

    /**
     * Updates the price display
     */
    private void updatePrice() {
        tvPrice.setText(String.format("$%.2f", beverage.cost()));
    }

    /**
     * Adds the beverage to the current order
     */
    private void addToOrder() {
        // Ensure quantity is updated
        updateQuantity();

        // Get the singleton instance
        Ordering order = Ordering.getInstance();

        // Add beverage to order
        order.getCurrent().addItem(beverage);

        // Show confirmation toast
        Toast.makeText(this, R.string.item_added, Toast.LENGTH_SHORT).show();

        // Close activity
        finish();
    }
}