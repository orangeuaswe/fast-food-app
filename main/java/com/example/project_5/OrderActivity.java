package com.example.project_5;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project_5.model.MenuItem;
import com.example.project_5.model.Order;
import com.example.project_5.model.Ordering;


import java.util.ArrayList;

/**
 * Activity for viewing and managing the current order.
 * @authors
 * Anirudh Deveram
 * Karthik Penumetch
 */
public class OrderActivity extends AppCompatActivity {

    private ListView listViewOrderItems;
    private TextView tvOrderNumber, tvSubtotal, tvTax, tvTotal;
    private Button btnRemoveItem, btnClearOrder, btnPlaceOrder, btnClose;

    private Ordering order;
    private Order currentOrder;
    private ArrayAdapter<MenuItem> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        // Get the StoredOrderSingleton instance
        order = Ordering.getInstance();
        currentOrder = Ordering.getInstance().getCurrent();
        // If there's no current order, something went wrong
        if (currentOrder == null) {
            Toast.makeText(this, R.string.error_loading_order, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize UI components
        initializeViews();
        updateUI();
        setupListeners();
    }

    /**
     * Initialize all the view components
     */
    private void initializeViews() {
        listViewOrderItems = findViewById(R.id.listViewOrderItems);
        tvOrderNumber = findViewById(R.id.tvOrderNumber);
        tvSubtotal = findViewById(R.id.tvSubtotal);
        tvTax = findViewById(R.id.tvTax);
        tvTotal = findViewById(R.id.tvTotal);
        btnRemoveItem = findViewById(R.id.btnRemoveItem);
        btnClearOrder = findViewById(R.id.btnClearOrder);
        btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        btnClose = findViewById(R.id.btnClose);

        // Initially disable remove button until an item is selected
        btnRemoveItem.setEnabled(false);
    }

    /**
     * Update the UI with the current order data
     */
    private void updateUI() {
        // Set order number
        tvOrderNumber.setText(getString(R.string.order_number_placeholder, currentOrder.getNumber()));

        // Populate list view with order items
        ArrayList<MenuItem> items = currentOrder.getItems();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listViewOrderItems.setAdapter(adapter);

        // Update cost displays
        tvSubtotal.setText(String.format("$%.2f", currentOrder.getTotalCost()));
        tvTax.setText(String.format("$%.2f", currentOrder.getTax()));
        tvTotal.setText(String.format("$%.2f", currentOrder.getTotal()));

        // Enable or disable buttons based on whether there are items in the order
        boolean hasItems = !items.isEmpty();
        btnClearOrder.setEnabled(hasItems);
        btnPlaceOrder.setEnabled(hasItems);
    }

    /**
     * Set up the listeners for all interactive elements
     */
    private void setupListeners() {
        // List view item selection listener
        listViewOrderItems.setOnItemClickListener((parent, view, position, id) -> {
            btnRemoveItem.setEnabled(true);
        });

        // Button listeners
        btnRemoveItem.setOnClickListener(v -> handleRemoveItem());
        btnClearOrder.setOnClickListener(v -> handleClearOrder());
        btnPlaceOrder.setOnClickListener(v -> handlePlaceOrder());
        btnClose.setOnClickListener(v -> finish());
    }

    /**
     * Handle removing a selected item from the order
     */
    private void handleRemoveItem() {
        int position = listViewOrderItems.getCheckedItemPosition();
        if (position != ListView.INVALID_POSITION) {
            MenuItem item = adapter.getItem(position);
            if (item != null) {
                currentOrder.eradicateItem(item);
                adapter.notifyDataSetChanged();
                updateUI();

                // Disable remove button after removing an item
                btnRemoveItem.setEnabled(false);

                // Show confirmation toast
                Toast.makeText(this, R.string.item_removed, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Handle clearing all items from the order after confirmation
     */
    private void handleClearOrder() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.clear_order)
                .setMessage(R.string.confirm_clear_order)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    currentOrder.eradicateAllItems();
                    adapter.notifyDataSetChanged();
                    updateUI();

                    // Disable remove button after clearing the order
                    btnRemoveItem.setEnabled(false);

                    // Show confirmation toast
                    Toast.makeText(OrderActivity.this, R.string.order_cleared, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }

    /**
     * Handle placing the order after confirmation
     */
    private void handlePlaceOrder() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.place_order)
                .setMessage(R.string.confirm_place_order)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    // Finalize the order
                    order.finalize();

                    // Show confirmation toast
                    Toast.makeText(OrderActivity.this, R.string.order_placed, Toast.LENGTH_SHORT).show();

                    // Close the activity
                    finish();
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }
}