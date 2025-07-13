package com.example.culinarycompanion;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class CreateRecipeActivity extends AppCompatActivity {

    private EditText etTitle, etIngredients, etInstructions;
    private Spinner spinnerCategory;
    private TextView selectedImageName;

    private AppDatabase db;
    private Uri selectedImageUri = null;

    // Launcher to pick image from device storage
    private final ActivityResultLauncher<Intent> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null) {
                        // Take persistable URI permission so Room can access it later
                        getContentResolver().takePersistableUriPermission(
                                selectedImageUri,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                        );

                        String imageName = getFileNameFromUri(selectedImageUri);
                        selectedImageName.setText(imageName != null ? imageName : "Image selected");
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);

        // Initialize UI elements
        etTitle = findViewById(R.id.etTitle);
        etIngredients = findViewById(R.id.etIngredients);
        etInstructions = findViewById(R.id.etInstructions);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        selectedImageName = findViewById(R.id.selectedImageName);
        Button btnUploadImage = findViewById(R.id.btnUploadImage);
        Button btnAddRecipe = findViewById(R.id.btnAddRecipe);

        db = AppDatabase.getInstance(this); // Get Room DB instance

        // Set up spinner with category values
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.recipe_categories,
                R.layout.spinner_item
        );
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerCategory.setAdapter(adapter);

        btnUploadImage.setOnClickListener(v -> openImagePicker());
        btnAddRecipe.setOnClickListener(v -> saveRecipe());

        // Navigation: back and home
        LinearLayout navHome = findViewById(R.id.navHome);
        navHome.setOnClickListener(v -> goToHomeScreen());

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> goToHomeScreen());
    }

    // Open Android file picker to select an image
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        pickImageLauncher.launch(intent);
    }

    // Get display name (e.g. filename) of a selected image URI
    private String getFileNameFromUri(Uri uri) {
        String result = null;
        if (Objects.equals(uri.getScheme(), "content")) {
            ContentResolver resolver = getContentResolver();
            try (Cursor cursor = resolver.query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex >= 0) {
                        result = cursor.getString(nameIndex);
                    }
                }
            } catch (Exception e) {
                Log.e("CreateRecipeActivity", "Error getting file name from URI", e);
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }

    // Save recipe into Room database
    private void saveRecipe() {
        String title = etTitle.getText().toString().trim();
        String ingredients = etIngredients.getText().toString().trim();
        String instructions = etInstructions.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();

        if (title.isEmpty() || ingredients.isEmpty() || instructions.isEmpty() || category.equals("Select Category")) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Recipe recipe = new Recipe();
        recipe.title = title;
        recipe.ingredients = ingredients;
        recipe.instructions = instructions;
        recipe.category = category;

        if (selectedImageUri != null) {
            recipe.imagePath = selectedImageUri.toString();
        } else {
            recipe.imagePath = "";
        }

        db.recipeDao().insert(recipe); // Save to DB
        Toast.makeText(this, "Recipe saved!", Toast.LENGTH_SHORT).show();
        goToHomeScreen();
    }

    // Go back to home screen
    private void goToHomeScreen() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
