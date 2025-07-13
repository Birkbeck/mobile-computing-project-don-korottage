package com.example.culinarycompanion;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RecipeDao {

    // Insert a new recipe into the database
    @Insert
    void insert(Recipe recipe);

    // Update an existing recipe
    @Update
    void update(Recipe recipe);

    // Delete a recipe
    @Delete
    void delete(Recipe recipe);

    // Get all recipes, most recent first
    @Query("SELECT * FROM recipes ORDER BY id DESC")
    List<Recipe> getAllRecipes();

    // Get recipes by category, most recent first
    @Query("SELECT * FROM recipes WHERE category = :category ORDER BY id DESC")
    List<Recipe> getRecipesByCategory(String category);

    // Get a single recipe by its ID
    @Query("SELECT * FROM recipes WHERE id = :id LIMIT 1")
    Recipe getRecipeById(int id);
}
