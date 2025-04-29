package com.example.project_5;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.project_5.model.Beverage;
import com.example.project_5.model.Burger;
import com.example.project_5.model.Combo;
import com.example.project_5.model.Flavor;
import com.example.project_5.model.Protein;
import com.example.project_5.model.Sandwich;
import com.example.project_5.model.Side;
import com.example.project_5.model.SideOption;
import com.example.project_5.model.Size;
import com.example.project_5.model.Ordering;

/**
 * activity of combinations
 * @authors
 * Anirudh Deveram, Karthik Penumetch
 */
public class ComboActivity extends AppCompatActivity
{

    private Spinner spinnerSandwich, spinnerDrink, spinnerSide;
    private TextView tvPrice;
    private Button btnAddToOrder, btnCancel;
    private ImageView ivSideImage, ivDrinkImage;

    private Sandwich sandwich;
    private Beverage beverage;
    private Side side;
    private Combo combo;
    private String sourceActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combo);

        // Get extras from intent
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            sourceActivity = extras.getString("sourceActivity", "");
        }

        // Initialize models
        initializeModels();

        // Initialize UI components
        initializeViews();
        setupSpinners();
        setupListeners();
        updateCombo();
        updatePrice();
    }

    private void initializeModels()
    {
        // Create default sandwich/burger based on source activity
        if ("BurgerActivity".equals(sourceActivity)) {
            sandwich = new Burger();
        } else {
            sandwich = new Sandwich();
            sandwich.setProtein(Protein.ROAST_BEEF);
        }

        // Create beverage with default medium size for combo
        beverage = new Beverage(Size.MEDIUM, Flavor.COLA);

        // Create side with default small size for combo
        side = new Side(Size.SMALL, SideOption.CHIPS);

        // Create the combo with the components
        combo = new Combo(sandwich, beverage, side);
    }
    private void initializeViews()
    {
        spinnerSandwich = findViewById(R.id.spinnerSandwich);
        spinnerDrink = findViewById(R.id.spinnerDrink);
        spinnerSide = findViewById(R.id.spinnerSide);
        tvPrice = findViewById(R.id.tvPrice);
        btnAddToOrder = findViewById(R.id.btnAddToOrder);
        btnCancel = findViewById(R.id.btnCancel);
        ivSideImage = findViewById(R.id.ivSideImage);
        ivDrinkImage = findViewById(R.id.ivDrinkImage);
    }

    private void setupSpinners()
    {
        String[] sandwichOptions;
        if ("BurgerActivity".equals(sourceActivity))
        {
            sandwichOptions = new String[] {"Burger"};
        }
        else
        {
            sandwichOptions = new String[] {"Roast Beef Sandwich", "Salmon Sandwich", "Chicken Sandwich"};
        }
        ArrayAdapter<String> sandwichAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, sandwichOptions);
        sandwichAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSandwich.setAdapter(sandwichAdapter);

        // Setup drink spinner
        ArrayAdapter<Flavor> drinkAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, Flavor.values());
        drinkAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDrink.setAdapter(drinkAdapter);

        // Setup side spinner
        ArrayAdapter<SideOption> sideAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, SideOption.values());
        sideAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSide.setAdapter(sideAdapter);
    }
    private void setupListeners() {
        spinnerSandwich.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id)
            {
                updateSandwichFromSelection(position);
                updateCombo();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent)
            {
            }
        });
        spinnerDrink.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id)
            {
                Flavor selectedFlavor = (Flavor) parent.getItemAtPosition(position);
                beverage.setFlavor(selectedFlavor);
                updateDrinkImage(selectedFlavor);
                updateCombo();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent)
            {
            }
        });

        // Side spinner listener
        spinnerSide.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id)
            {
                SideOption selectedSideOption = (SideOption) parent.getItemAtPosition(position);
                side.setSide(selectedSideOption);
                updateSideImage(selectedSideOption);
                updateCombo();
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent)
            {
            }
        });

        // Button listeners
        btnAddToOrder.setOnClickListener(v -> addToOrder());
        btnCancel.setOnClickListener(v -> finish());
    }

    /**
     * Updates the sandwich object based on selection
     */
    private void updateSandwichFromSelection(int position)
    {
        if ("BurgerActivity".equals(sourceActivity))
        {
            return;
        }

        Protein selectedProtein;
        switch (position) {
            case 1:
                selectedProtein = Protein.SALMON;
                break;
            case 2:
                selectedProtein = Protein.CHICKEN;
                break;
            default:
                selectedProtein = Protein.ROAST_BEEF;
                break;
        }

        sandwich.setProtein(selectedProtein);
    }
    private void updateDrinkImage(Flavor flavor)
    {
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

        ivDrinkImage.setImageResource(imageResId);


    }


    private void updateSideImage(SideOption sideOption)
    {
        int imageResId;


        // Set appropriate image based on side option
        switch (sideOption) {
            case CHIPS:
                imageResId = R.drawable.side_chips;
                break;
            case FRIES:
                imageResId = R.drawable.side_fries;
                break;
            case ONION_RINGS:
                imageResId = R.drawable.side_onion_rings;
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


    private void updateCombo()
    {
        combo = new Combo(sandwich, beverage, side);
        updatePrice();
    }

    private void updatePrice()
    {
        tvPrice.setText(String.format("$%.2f", combo.cost()));
    }


    private void addToOrder()
    {
        // Get the singleton instance
        Ordering order = Ordering.getInstance();

        // Add combo to order
        order.getCurrent().addItem(combo);

        // Show confirmation toast
        Toast.makeText(this, R.string.item_added, Toast.LENGTH_SHORT).show();

        // Close activity
        finish();
    }
}