package com.example.culinarycompanion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CreateRecipeActivity extends AppCompatActivity {

    private EditText etTitle, etIngredients, etInstructions;
    private Spinner spinnerCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);

        etTitle = findViewById(R.id.etTitle);
        etIngredients = findViewById(R.id.etIngredients);
        etInstructions = findViewById(R.id.etInstructions);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        Button btnAddRecipe = findViewById(R.id.btnAddRecipe);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.recipe_categories,
                R.layout.spinner_item
        );
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerCategory.setAdapter(adapter);

        btnAddRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTitle.getText().toString().trim();
                String ingredients = etIngredients.getText().toString().trim();
                String instructions = etInstructions.getText().toString().trim();
                String category = spinnerCategory.getSelectedItem().toString();

                if (title.isEmpty() || ingredients.isEmpty() || instructions.isEmpty() || category.equals("Select Category")) {
                    Toast.makeText(CreateRecipeActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CreateRecipeActivity.this, "Recipe added: " + title, Toast.LENGTH_LONG).show();

                    // TODO: Save to database or other storage
                }
            }
        });

        // Bottom nav: Home
        LinearLayout navHome = findViewById(R.id.navHome);
        navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToHomeScreen();
            }
        });

        // Back button
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToHomeScreen();
            }
        });
    }

    // Navigate back to Home screen
    private void goToHomeScreen() {
        Intent intent = new Intent(CreateRecipeActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
