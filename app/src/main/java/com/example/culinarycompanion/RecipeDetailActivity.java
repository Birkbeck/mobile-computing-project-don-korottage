package com.example.culinarycompanion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView recipeTitleNav, recipeTitleView, recipeInstructions, recipeIngredients, recipeCategory;
    private ImageView backButton;
    private LinearLayout navHome, navAdd;
    private Button btnEdit, btnDelete;

    private AppDatabase db;
    private Recipe currentRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        // Connect UI elements
        backButton = findViewById(R.id.backButton);
        navHome = findViewById(R.id.navHome);
        navAdd = findViewById(R.id.navAdd);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);

        recipeTitleNav = findViewById(R.id.recipeTitleNav);
        recipeTitleView = findViewById(R.id.recipeTitleView);
        recipeInstructions = findViewById(R.id.recipeInstructions);
        recipeIngredients = findViewById(R.id.recipeIngredients);
        recipeCategory = findViewById(R.id.recipeCategory);

        // Initialize database
        db = AppDatabase.getInstance(getApplicationContext());

        // Get recipe ID from intent extras
        int recipeId = getIntent().getIntExtra("recipeId", -1);
        if (recipeId == -1) {
            Toast.makeText(this, "Invalid recipe ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Fetch recipe from database
        currentRecipe = db.recipeDao().getRecipeById(recipeId);
        if (currentRecipe == null) {
            Toast.makeText(this, "Recipe not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Populate UI with recipe details
        recipeTitleNav.setText(currentRecipe.title);
        recipeTitleView.setText(currentRecipe.title);
        recipeInstructions.setText(currentRecipe.instructions);
        recipeIngredients.setText(currentRecipe.ingredients);
        recipeCategory.setText(currentRecipe.category);

        // TODO: Optionally load image from currentRecipe.imagePath if implemented

        // Back button: go back to RecipeListActivity
        backButton.setOnClickListener(v -> finish());

        // Home button: go to HomeActivity, clear stack so it's fresh
        navHome.setOnClickListener(v -> {
            Intent intent = new Intent(RecipeDetailActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        // Add new button: go to CreateRecipeActivity
        navAdd.setOnClickListener(v -> {
            Intent intent = new Intent(RecipeDetailActivity.this, CreateRecipeActivity.class);
            startActivity(intent);
        });

        // Edit button: go to EditRecipeActivity with current recipe ID
        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(RecipeDetailActivity.this, EditRecipeActivity.class);
            intent.putExtra("recipeId", currentRecipe.id);
            startActivity(intent);
        });

        // Delete button: delete recipe and go back to list
        btnDelete.setOnClickListener(v -> {
            db.recipeDao().delete(currentRecipe);
            Toast.makeText(this, "Recipe deleted", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
