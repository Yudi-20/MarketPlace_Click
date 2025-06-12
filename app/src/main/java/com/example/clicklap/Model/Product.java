package com.example.clicklap.Model;

public class Product {
    public String name;
    public String price;
    public int imageResource;

    public Product(String name, String price, int imageResource) {
        this.name = name;
        this.price = price;
        this.imageResource = imageResource;
    }

    // Getter methods (optional, untuk enkapsulasi yang lebih baik)
    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public int getImageResource() {
        return imageResource;
    }
}