# Fast Food Android App

An Android application for a food ordering system that allows users to order sandwiches, burgers, beverages, sides, and combo meals.

## Project Overview

This project is an Android conversion of a JavaFX desktop application. It demonstrates:
- Multiple Activity-based navigation
- Singleton pattern for app-wide data management
- RecyclerView implementation with custom adapters
- Custom layouts for different food categories
- Comprehensive exception handling

## Features

- **Menu Customization**: Order customized sandwiches, burgers, beverages, and sides
- **Combo Creation**: Combine items into combo meals for a discount
- **Order Management**: View, modify, and place orders
- **Order History**: View and cancel previously placed orders

## Technical Implementation

### Architecture
- **Model-View-Controller (MVC)** architecture
- **Singleton Pattern** for data sharing between activities
- **Android UI Components**: Activities, RecyclerView, ListView, Toast, AlertDialog

### Key Components

- **Activities**:
  - `MainActivity`: Main navigation hub with buttons for different food categories
  - `SandwichActivity`: Customizes and orders sandwiches
  - `BurgerActivity`: Customizes and orders burgers
  - `BeverageActivity`: Selects beverages with RecyclerView for flavors
  - `SideActivity`: Customizes and orders side items
  - `ComboActivity`: Creates combo meals
  - `OrderActivity`: Manages the current order
  - `StoreOrdersActivity`: Views and cancels stored orders

- **Adapters**:
  - `FlavorAdapter`: RecyclerView adapter for displaying beverage flavors with unique images

- **Models**:
  - `MenuItem`: Abstract base class for all menu items
  - `Sandwich`, `Burger`, `Beverage`, `Side`: Food item classes
  - `Combo`: Combines a sandwich/burger, beverage, and side
  - `Order`: Contains order information and items
  - `StoredOrder`: Manages multiple orders
  - Supporting Enums: `Bread`, `Protein`, `AddOns`, `Flavor`, `Size`, `SideOption`

### Custom UI Elements

- **RecyclerView** for beverage flavors with unique images
- **Custom drawable resources** for beverage flavors
- **Customized layouts** for each food category

## Setup and Installation

1. Clone the repository
2. Open the project in Android Studio
3. Build and run on a Pixel 3a XL API 34 emulator

## Requirements

- Android Studio
- Minimum SDK: API 34
- Target Device: Pixel 3a XL

## Project Structure

```
app/
├── src/
    ├── main/
        ├── java/
        │   └── com/
        │       └── example/
        │           └── project_5/
        │               ├── MainActivity.java
        │               ├── SandwichActivity.java
        │               ├── BurgerActivity.java
        │               ├── BeverageActivity.java
        │               ├── SideActivity.java
        │               ├── ComboActivity.java
        │               ├── OrderActivity.java
        │               ├── StoreOrdersActivity.java
        │               ├── adapters/
        │               │   └── FlavorAdapter.java
        │               └── model/
        │                   ├── AddOns.java
        │                   ├── Beverage.java
        │                   ├── Bread.java
        │                   ├── Burger.java
        │                   ├── Combo.java
        │                   ├── Flavor.java
        │                   ├── MenuItem.java
        │                   ├── Order.java
        │                   ├── Protein.java
        │                   ├── Sandwich.java
        │                   ├── Side.java
        │                   ├── SideOption.java
        │                   ├── Size.java
        │                   ├── StoredOrder.java
        │                   └── StoredOrderSingleton.java
        ├── res/
            ├── drawable/
            │   ├── cola.xml
            │   ├── diet_cola.xml
            │   ├── default_drink.xml
            │   └── ...
            ├── layout/
            │   ├── activity_main.xml
            │   ├── activity_sandwich.xml
            │   ├── activity_burger.xml
            │   ├── activity_beverage.xml
            │   ├── activity_side.xml
            │   ├── activity_combo.xml
            │   ├── activity_order.xml
            │   ├── activity_store_orders.xml
            │   └── item_flavor.xml
            ├── mipmap/
            │   └── ic_launcher.xml
            └── values/
                └── strings.xml
```

## Development Notes

- Uses Android's standard UI components and design patterns
- Implements proper exception handling to prevent crashes
- Provides consistent navigation between screens
- Uses string resources to avoid hardcoded text
- Custom launcher icon for brand identification

## Future Enhancements

- Implementation of user authentication
- Local database storage for order persistence
- Payment processing integration
- Enhanced UI with animations and transitions
- Order tracking functionality
