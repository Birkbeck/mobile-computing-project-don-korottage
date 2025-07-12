package com.example.culinarycompanion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeListActivity extends AppCompatActivity {

    LinearLayout navAddList;
    LinearLayout navHomeList;
    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        // Connect UI elements
        navAddList = findViewById(R.id.navAdd);
        navHomeList = findViewById(R.id.navHome);
        backButton = findViewById(R.id.backButton);
        TextView categoryTitle = findViewById(R.id.categoryTitle);
        // View button for recipe detail
        Button viewButton = findViewById(R.id.viewButton);

        // Get the passed category name
        String category = getIntent().getStringExtra("category");

        // Display the category title (optional)
        if (category != null) {
            categoryTitle.setText(category);
        } else {
            categoryTitle.setText("Recipes");
        }

        // Reusable navigation method to go home
        Runnable goToHome = () -> {
            Intent intent = new Intent(RecipeListActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        };

        // Bottom nav buttons listeners
        navHomeList.setOnClickListener(v -> goToHome.run());

        navAddList.setOnClickListener(v -> {
            Intent intent = new Intent(RecipeListActivity.this, CreateRecipeActivity.class);
            startActivity(intent);
        });

        // Back button listener goes home
        backButton.setOnClickListener(v -> goToHome.run());

        // View button opens recipe detail activity
        viewButton.setOnClickListener(v -> {
            Intent intent = new Intent(RecipeListActivity.this, RecipeDetailActivity.class);

            startActivity(intent);
        });

        // TODO: Load and display recipes based on the category
    }
}
