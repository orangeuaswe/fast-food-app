package com.example.project_5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_5.model.AddOns;
import com.example.project_5.model.Bread;
import com.example.project_5.model.Protein;
import com.example.project_5.model.Sandwich;
import com.example.project_5.model.Ordering;

/**
 * Activity for customizing and ordering sandwiches.
 * @authors
 * Anirudh Deveram, Karthik Penumetch
 */
public class SandwichActivity extends AppCompatActivity {

    private Spinner spinnerBread, spinnerProtein;
    private CheckBox cbLettuce, cbTomatoes, cbOnions, cbAvocado, cbCheese, cbCombo;
    private EditText etQuantity;
    private TextView tvPrice;
    private Button btnAddToOrder, btnCancel;

    private Sandwich sandwich;
    private boolean isCombo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sandwich);

        // Initialize sandwich model
        sandwich = new Sandwich();
        isCombo = false;

        // Initialize UI components
        initializeViews();
        setupSpinners();
        setupListeners();
        updatePrice();
    }

    /**
     * Initialize all the view components
     */
    private void initializeViews() {
        spinnerBread = findViewById(R.id.spinnerBread);
        spinnerProtein = findViewById(R.id.spinnerProtein);
        cbLettuce = findViewById(R.id.cbLettuce);
        cbTomatoes = findViewById(R.id.cbTomatoes);
        cbOnions = findViewById(R.id.cbOnions);
        cbAvocado = findViewById(R.id.cbAvocado);
        cbCheese = findViewById(R.id.cbCheese);
        cbCombo = findViewById(R.id.cbCombo);
        etQuantity = findViewById(R.id.etQuantity);
        tvPrice = findViewById(R.id.tvPrice);
        btnAddToOrder = findViewById(R.id.btnAddToOrder);
        btnCancel = findViewById(R.id.btnCancel);

        // Set default quantity
        etQuantity.setText("1");
    }

    /**
     * Set up the spinner adapters
     */
    private void setupSpinners() {
        // Setup bread spinner
        ArrayAdapter<Bread> breadAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, Bread.values());
        breadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBread.setAdapter(breadAdapter);

        // Setup protein spinner - exclude BEEF_PATTY as it's for burgers
        Protein[] proteins = {Protein.ROAST_BEEF, Protein.SALMON, Protein.CHICKEN};
        ArrayAdapter<Protein> proteinAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, proteins);
        proteinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProtein.setAdapter(proteinAdapter);
    }

    /**
     * Set up event listeners
     */
    private void setupListeners() {
        // Bread spinner listener
        spinnerBread.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                Bread selectedBread = (Bread) parent.getItemAtPosition(position);
                sandwich.setBread(selectedBread);
                updateSandwich();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Protein spinner listener
        spinnerProtein.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                Protein selectedProtein = (Protein) parent.getItemAtPosition(position);
                sandwich.setProtein(selectedProtein);
                updateSandwich();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Add-on checkboxes listeners
        View.OnClickListener addonListener = v -> updateSandwich();
        cbLettuce.setOnClickListener(addonListener);
        cbTomatoes.setOnClickListener(addonListener);
        cbOnions.setOnClickListener(addonListener);
        cbAvocado.setOnClickListener(addonListener);
        cbCheese.setOnClickListener(addonListener);

        // Combo checkbox listener
        cbCombo.setOnClickListener(v -> {
            isCombo = cbCombo.isChecked();
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
     * Updates the sandwich model based on UI selections
     */
    private void updateSandwich() {
        // Set bread from spinner
        sandwich.setBread((Bread) spinnerBread.getSelectedItem());

        // Set protein from spinner
        sandwich.setProtein((Protein) spinnerProtein.getSelectedItem());

        // Update quantity
        updateQuantity();

        // Clear previous add-ons
        sandwich.getAddons().clear();

        // Add selected add-ons
        if (cbLettuce.isChecked()) sandwich.addAddOns(AddOns.LETTUCE);
        if (cbTomatoes.isChecked()) sandwich.addAddOns(AddOns.TOMATOES);
        if (cbOnions.isChecked()) sandwich.addAddOns(AddOns.ONIONS);
        if (cbAvocado.isChecked()) sandwich.addAddOns(AddOns.AVOCADO);
        if (cbCheese.isChecked()) sandwich.addAddOns(AddOns.CHEESE);

        // Generate a descriptive name for the sandwich
        StringBuilder nameBuilder = new StringBuilder();
        nameBuilder.append(sandwich.getBread())
                .append(" Sandwich with ")
                .append(sandwich.getProtein());

        // If add-ons are present, append them to the name
        if (!sandwich.getAddons().isEmpty()) {
            nameBuilder.append(" (Add-ons: ");
            boolean first = true;
            for (AddOns addon : sandwich.getAddons()) {
                if (!first) {
                    nameBuilder.append(", ");
                }
                nameBuilder.append(addon);
                first = false;
            }
            nameBuilder.append(")");
        }
        sandwich.setName(nameBuilder.toString());

        // Update price display
        updatePrice();
    }

    /**
     * Updates the quantity from the EditText
     */
    private void updateQuantity() {
        try {
            int quantity = Integer.parseInt(etQuantity.getText().toString());
            if (quantity < 1) quantity = 1;
            if (quantity > 10) quantity = 10;
            sandwich.setQuantity(quantity);
            etQuantity.setText(String.valueOf(quantity));
        } catch (NumberFormatException e) {
            etQuantity.setText("1");
            sandwich.setQuantity(1);
        }
        updatePrice();
    }

    /**
     * Updates the price display
     */
    private void updatePrice() {
        double cost = sandwich.cost();
        // Add combo cost if applicable
        if (isCombo) cost += 2.00;
        tvPrice.setText(String.format("$%.2f", cost));
    }

    /**
     * Adds the sandwich to the current order
     */
    private void addToOrder() {
        // Ensure quantity is updated
        updateQuantity();

        // Get the singleton instance
        Ordering order = Ordering.getInstance();

        // If combo is selected, open ComboActivity
        if (isCombo) {
            Intent intent = new Intent(SandwichActivity.this, ComboActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("sourceActivity", "SandwichActivity");
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            // Add sandwich to order
            order.getCurrent().addItem(sandwich);

            // Show confirmation toast
            Toast.makeText(this, R.string.item_added, Toast.LENGTH_SHORT).show();

            // Close activity
            finish();
        }
    }
}