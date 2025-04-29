package com.example.project_5;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import com.example.project_5.model.AddOns;
import com.example.project_5.model.Bread;
import com.example.project_5.model.Burger;
import com.example.project_5.model.Ordering;
import android.widget.Toast;
import android.view.View;
/**
 * implementation of burger activity
 * @authors
 * Anirudh Deveram, Karthik Penumetch
 */
public class BurgerActivity extends AppCompatActivity
{
    private Spinner bread;
    private RadioGroup patty;
    private RadioButton singlePatty, doublePatty;
    private CheckBox lettuce, tomatoes, onions, avocado, cheese, combo;
    private EditText quantity;
    private TextView price;
    private Button addToOrder, cancel;
    private Burger borger;
    private boolean isCombo;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_burger);
        borger = new Burger();
        isCombo = false;
        initializeView();
        setupSpinners();
        setupListeners();
        updatePrice();
    }
    private void initializeView()
    {
        bread = findViewById(R.id.bread);
        patty = findViewById(R.id.patty);
        singlePatty = findViewById(R.id.singlePatty);
        doublePatty = findViewById(R.id.doublePatty);
        lettuce = findViewById(R.id.lettuce);
        tomatoes = findViewById(R.id.tomatoes);
        onions  = findViewById(R.id.onions);
        avocado = findViewById(R.id.avocado);
        cheese = findViewById(R.id.cheese);
        combo = findViewById(R.id.combo);
        quantity = findViewById(R.id.quantity);
        price = findViewById(R.id.price);
        addToOrder = findViewById(R.id.addToOrder);
        cancel = findViewById(R.id.cancel);
        singlePatty.setChecked(true);
        quantity.setText("1");
    }

    private void setupSpinners()
    {
        ArrayAdapter<Bread> breadAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, Bread.values());
        breadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bread.setAdapter(breadAdapter);
    }
    private void setupListeners()
    {
        bread.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                Bread selectedBread = (Bread) parent.getItemAtPosition(position);
                borger.setBread(selectedBread);
                updateBurger();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {

            }
        });
        patty.setOnCheckedChangeListener((group, checkedId) -> {
            borger.setDoublePatty(checkedId == R.id.doublePatty);
            updateBurger();
        });

        View.OnClickListener addonListener = v -> updateBurger();
        lettuce.setOnClickListener(addonListener);
        tomatoes.setOnClickListener(addonListener);
        onions.setOnClickListener(addonListener);
        avocado.setOnClickListener(addonListener);
        cheese.setOnClickListener(addonListener);

        // Combo checkbox listener
        combo.setOnClickListener(v -> {
            isCombo = combo.isChecked();
            updatePrice();
        });

        // Quantity change listener
        quantity.setOnEditorActionListener((v, actionId, event) -> {
            updateQuantity();
            return false;
        });

        // Button listeners
        addToOrder.setOnClickListener(v -> addToOrder());
        cancel.setOnClickListener(v -> finish());
    }
    private void updateBurger()
    {
        // Set bread from spinner
        borger.setBread((Bread) bread.getSelectedItem());

        // Set patty type
        borger.setDoublePatty(doublePatty.isChecked());

        // Update quantity
        updateQuantity();

        // Clear previous add-ons
        borger.getAddons().clear();

        // Add selected add-ons
        if (lettuce.isChecked()) borger.addAddOns(AddOns.LETTUCE);
        if (tomatoes.isChecked()) borger.addAddOns(AddOns.TOMATOES);
        if (onions.isChecked()) borger.addAddOns(AddOns.ONIONS);
        if (avocado.isChecked()) borger.addAddOns(AddOns.AVOCADO);
        if (cheese.isChecked()) borger.addAddOns(AddOns.CHEESE);

        // Update price display
        updatePrice();
    }
    private void updateQuantity()
    {
        try
        {
            int quantityValue = Integer.parseInt(quantity.getText().toString());
            if (quantityValue < 1) quantityValue = 1;
            if (quantityValue > 10) quantityValue = 10;
            borger.setQuantity(quantityValue);
            quantity.setText(String.valueOf(quantityValue));
        } catch (NumberFormatException e)
        {
            quantity.setText("1");
            borger.setQuantity(1);
        }
        updatePrice();
    }

    /**
     * Updates the price display
     */
    private void updatePrice()
    {
        double cost = borger.cost();
        // Add combo cost if applicable
        if (isCombo) cost += 2.00;
        price.setText(String.format("$%.2f", cost));
    }

    /**
     * Adds the burger to the current order
     */
    private void addToOrder()
    {
        updateQuantity();
        Ordering order = Ordering.getInstance();
        if (isCombo)
        {
            Intent intent = new Intent(BurgerActivity.this, ComboActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("sourceActivity", "BurgerActivity");
            // We need to create a new sandwich here because the combo will use it
            // But we'll recreate it in ComboActivity
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else
        {
            // Add burger to order
            order.getCurrent().addItem(borger);

            // Show confirmation toast
            Toast.makeText(this, R.string.item_added, Toast.LENGTH_SHORT).show();

            // Close activity
            finish();
        }
    }
}