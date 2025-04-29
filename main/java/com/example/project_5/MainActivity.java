package com.example.project_5;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.project_5.model.Ordering;

/**
 * activities for main method
 * @authors
 * Anirudh Deveram
 * Karthik Penumetch
 */
public class MainActivity extends AppCompatActivity {
    private Button burger, sandwich, beverage, side, viewOrder, storeOrders;
    private Ordering ordering;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ensure there is a current order
        ordering = Ordering.getInstance();
        if (ordering.getCurrent() == null) {
            ordering.createNew();
        }

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        burger      = findViewById(R.id.btnBurger);
        sandwich    = findViewById(R.id.btnSandwich);
        beverage    = findViewById(R.id.btnBeverage);
        side        = findViewById(R.id.btnSide);
        viewOrder   = findViewById(R.id.btnViewOrder);
        storeOrders = findViewById(R.id.btnStoreOrders);
    }

    private void setupClickListeners() {
        burger.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, BurgerActivity.class)));

        sandwich.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, SandwichActivity.class)));

        beverage.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, BeverageActivity.class)));

        side.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, SideActivity.class)));

        viewOrder.setOnClickListener(v -> {
            if (ordering.getCurrent() != null && !ordering.getCurrent().getItems().isEmpty()) {
                // Launch the order‐details screen
                startActivity(new Intent(MainActivity.this, OrderActivity.class));
            } else {
                Toast.makeText(
                        MainActivity.this,
                        getString(R.string.empty_order),
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        storeOrders.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, StoreOrdersActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ordering.getCurrent() == null) {
            ordering.createNew();
        }
    }
}
