package com.example.clicklap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.clicklap.adapter.ProductImageAdapter;
import com.example.clicklap.adapter.RecommendedProductAdapter;

import java.util.ArrayList;
import java.util.List;

public class FullViewProduct extends AppCompatActivity {

    // UI Components
    private ImageView btnBack, btnCart, btnWishlist;
    private TextView tvCartCount, tvImageIndicator;
    private ViewPager2 viewPagerProductImages;
    private TextView tvProductTitle, tvRating, tvReviewCount, tvPrice, tvOriginalPrice;
    private TextView tvInstallment, tvDescription, tvSeeAllReviews;
    private Button btnAddToCart, btnBuyNow;
    private RecyclerView rvRecommendedProducts;

    // Data
    private ProductImageAdapter imageAdapter;
    private RecommendedProductAdapter recommendedAdapter;
    private List<String> productImages;
    private List<Product> recommendedProducts;
    private Product currentProduct;
    private boolean isWishlisted = false;
    private int cartCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_view_product);

        initViews();
        setupData();
        setupListeners();
        setupViewPager();
        setupRecommendedProducts();
    }

    private void initViews() {
        // Header
        btnBack = findViewById(R.id.btn_back);
        btnCart = findViewById(R.id.btn_cart);
        tvCartCount = findViewById(R.id.tv_cart_count);

        // Product Images
        viewPagerProductImages = findViewById(R.id.viewpager_product_images);
        btnWishlist = findViewById(R.id.btn_wishlist);
        tvImageIndicator = findViewById(R.id.tv_image_indicator);

        // Product Info
        tvProductTitle = findViewById(R.id.tv_product_title);
        tvRating = findViewById(R.id.tv_rating);
        tvReviewCount = findViewById(R.id.tv_review_count);
        tvPrice = findViewById(R.id.tv_price);
        tvOriginalPrice = findViewById(R.id.tv_original_price);
        tvInstallment = findViewById(R.id.tv_installment);
        tvDescription = findViewById(R.id.tv_description);

        // Reviews
        tvSeeAllReviews = findViewById(R.id.tv_see_all_reviews);

        // Recommended Products
        rvRecommendedProducts = findViewById(R.id.rv_recommended_products);

        // Bottom Navigation
        btnAddToCart = findViewById(R.id.btn_add_to_cart);
        btnBuyNow = findViewById(R.id.btn_buy_now);
    }

    private void setupData() {
        // Get product data from intent or initialize sample data
        currentProduct = getProductFromIntent();

        // Setup product images
        productImages = new ArrayList<>();
        productImages.add("image1_url");
        productImages.add("image2_url");
        productImages.add("image3_url");
        productImages.add("image4_url");
        productImages.add("image5_url");

        // Setup recommended products
        recommendedProducts = new ArrayList<>();
        recommendedProducts.add(new Product("1", "ASUS ROG Strix G15", "Rp28.999.000", "Rp32.999.000", "image_url", 4.8f, 850));
        recommendedProducts.add(new Product("2", "MSI Gaming Laptop", "Rp25.499.000", "Rp29.999.000", "image_url", 4.7f, 650));
        recommendedProducts.add(new Product("3", "Acer Predator Helios", "Rp31.999.000", "Rp35.999.000", "image_url", 4.6f, 420));
        recommendedProducts.add(new Product("4", "HP Omen Gaming", "Rp22.999.000", "Rp26.999.000", "image_url", 4.5f, 380));

        // Update cart count
        updateCartCount();
    }

    private Product getProductFromIntent() {
        // Get product data from intent extras
        Intent intent = getIntent();
        if (intent != null) {
            String productId = intent.getStringExtra("product_id");
            String title = intent.getStringExtra("product_title");
            String price = intent.getStringExtra("product_price");
            String originalPrice = intent.getStringExtra("product_original_price");
            String imageUrl = intent.getStringExtra("product_image");
            float rating = intent.getFloatExtra("product_rating", 0.0f);
            int reviewCount = intent.getIntExtra("product_review_count", 0);

            return new Product(productId, title, price, originalPrice, imageUrl, rating, reviewCount);
        }

        // Return sample product if no data from intent
        return new Product("sample_id",
                "ASUS ROG Strix G16 G615LM-I9N56C6G-HM – Eclipse Gray [Intel Core Ultra 9 Processor 275HX / NVIDIA GeForce RTX 4060 / 16GB RAM / 512GB SSD / Windows 11 Home]",
                "Rp35.799.000",
                "Rp39.999.000",
                "sample_image_url",
                4.9f,
                11600);
    }

    private void setupListeners() {
        // Back button
        btnBack.setOnClickListener(v -> onBackPressed());

        // Cart button
        btnCart.setOnClickListener(v -> {
            Intent intent = new Intent(FullViewProduct.this, CartActivity.class);
            startActivity(intent);
        });

        // Wishlist button
        btnWishlist.setOnClickListener(v -> toggleWishlist());

        // See all reviews
//        tvSeeAllReviews.setOnClickListener(v -> {
//            Intent intent = new Intent(FullViewProduct.this, ReviewsActivity.class);
//            intent.putExtra("product_id", currentProduct.getId());
//            startActivity(intent);
//        });

        // Add to cart button
        btnAddToCart.setOnClickListener(v -> addToCart());

        // Buy now button
        btnBuyNow.setOnClickListener(v -> buyNow());
    }

    private void setupViewPager() {
        imageAdapter = new ProductImageAdapter(this, productImages);
        viewPagerProductImages.setAdapter(imageAdapter);

        // Update image indicator
        updateImageIndicator(0);

        viewPagerProductImages.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateImageIndicator(position);
            }
        });
    }

    private void setupRecommendedProducts() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvRecommendedProducts.setLayoutManager(layoutManager);

        recommendedAdapter = new RecommendedProductAdapter(this, recommendedProducts);
        rvRecommendedProducts.setAdapter(recommendedAdapter);

        recommendedAdapter.setOnItemClickListener(product -> {
            Intent intent = new Intent(FullViewProduct.this, FullViewProduct.class);
            intent.putExtra("product_id", product.getId());
            intent.putExtra("product_title", product.getTitle());
            intent.putExtra("product_price", product.getPrice());
            intent.putExtra("product_original_price", product.getOriginalPrice());
            intent.putExtra("product_image", product.getImageUrl());
            intent.putExtra("product_rating", product.getRating());
            intent.putExtra("product_review_count", product.getReviewCount());
            startActivity(intent);
        });
    }

    private void updateImageIndicator(int position) {
        String indicator = (position + 1) + "/" + productImages.size();
        tvImageIndicator.setText(indicator);
    }

    private void toggleWishlist() {
        isWishlisted = !isWishlisted;

        if (isWishlisted) {
            btnWishlist.setImageResource(R.drawable.ic_favorite_filled);
            addToWishlist();
            Toast.makeText(this, "Ditambahkan ke wishlist", Toast.LENGTH_SHORT).show();
        } else {
            btnWishlist.setImageResource(R.drawable.ic_favorite_border);
            removeFromWishlist();
            Toast.makeText(this, "Dihapus dari wishlist", Toast.LENGTH_SHORT).show();
        }
    }

    private void addToWishlist() {
        // Add product to wishlist database or shared preferences
        WishlistManager.getInstance().addToWishlist(currentProduct);
    }

    private void removeFromWishlist() {
        // Remove product from wishlist database or shared preferences
        WishlistManager.getInstance().removeFromWishlist(currentProduct.getId());
    }

    private void addToCart() {
        // Add product to cart
        CartManager.getInstance().addToCart(currentProduct, 1);

        // Update cart count
        cartCount++;
        updateCartCount();

        Toast.makeText(this, "Produk ditambahkan ke keranjang", Toast.LENGTH_SHORT).show();

        // Optional: Navigate to cart activity
        Intent intent = new Intent(FullViewProduct.this, CartActivity.class);
        startActivity(intent);
    }

    private void buyNow() {
        // Create temporary cart item for checkout
        List<CartItem> checkoutItems = new ArrayList<>();
        checkoutItems.add(new CartItem(currentProduct, 1));

        // Navigate to checkout
        Intent intent = new Intent(FullViewProduct.this, CheckoutActivity.class);
        intent.putExtra("checkout_items", (ArrayList<CartItem>) checkoutItems);
        intent.putExtra("total_price", currentProduct.getPrice());
        startActivity(intent);
    }

    private void updateCartCount() {
        cartCount = CartManager.getInstance().getCartItemCount();

        if (cartCount > 0) {
            tvCartCount.setVisibility(View.VISIBLE);
            if (cartCount > 99) {
                tvCartCount.setText("99+");
            } else {
                tvCartCount.setText(String.valueOf(cartCount));
            }
        } else {
            tvCartCount.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Update cart count when returning from other activities
        updateCartCount();

        // Check if product is in wishlist
        isWishlisted = WishlistManager.getInstance().isInWishlist(currentProduct.getId());
        if (isWishlisted) {
            btnWishlist.setImageResource(R.drawable.ic_favorite_filled);
        } else {
            btnWishlist.setImageResource(R.drawable.ic_favorite_border);
        }
    }

    // Product class
    public static class Product {
        private String id;
        private String title;
        private String price;
        private String originalPrice;
        private String imageUrl;
        private float rating;
        private int reviewCount;

        public Product(String id, String title, String price, String originalPrice,
                       String imageUrl, float rating, int reviewCount) {
            this.id = id;
            this.title = title;
            this.price = price;
            this.originalPrice = originalPrice;
            this.imageUrl = imageUrl;
            this.rating = rating;
            this.reviewCount = reviewCount;
        }

        // Getters
        public String getId() { return id; }
        public String getTitle() { return title; }
        public String getPrice() { return price; }
        public String getOriginalPrice() { return originalPrice; }
        public String getImageUrl() { return imageUrl; }
        public float getRating() { return rating; }
        public int getReviewCount() { return reviewCount; }
    }

    // CartItem class
    public static class CartItem {
        private Product product;
        private int quantity;

        public CartItem(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }

        public Product getProduct() { return product; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }
}