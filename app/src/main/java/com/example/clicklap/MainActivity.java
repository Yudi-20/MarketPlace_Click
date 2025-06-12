package com.example.clicklap;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.clicklap.Fragment.HomeFragment;
import com.example.clicklap.Fragment.ProfileFragment;
import com.example.clicklap.Fragment.transactionFragment;
import com.example.clicklap.Fragment.trendingFragment;
import com.example.clicklap.Fragment.wishlistFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private LinearLayout headerLayout;
    private FrameLayout fragmentContainer;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Setup system bars BEFORE setContentView
        setupSystemBars();

        // Enable edge to edge
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);

        initViews();
        setupWindowInsets();
        setupBottomNavigation();
    }

    private void setupSystemBars() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        } else {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            );
        }
    }

    private void setupWindowInsets() {
        View mainLayout = findViewById(R.id.main);
        if (mainLayout != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainLayout, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

                // Apply side padding to main container
                v.setPadding(systemBars.left, 0, systemBars.right, 0);

                // Fragment akan handle padding untuk header mereka sendiri
                // MainActivity hanya handle bottom navigation
                if (bottomNavigationView != null) {
                    bottomNavigationView.setPadding(
                            bottomNavigationView.getPaddingLeft(),
                            bottomNavigationView.getPaddingTop(),
                            bottomNavigationView.getPaddingRight(),
                            systemBars.bottom
                    );
                }

                return insets;
            });
        }
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private void initViews() {
        headerLayout = findViewById(R.id.header_layout);
        fragmentContainer = findViewById(R.id.fragment_container);
        bottomNavigationView = findViewById(R.id.bottomNavigation);

        // Log untuk debugging
        if (headerLayout == null) {
            android.util.Log.e("MainActivity", "header_layout not found in activity_main.xml");
        }
        if (fragmentContainer == null) {
            android.util.Log.e("MainActivity", "fragment_container not found in activity_main.xml");
        }
        if (bottomNavigationView == null) {
            android.util.Log.e("MainActivity", "bottomNavigation not found in activity_main.xml");
        }
    }

    private void setupBottomNavigation() {
        if (bottomNavigationView == null) return;

        // Set default fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment())
                .commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    selectedFragment = new HomeFragment();
                } else if (itemId == R.id.nav_trending) {
                    selectedFragment = new trendingFragment();
                } else if (itemId == R.id.nav_wishlist) {
                    selectedFragment = new wishlistFragment();
                } else if (itemId == R.id.nav_transaction) {
                    selectedFragment = new transactionFragment();
                } else if (itemId == R.id.nav_profile) {
                    selectedFragment = new ProfileFragment();
                } else {
                    return false;
                }

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
                return true;
            }
        });
    }

    public int getHeaderHeight() {
        return headerLayout != null ? headerLayout.getHeight() : 0;
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}