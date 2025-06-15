package com.example.clicklap.Fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.clicklap.R;
import com.example.clicklap.adapter.ProductsAdapter;
import com.example.clicklap.Model.Product;
import java.util.ArrayList;
import java.util.List;

public class trendingFragment extends Fragment {

    private RecyclerView productsRecyclerView;
    private ProductsAdapter productAdapter;
    private List<Product> productList = new ArrayList<>();

    public trendingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trending, container, false);

        // Initialize RecyclerView
        productsRecyclerView = view.findViewById(R.id.products_recycler_view);

        // Setup GridLayoutManager with 2 columns
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        productsRecyclerView.setLayoutManager(layoutManager);

        // Add sample products
        addSampleProducts();

        // Setup adapter - HANYA mengirim productList sesuai dengan constructor adapter
        productAdapter = new ProductsAdapter(productList);
        productsRecyclerView.setAdapter(productAdapter);

        return view;
    }

    private void addSampleProducts() {
        productList.add(new Product("ROG Strix Scar RTX ", "Rp. 37.500.200", R.drawable.product_laptop_1));
        productList.add(new Product("Product 2", "Rp. 10.000.000", R.drawable.product_laptop_1));
        productList.add(new Product("Product 3", "Rp. 15.000.000", R.drawable.product_laptop_2));
        productList.add(new Product("Product 4", "Rp. 20.000.000", R.drawable.product_laptop_3));
        productList.add(new Product("ROG Strix Scar RTX ", "Rp. 37.500.200", R.drawable.product_laptop_1));
        productList.add(new Product("Product 2", "Rp. 10.000.000", R.drawable.product_laptop_1));
        productList.add(new Product("Product 3", "Rp. 15.000.000", R.drawable.product_laptop_2));
        productList.add(new Product("Product 4", "Rp. 20.000.000", R.drawable.product_laptop_3));
        productList.add(new Product("ROG Strix Scar RTX ", "Rp. 37.500.200", R.drawable.product_laptop_1));
        productList.add(new Product("Product 2", "Rp. 10.000.000", R.drawable.product_laptop_1));
        productList.add(new Product("Product 3", "Rp. 15.000.000", R.drawable.product_laptop_2));
        productList.add(new Product("Product 4", "Rp. 20.000.000", R.drawable.product_laptop_3));
        productList.add(new Product("ROG Strix Scar RTX ", "Rp. 37.500.200", R.drawable.product_laptop_1));
        productList.add(new Product("Product 2", "Rp. 10.000.000", R.drawable.product_laptop_1));
        productList.add(new Product("Product 3", "Rp. 15.000.000", R.drawable.product_laptop_2));
        productList.add(new Product("Product 4", "Rp. 20.000.000", R.drawable.product_laptop_3));
        productList.add(new Product("ROG Strix Scar RTX ", "Rp. 37.500.200", R.drawable.product_laptop_1));
        productList.add(new Product("Product 2", "Rp. 10.000.000", R.drawable.product_laptop_1));
        productList.add(new Product("Product 3", "Rp. 15.000.000", R.drawable.product_laptop_2));
        productList.add(new Product("Product 4", "Rp. 20.000.000", R.drawable.product_laptop_3));

    }
}