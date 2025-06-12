package com.example.clicklap.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.clicklap.R;
import com.example.clicklap.Model.Product; // Import model Product
import com.example.clicklap.adapter.ProductsAdapter;
import com.example.clicklap.adapter.PromoCarouselAdapter;
import com.google.android.material.tabs.TabLayout;
import com.example.clicklap.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {

    // Views
    private TextView cartBadge;
    private LinearLayout headerLayout;
    private CardView searchCardView;

    private Spinner addressSpinner;
    private String[] addresses = {"Rumah", "Kantor", "Kost", "Alamat Lain"};

    // Carousel
    private ViewPager2 promoViewPager;
    private LinearLayout dotsIndicator;
    private Timer carouselTimer;
    private Handler carouselHandler;
    private int currentCarouselPage = 0;
    private PromoCarouselAdapter promoAdapter;

    // Products section
    private NestedScrollView nestedScrollView;
    private LinearLayout forYouSection;
    private TabLayout tabLayout;
    private RecyclerView productsRecyclerView;
    private ProductsAdapter productsAdapter;

    // Data
    private List<PromoItem> promoItems;
    private List<Product> products;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initViews(view);
        setupPromoCarousel();
        setupTabLayout();
        setupProductsRecyclerView();
        setupScrollBehavior();
        setupAddressSpinner();


        return view;
    }

    private void initViews(View view) {
        searchCardView = view.findViewById(R.id.search_card_view);
        cartBadge = view.findViewById(R.id.cart_badge);
        headerLayout = view.findViewById(R.id.header_layout);
        addressSpinner = view.findViewById(R.id.address_spinner);


        promoViewPager = view.findViewById(R.id.promo_viewpager);
        dotsIndicator = view.findViewById(R.id.dots_indicator);
        nestedScrollView = view.findViewById(R.id.nested_scroll_view);
        forYouSection = view.findViewById(R.id.for_you_section);
        tabLayout = view.findViewById(R.id.tab_layout);
        productsRecyclerView = view.findViewById(R.id.products_recycler_view);

        carouselHandler = new Handler(Looper.getMainLooper());

        setupFragmentInsets(view);
    }

    private void setupAddressSpinner() {
        if (addressSpinner != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item, addresses);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            addressSpinner.setAdapter(adapter);
        }
    }

    private void setupFragmentInsets(View view) {
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            // Header padding
            if (headerLayout != null) {
                headerLayout.setPadding(
                        headerLayout.getPaddingLeft(),
                        systemBars.top + dpToPx(16),
                        headerLayout.getPaddingRight(),
                        dpToPx(40) // Sesuai dengan paddingBottom di XML
                );
            }

            // Search card position
            if (searchCardView != null) {
                ViewGroup.MarginLayoutParams searchParams =
                        (ViewGroup.MarginLayoutParams) searchCardView.getLayoutParams();
                searchParams.topMargin = systemBars.top + dpToPx(90); // Sesuai dengan XML
                searchCardView.setLayoutParams(searchParams);
            }

            // Nested scroll view
            if (nestedScrollView != null) {
                ViewGroup.MarginLayoutParams scrollParams =
                        (ViewGroup.MarginLayoutParams) nestedScrollView.getLayoutParams();
                scrollParams.topMargin = systemBars.top + dpToPx(120); // Sesuai dengan XML
                nestedScrollView.setLayoutParams(scrollParams);
            }

            return insets;
        });
    }

    private int dpToPx(int dp) {
        if (getContext() == null) return dp;
        float density = getContext().getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private void setupPromoCarousel() {
        promoItems = new ArrayList<>();
        promoItems.add(new PromoItem("Promo 1", R.drawable.promo_image_1));
        promoItems.add(new PromoItem("Promo 2", R.drawable.promo_image_2));
        promoItems.add(new PromoItem("Promo 3", R.drawable.promo_image_3));

        promoAdapter = new PromoCarouselAdapter(promoItems);
        promoViewPager.setAdapter(promoAdapter);

        setupDotsIndicator();
        startAutoCarousel();

        promoViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentCarouselPage = position;
                updateDotsIndicator(position);
            }
        });
    }

    private void setupDotsIndicator() {
        if (dotsIndicator == null || getContext() == null) return;

        dotsIndicator.removeAllViews();
        ImageView[] dots = new ImageView[promoItems.size()];

        for (int i = 0; i < promoItems.size(); i++) {
            dots[i] = new ImageView(getContext());
            dots[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.dot_inactive));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 0, 8, 0);
            dotsIndicator.addView(dots[i], params);
        }

        if (dots.length > 0 && getContext() != null) {
            dots[0].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.dot_active));
        }
    }

    private void updateDotsIndicator(int position) {
        if (dotsIndicator == null || getContext() == null) return;

        for (int i = 0; i < dotsIndicator.getChildCount(); i++) {
            ImageView dot = (ImageView) dotsIndicator.getChildAt(i);
            if (i == position) {
                dot.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.dot_active));
            } else {
                dot.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.dot_inactive));
            }
        }
    }

    private void startAutoCarousel() {
        if (carouselTimer != null) {
            carouselTimer.cancel();
        }

        carouselTimer = new Timer();
        carouselTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (carouselHandler != null && isAdded() && getActivity() != null) {
                    carouselHandler.post(() -> {
                        if (promoViewPager != null && promoItems != null && promoItems.size() > 0) {
                            if (currentCarouselPage == promoItems.size() - 1) {
                                currentCarouselPage = 0;
                            } else {
                                currentCarouselPage++;
                            }
                            promoViewPager.setCurrentItem(currentCarouselPage, true);
                        }
                    });
                }
            }
        }, 3000, 3000);
    }

    private void setupTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText("For You"));
        tabLayout.addTab(tabLayout.newTab().setText("Most Popular"));
        tabLayout.addTab(tabLayout.newTab().setText("Spring Sale"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                loadProductsForTab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupProductsRecyclerView() {
        products = new ArrayList<>();
        productsAdapter = new ProductsAdapter(products);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        productsRecyclerView.setLayoutManager(layoutManager);
        productsRecyclerView.setAdapter(productsAdapter);

        loadProductsForTab(0); // Load default tab
    }

    private void loadProductsForTab(int tabPosition) {
        products.clear();

        switch (tabPosition) {
            case 0: // For You
                products.add(new Product("ROG Strix Scar RTX 4090", "Rp. 37.500.200", R.drawable.product_laptop_1));
                products.add(new Product("ROG Strix Scar RTX 5080", "Rp. 77.500.200", R.drawable.product_laptop_2));
                products.add(new Product("Gaming Laptop ASUS", "Rp. 15.200.000", R.drawable.product_laptop_3));
                products.add(new Product("MacBook Pro M2", "Rp. 22.000.000", R.drawable.product_laptop_4));
                break;
            case 1: // Most Popular
                products.add(new Product("iPhone 15 Pro", "Rp. 18.500.000", R.drawable.product_phone_1));
                products.add(new Product("Samsung Galaxy S24", "Rp. 16.200.000", R.drawable.product_phone_2));
                break;
            case 2: // Spring Sale
                products.add(new Product("Gaming Mouse RGB", "Rp. 450.000", R.drawable.product_mouse_1));
                products.add(new Product("Mechanical Keyboard", "Rp. 850.000", R.drawable.product_keyboard_1));
                break;
        }

        productsAdapter.notifyDataSetChanged();
    }

    // Inner static class untuk PromoItem
    public static class PromoItem {
        public String title;
        public int imageResource;

        public PromoItem(String title, int imageResource) {
            this.title = title;
            this.imageResource = imageResource;
        }
    }

    private void setupScrollBehavior() {
        if (!(getActivity() instanceof MainActivity) || nestedScrollView == null || forYouSection == null) {
            return;
        }

        MainActivity mainActivity = (MainActivity) getActivity();

        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (forYouSection == null || mainActivity == null) return;

                int[] location = new int[2];
                forYouSection.getLocationOnScreen(location);
                int forYouTop = location[1];

                int headerHeight = mainActivity.getHeaderHeight();

                if (forYouTop <= headerHeight + 20) {
                    forYouSection.setTranslationY(scrollY - (forYouSection.getTop() - headerHeight - 20));
                } else {
                    forYouSection.setTranslationY(0);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (carouselTimer != null) {
            carouselTimer.cancel();
            carouselTimer = null;
        }

        if (carouselHandler != null) {
            carouselHandler.removeCallbacksAndMessages(null);
        }
    }
}