package com.example.culinarycompanion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        // Connect UI elements
        ImageView backButton = findViewById(R.id.backButton);
        LinearLayout navHome = findViewById(R.id.navHome);
        LinearLayout navAdd = findViewById(R.id.navAdd);
        Button btnEdit = findViewById(R.id.btnEdit);

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

        // Edit button: go to EditRecipeActivity
        btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(RecipeDetailActivity.this, EditRecipeActivity.class);
            startActivity(intent);
        });
    }
}
