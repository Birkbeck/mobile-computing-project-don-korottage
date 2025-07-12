package com.example.culinarycompanion;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView recipeTitleNav, recipeTitleView, recipeInstructions, recipeIngredients, recipeCategory;
    private ImageView backButton, recipeImage;
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
        recipeImage = findViewById(R.id.recipeImage);

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

        // ✅ Load image from URI if available
        if (currentRecipe.imagePath != null && !currentRecipe.imagePath.isEmpty()) {
            try {
                Uri imageUri = Uri.parse(currentRecipe.imagePath);
                recipeImage.setImageURI(imageUri);
            } catch (Exception e) {
                e.printStackTrace(); // Replace with proper logging if needed
                recipeImage.setImageResource(R.drawable.ic_app_lis_image); // fallback
            }
        } else {
            recipeImage.setImageResource(R.drawable.ic_app_lis_image); // fallback if no image
        }

        // Back button: go back to RecipeListActivity
        backButton.setOnClickListener(v -> finish());

        // Home button: go to HomeActivity
        navHome.setOnClickListener(v -> {
            Intent intent = new Intent(RecipeDetailActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        // Add new button
        navAdd.setOnClickListener(v -> {
            Intent intent = new Intent(RecipeDetailActivity.this, CreateRecipeActivity.class);
            startActivity(intent);
        });

        // Edit button
        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(RecipeDetailActivity.this, EditRecipeActivity.class);
            intent.putExtra("recipeId", currentRecipe.id);
            startActivity(intent);
        });

        // Delete button
        btnDelete.setOnClickListener(v -> {
            db.recipeDao().delete(currentRecipe);
            Toast.makeText(this, "Recipe deleted", Toast.LENGTH_SHORT).show();

            Intent resultIntent = new Intent();
            resultIntent.putExtra("recipeDeleted", true);
            setResult(RESULT_OK, resultIntent);

            finish();
        });
    }
}
