package com.example.clicklap;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

// CartManager.java
public class CartManager {
    private static CartManager instance;
    private static final String PREF_NAME = "CartPrefs";
    private static final String CART_KEY = "cart_items";

    private SharedPreferences sharedPreferences;
    private Gson gson;
    private List<FullViewProduct.CartItem> cartItems;

    private CartManager() {
        gson = new Gson();
        cartItems = new ArrayList<>();
    }

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void init(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        loadCartItems();
    }

    public void addToCart(FullViewProduct.Product product, int quantity) {
        // Check if product already exists in cart
        for (FullViewProduct.CartItem item : cartItems) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.setQuantity(item.getQuantity() + quantity);
                saveCartItems();
                return;
            }
        }

        // Add new item
        cartItems.add(new FullViewProduct.CartItem(product, quantity));
        saveCartItems();
    }

    public void removeFromCart(String productId) {
        cartItems.removeIf(item -> item.getProduct().getId().equals(productId));
        saveCartItems();
    }

    public void updateQuantity(String productId, int quantity) {
        for (FullViewProduct.CartItem item : cartItems) {
            if (item.getProduct().getId().equals(productId)) {
                if (quantity <= 0) {
                    removeFromCart(productId);
                } else {
                    item.setQuantity(quantity);
                }
                break;
            }
        }
        saveCartItems();
    }

    public List<FullViewProduct.CartItem> getCartItems() {
        return new ArrayList<>(cartItems);
    }

    public int getCartItemCount() {
        int count = 0;
        for (FullViewProduct.CartItem item : cartItems) {
            count += item.getQuantity();
        }
        return count;
    }

    public void clearCart() {
        cartItems.clear();
        saveCartItems();
    }

    private void saveCartItems() {
        if (sharedPreferences != null) {
            String json = gson.toJson(cartItems);
            sharedPreferences.edit().putString(CART_KEY, json).apply();
        }
    }

    private void loadCartItems() {
        if (sharedPreferences != null) {
            String json = sharedPreferences.getString(CART_KEY, null);
            if (json != null) {
                Type type = new TypeToken<List<FullViewProduct.CartItem>>(){}.getType();
                cartItems = gson.fromJson(json, type);
                if (cartItems == null) {
                    cartItems = new ArrayList<>();
                }
            }
        }
    }
}
