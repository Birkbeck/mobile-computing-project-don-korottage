package com.example.culinarycompanion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome); // Links to activity_welcome.xml

        // Find the button by its ID
        Button btnSeeRecipes = findViewById(R.id.btnSeeRecipes);

        // Set a click listener on the button
        btnSeeRecipes.setOnClickListener(view -> {
            // Create an Intent to start HomeActivity
            Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
            startActivity(intent); // Launch HomeActivity
        });
    }
}
