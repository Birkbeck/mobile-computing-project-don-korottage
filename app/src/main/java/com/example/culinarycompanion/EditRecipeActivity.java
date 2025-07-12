package com.example.culinarycompanion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class EditRecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe); // Your layout

        // Initialize Spinner
        Spinner categorySpinner = findViewById(R.id.inputCategory);

        // Set Adapter for Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.recipe_categories,
                R.layout.spinner_item
        );


        adapter.setDropDownViewResource(R.layout.spinner_item);

        categorySpinner.setAdapter(adapter);

        // Initialize Upload Button
        Button uploadButton = findViewById(R.id.btnUploadImage);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Add image picker logic here
            }
        });

        // Initialize Save Changes Button
        Button saveButton = findViewById(R.id.btnSaveChanges);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Add save logic here
            }
        });

        // Home button navigation:
        LinearLayout navHome = findViewById(R.id.navHome);
        navHome.setOnClickListener(v -> {
            Intent intent = new Intent(EditRecipeActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        // Add New button navigation (navAdd) - opens CreateRecipeActivity
        LinearLayout navAdd = findViewById(R.id.navAdd);
        navAdd.setOnClickListener(v -> {
            Intent intent = new Intent(EditRecipeActivity.this, CreateRecipeActivity.class);
            startActivity(intent);
        });

        // Back button: go back to RecipeDetailActivity (or previous)
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            finish();
        });
    }
}
