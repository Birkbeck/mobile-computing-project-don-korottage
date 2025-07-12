package com.example.culinarycompanion;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
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

public class EditRecipeActivity extends AppCompatActivity {

    private EditText inputTitle, inputIngredients, inputInstructions;
    private Spinner categorySpinner;
    private TextView selectedImageName;
    private Button btnUploadImage, btnSaveChanges;
    private ImageView backButton;
    private LinearLayout navHome, navAdd;

    private AppDatabase db;
    private Recipe currentRecipe;
    private Uri selectedImageUri = null;

    private final ActivityResultLauncher<Intent> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {
                        // Persist URI permission to avoid SecurityException later
                        final int takeFlags = result.getData().getFlags() &
                                (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        selectedImageUri = uri;
                        String imageName = getFileNameFromUri(selectedImageUri);
                        selectedImageName.setText(imageName != null ? imageName : "Image selected");
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        // Bind views
        inputTitle = findViewById(R.id.inputTitle);
        inputIngredients = findViewById(R.id.inputIngredients);
        inputInstructions = findViewById(R.id.inputInstructions);
        categorySpinner = findViewById(R.id.inputCategory);
        selectedImageName = findViewById(R.id.selectedImageName);
        btnUploadImage = findViewById(R.id.btnUploadImage);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        backButton = findViewById(R.id.backButton);
        navHome = findViewById(R.id.navHome);
        navAdd = findViewById(R.id.navAdd);

        // Spinner setup
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.recipe_categories,
                R.layout.spinner_item
        );
        adapter.setDropDownViewResource(R.layout.spinner_item);
        categorySpinner.setAdapter(adapter);

        db = AppDatabase.getInstance(getApplicationContext());

        int recipeId = getIntent().getIntExtra("recipeId", -1);
        if (recipeId == -1) {
            Toast.makeText(this, "Invalid recipe ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        currentRecipe = db.recipeDao().getRecipeById(recipeId);
        if (currentRecipe == null) {
            Toast.makeText(this, "Recipe not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Populate fields
        inputTitle.setText(currentRecipe.title);
        inputIngredients.setText(currentRecipe.ingredients);
        inputInstructions.setText(currentRecipe.instructions);

        int spinnerPosition = adapter.getPosition(currentRecipe.category);
        if (spinnerPosition >= 0) {
            categorySpinner.setSelection(spinnerPosition);
        }

        // Display existing image name if any
        if (currentRecipe.imagePath != null && !currentRecipe.imagePath.isEmpty()) {
            selectedImageUri = Uri.parse(currentRecipe.imagePath);
            selectedImageName.setText(getFileNameFromUri(selectedImageUri));
        }

        // Upload new image
        btnUploadImage.setOnClickListener(v -> openImagePicker());

        // Save changes
        btnSaveChanges.setOnClickListener(v -> {
            String newTitle = inputTitle.getText().toString().trim();
            String newIngredients = inputIngredients.getText().toString().trim();
            String newInstructions = inputInstructions.getText().toString().trim();
            String newCategory = categorySpinner.getSelectedItem().toString();

            if (newTitle.isEmpty()) {
                inputTitle.setError("Title cannot be empty");
                inputTitle.requestFocus();
                return;
            }

            currentRecipe.title = newTitle;
            currentRecipe.ingredients = newIngredients;
            currentRecipe.instructions = newInstructions;
            currentRecipe.category = newCategory;

            if (selectedImageUri != null) {
                currentRecipe.imagePath = selectedImageUri.toString();
            }

            db.recipeDao().update(currentRecipe);
            Toast.makeText(this, "Recipe updated", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(EditRecipeActivity.this, RecipeDetailActivity.class);
            intent.putExtra("recipeId", currentRecipe.id);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        // Navigation
        backButton.setOnClickListener(v -> finish());
        navHome.setOnClickListener(v -> {
            Intent intent = new Intent(EditRecipeActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
        navAdd.setOnClickListener(v -> {
            Intent intent = new Intent(EditRecipeActivity.this, CreateRecipeActivity.class);
            startActivity(intent);
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        // Important: add these flags to get persistable permission
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        pickImageLauncher.launch(intent);
    }

    private String getFileNameFromUri(Uri uri) {
        String result = null;
        if ("content".equals(uri.getScheme())) {
            ContentResolver resolver = getContentResolver();
            try (Cursor cursor = resolver.query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex >= 0) {
                        result = cursor.getString(nameIndex);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }
}
