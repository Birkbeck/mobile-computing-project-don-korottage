package com.example.culinarycompanion;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipes")
public class Recipe {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;
    public String ingredients;
    public String instructions;
    public String category;

    // Optional: path or name of uploaded image file (for future use)
    public String imagePath;
}
