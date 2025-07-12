package com.example.culinarycompanion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    LinearLayout navAdd;
    LinearLayout navHome;

    // Category boxes
    LinearLayout boxBreakfast, boxBrunch, boxLunch, boxDinner, boxDesserts, boxOther;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Connect UI elements
        navAdd = findViewById(R.id.navAdd);
        navHome = findViewById(R.id.navHome);

        boxBreakfast = findViewById(R.id.boxBreakfast);
        boxBrunch = findViewById(R.id.boxBrunch);
        boxLunch = findViewById(R.id.boxLunch);
        boxDinner = findViewById(R.id.boxDinner);
        boxDesserts = findViewById(R.id.boxDesserts);
        boxOther = findViewById(R.id.boxOther);

        // Set click listener for Add New button
        navAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CreateRecipeActivity.class);
                startActivity(intent);
            }
        });

        // Home button click
        navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Optionally refresh or keep empty
            }
        });

        // Set click listeners for each category
        setupCategoryClick(boxBreakfast, "Breakfast");
        setupCategoryClick(boxBrunch, "Brunch");
        setupCategoryClick(boxLunch, "Lunch");
        setupCategoryClick(boxDinner, "Dinner");
        setupCategoryClick(boxDesserts, "Desserts");
        setupCategoryClick(boxOther, "Other");
    }

    private void setupCategoryClick(LinearLayout categoryBox, final String categoryName) {
        categoryBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, RecipeListActivity.class);
                intent.putExtra("category", categoryName);
                startActivity(intent);
            }
        });
    }
}
