package com.example.culinarycompanion;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class RecipeListActivity extends AppCompatActivity {

    LinearLayout navAddList, navHomeList, recipeListContainer;
    ImageView backButton;
    AppDatabase db;
    String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        // Bind UI
        navAddList = findViewById(R.id.navAdd);
        navHomeList = findViewById(R.id.navHome);
        backButton = findViewById(R.id.backButton);
        TextView categoryTitle = findViewById(R.id.categoryTitle);
        recipeListContainer = findViewById(R.id.recipeListContainer); // dynamic list holder

        db = AppDatabase.getInstance(this);
        category = getIntent().getStringExtra("category");

        if (category != null) {
            categoryTitle.setText(category);
        } else {
            categoryTitle.setText(R.string.recipes);
        }

        // Navigation
        Runnable goToHome = () -> {
            Intent intent = new Intent(RecipeListActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        };

        navHomeList.setOnClickListener(v -> goToHome.run());
        navAddList.setOnClickListener(v -> {
            Intent intent = new Intent(RecipeListActivity.this, CreateRecipeActivity.class);
            startActivity(intent);
        });
        backButton.setOnClickListener(v -> goToHome.run());

        // Load and display recipes
        loadRecipes();
    }

    private void loadRecipes() {
        List<Recipe> recipes;

        if (category != null) {
            recipes = db.recipeDao().getRecipesByCategory(category);
        } else {
            recipes = db.recipeDao().getAllRecipes();
        }

        LayoutInflater inflater = LayoutInflater.from(this);
        recipeListContainer.removeAllViews(); // Clear previous views

        for (Recipe recipe : recipes) {
            View card = inflater.inflate(R.layout.single_recipe_card, recipeListContainer, false);

            TextView recipeCategory = card.findViewById(R.id.recipeCategory);
            TextView recipeName = card.findViewById(R.id.recipeName);
            Button viewButton = card.findViewById(R.id.viewButton);

            recipeCategory.setText(recipe.category);
            recipeName.setText(recipe.title);

            viewButton.setOnClickListener(v -> {
                Intent intent = new Intent(RecipeListActivity.this, RecipeDetailActivity.class);
                intent.putExtra("recipeId", recipe.id);
                startActivity(intent);
            });

            recipeListContainer.addView(card);
        }
    }
}
