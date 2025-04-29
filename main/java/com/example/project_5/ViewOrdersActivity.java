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

import com.example.project_5.model.MenuItem;
import com.example.project_5.model.Order;
import com.example.project_5.model.Ordering;

import java.util.ArrayList;

/**
 * Activity for viewing and managing stored orders.
 * @authors
 * Karthik Penumetch, Anirudh Deveram
 */
public class ViewOrdersActivity extends AppCompatActivity {

    private ListView listViewOrders;
    private ListView listViewOrderItems;
    private Button btnCancelOrder, btnClose;

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
        ArrayList<Order> orders = Ordering.getInstance()
                .getStoredOrder()
                .getOrders();

        ordersAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_activated_1,
                orders
        );
        listViewOrders.setAdapter(ordersAdapter);

        // Clear the items list when no order is selected
        listViewOrderItems.setAdapter(null);
        selectedOrderPosition = ListView.INVALID_POSITION;
        btnCancelOrder.setEnabled(false);
    }

    private void setupListeners() {
        listViewOrders.setOnItemClickListener((parent, view, position, id) -> {
            selectedOrderPosition = position;
            listViewOrders.setItemChecked(position, true);

            Order selected = ordersAdapter.getItem(position);
            if (selected != null) {
                showOrderItems(selected);
                btnCancelOrder.setEnabled(true);
            }
        });

        btnCancelOrder.setOnClickListener(v -> {
            if (selectedOrderPosition != ListView.INVALID_POSITION) {
                Order toCancel = ordersAdapter.getItem(selectedOrderPosition);
                new AlertDialog.Builder(ViewOrdersActivity.this)
                        .setTitle(R.string.cancel_order)
                        .setMessage(getString(
                                R.string.confirm_cancel_order_number,
                                toCancel.getNumber()))
                        .setPositiveButton(R.string.yes, (dlg, which) -> {
                            boolean ok = Ordering.getInstance()
                                    .getStoredOrder()
                                    .cancelOrder(toCancel.getNumber());
                            Toast.makeText(
                                            ViewOrdersActivity.this,
                                            ok ? R.string.order_cancelled : R.string.error_cancelling_order,
                                            Toast.LENGTH_SHORT)
                                    .show();
                            loadOrders();
                        })
                        .setNegativeButton(R.string.no, null)
                        .show();
            }
        });

        btnClose.setOnClickListener(v -> finish());
    }

    private void showOrderItems(Order order) {
        itemsAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                order.getItems()
        );
        listViewOrderItems.setAdapter(itemsAdapter);
    }
}
