package com.example.culinarycompanion;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class RecipeListActivity extends AppCompatActivity {

    LinearLayout navAddList, navHomeList, recipeListContainer;
    ImageView backButton;
    AppDatabase db;
    String category;

    private ActivityResultLauncher<Intent> detailActivityLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        navAddList = findViewById(R.id.navAdd);
        navHomeList = findViewById(R.id.navHome);
        backButton = findViewById(R.id.backButton);
        TextView categoryTitle = findViewById(R.id.categoryTitle);
        recipeListContainer = findViewById(R.id.recipeListContainer);

        db = AppDatabase.getInstance(this);
        category = getIntent().getStringExtra("category");

        if (category != null) {
            String title = getString(R.string.recipes, category);
            categoryTitle.setText(title);
        } else {
            String defaultCategory = getString(R.string.category);
            String title = getString(R.string.recipes, defaultCategory);
            categoryTitle.setText(title);
        }

        detailActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getBooleanExtra("recipeDeleted", false)) {
                            loadRecipes();
                        }
                    }
                }
        );

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

        loadRecipes();
    }

    private void loadRecipes() {
        List<Recipe> recipes;

        if (category != null) {
            recipes = db.recipeDao().getRecipesByCategory(category);
        } else {
            recipes = db.recipeDao().getAllRecipes();
        }

        recipeListContainer.removeAllViews();

        if (recipes.isEmpty()) {
            TextView emptyMessage = new TextView(this);
            emptyMessage.setTextColor(getResources().getColor(android.R.color.white));
            emptyMessage.setTextSize(16);
            String message = "There are no recipes saved under \"" + (category != null ? category : "All") +
                    "\", please '+ Add New' from below to create a new recipe.";
            emptyMessage.setText(message);
            emptyMessage.setPadding(16, 16, 16, 16);
            recipeListContainer.addView(emptyMessage);
            return;
        }

        LayoutInflater inflater = LayoutInflater.from(this);

        for (Recipe recipe : recipes) {
            View card = inflater.inflate(R.layout.single_recipe_card, recipeListContainer, false);

            TextView recipeCategory = card.findViewById(R.id.recipeCategory);
            TextView recipeName = card.findViewById(R.id.recipeName);
            Button viewButton = card.findViewById(R.id.viewButton);
            ImageView recipeImage = card.findViewById(R.id.recipeImage);

            recipeCategory.setText(recipe.category);
            recipeName.setText(recipe.title);

            if (recipe.imagePath != null && !recipe.imagePath.isEmpty()) {
                try {
                    Uri imageUri = Uri.parse(recipe.imagePath);
                    recipeImage.setImageURI(imageUri);
                } catch (Exception e) {
                    Log.e("RecipeListActivity", "Failed to load recipe image: " + recipe.imagePath, e);
                    recipeImage.setImageResource(R.drawable.ic_app_lis_image);
                }
            } else {
                recipeImage.setImageResource(R.drawable.ic_app_lis_image);
            }

            viewButton.setOnClickListener(v -> {
                Intent intent = new Intent(RecipeListActivity.this, RecipeDetailActivity.class);
                intent.putExtra("recipeId", recipe.id);
                detailActivityLauncher.launch(intent);
            });

            recipeListContainer.addView(card);
        }
    }
}
