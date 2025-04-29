package com.example.project_5;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project_5.model.Order;
import com.example.project_5.model.Ordering;
import com.example.project_5.model.MenuItem;

import java.util.ArrayList;

/**
 * activity for storing orders
 * @authors
 * Anirudh Deveram, Karthik Penumetch
 */
public class StoreOrdersActivity extends AppCompatActivity {
    private ListView listViewOrders, listViewOrderItems;
    private Button btnCancelOrder, btnClose;
    // no more tvOrderDetails
    private ArrayAdapter<Order> ordersAdapter;
    private ArrayAdapter<MenuItem> itemsAdapter;
    private int selectedOrderPosition = ListView.INVALID_POSITION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_order);

        listViewOrders     = findViewById(R.id.listViewOrders);
        listViewOrderItems = findViewById(R.id.listViewOrderItems);
        btnCancelOrder     = findViewById(R.id.btnCancelOrder);
        btnClose           = findViewById(R.id.btnClose);

        listViewOrders.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        btnCancelOrder.setEnabled(false);

        loadOrders();
        setupListeners();
    }

    private void loadOrders() {
        ArrayList<Order> orders = Ordering
                .getInstance()
                .getStoredOrder()
                .getOrders();

        ordersAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_activated_1,
                orders
        );
        listViewOrders.setAdapter(ordersAdapter);

        // clear the items listview
        listViewOrderItems.setAdapter(null);
        selectedOrderPosition = ListView.INVALID_POSITION;
        btnCancelOrder.setEnabled(false);
    }

    private void setupListeners() {
        listViewOrders.setOnItemClickListener((parent, view, position, id) -> {
            selectedOrderPosition = position;
            listViewOrders.setItemChecked(position, true);

            Order picked = ordersAdapter.getItem(position);
            showOrderItems(picked);
            btnCancelOrder.setEnabled(true);
        });

        btnCancelOrder.setOnClickListener(v -> {
            Order toCancel = ordersAdapter.getItem(selectedOrderPosition);
            new AlertDialog.Builder(this)
                    .setTitle("Cancel Order")
                    .setMessage("Really cancel order #" + toCancel.getNumber() + "?")
                    .setPositiveButton("Yes", (dlg, which) -> {
                        Ordering.getInstance()
                                .getStoredOrder()
                                .cancelOrder(toCancel.getNumber());
                        loadOrders();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        btnClose.setOnClickListener(v -> finish());
    }

    private void showOrderItems(Order order) {
        // Turn order.getItems() into a ListView
        itemsAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                order.getItems()
        );
        listViewOrderItems.setAdapter(itemsAdapter);
    }
}
