package com.example.culinarycompanion;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RecipeDao {

    @Insert
    void insert(Recipe recipe);

    @Update
    void update(Recipe recipe);

    @Delete
    void delete(Recipe recipe);

    @Query("SELECT * FROM recipes ORDER BY id DESC")
    List<Recipe> getAllRecipes();

    @Query("SELECT * FROM recipes WHERE category = :category ORDER BY id DESC")
    List<Recipe> getRecipesByCategory(String category);

    @Query("SELECT * FROM recipes WHERE id = :id LIMIT 1")
    Recipe getRecipeById(int id);
}
