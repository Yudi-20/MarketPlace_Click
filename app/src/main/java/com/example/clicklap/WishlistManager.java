package com.example.clicklap;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

class WishlistManager {
    private static WishlistManager instance;
    private static final String PREF_NAME = "WishlistPrefs";
    private static final String WISHLIST_KEY = "wishlist_items";

    private SharedPreferences sharedPreferences;
    private Gson gson;
    private List<FullViewProduct.Product> wishlistItems;

    private WishlistManager() {
        gson = new Gson();
        wishlistItems = new ArrayList<>();
    }

    public static WishlistManager getInstance() {
        if (instance == null) {
            instance = new WishlistManager();
        }
        return instance;
    }

    public void init(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        loadWishlistItems();
    }

    public void addToWishlist(FullViewProduct.Product product) {
        // Check if product already exists
        if (!isInWishlist(product.getId())) {
            wishlistItems.add(product);
            saveWishlistItems();
        }
    }

    public void removeFromWishlist(String productId) {
        wishlistItems.removeIf(product -> product.getId().equals(productId));
        saveWishlistItems();
    }

    public boolean isInWishlist(String productId) {
        return wishlistItems.stream().anyMatch(product -> product.getId().equals(productId));
    }

    public List<FullViewProduct.Product> getWishlistItems() {
        return new ArrayList<>(wishlistItems);
    }

    public int getWishlistItemCount() {
        return wishlistItems.size();
    }

    public void clearWishlist() {
        wishlistItems.clear();
        saveWishlistItems();
    }

    private void saveWishlistItems() {
        if (sharedPreferences != null) {
            String json = gson.toJson(wishlistItems);
            sharedPreferences.edit().putString(WISHLIST_KEY, json).apply();
        }
    }

    private void loadWishlistItems() {
        if (sharedPreferences != null) {
            String json = sharedPreferences.getString(WISHLIST_KEY, null);
            if (json != null) {
                Type type = new TypeToken<List<FullViewProduct.Product>>(){}.getType();
                wishlistItems = gson.fromJson(json, type);
                if (wishlistItems == null) {
                    wishlistItems = new ArrayList<>();
                }
            }
        }
    }
}