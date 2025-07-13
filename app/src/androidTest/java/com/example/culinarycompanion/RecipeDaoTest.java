package com.example.culinarycompanion;

import static org.junit.Assert.*;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class RecipeDaoTest {

    private AppDatabase db;       // Our test database
    private RecipeDao recipeDao;

    @Before
    public void createDb() {
        // Set up a new in-memory database before each test runs
        // We allow queries on the main thread here just for simplicity in tests
        db = Room.inMemoryDatabaseBuilder(
                        ApplicationProvider.getApplicationContext(),
                        AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        recipeDao = db.recipeDao();
    }

    @After
    public void closeDb() {
        // Close the database after the test finishes to clean up resources
        db.close();
    }

    @Test
    public void insertAndGetRecipe() {
        // Create a recipe object with some sample data
        Recipe recipe = new Recipe();
        recipe.title = "Pancakes";
        recipe.ingredients = "Flour, Eggs, Milk";
        recipe.instructions = "Mix and cook";
        recipe.category = "Breakfast";
        recipe.imagePath = "";

        // Save it to the database
        recipeDao.insert(recipe);

        // Grab all recipes from the database (should be just the one we inserted)
        List<Recipe> allRecipes = recipeDao.getAllRecipes();

        // Check that there’s exactly one recipe stored
        assertEquals(1, allRecipes.size());

        // Double-check the details to make sure they saved correctly
        Recipe fetchedRecipe = allRecipes.get(0);
        assertEquals("Pancakes", fetchedRecipe.title);
        assertEquals("Breakfast", fetchedRecipe.category);
    }

    @Test
    public void updateRecipe_updatesData() {
        // Insert a recipe we’ll update later
        Recipe recipe = new Recipe();
        recipe.title = "Toast";
        recipe.ingredients = "Bread";
        recipe.instructions = "Toast bread";
        recipe.category = "Breakfast";
        recipe.imagePath = "";

        recipeDao.insert(recipe);

        // Get the recipe back from the database
        Recipe inserted = recipeDao.getAllRecipes().get(0);

        // Change the title of the recipe
        inserted.title = "Buttered Toast";

        // Update it in the database
        recipeDao.update(inserted);

        // Fetch it again to make sure the update worked
        Recipe updated = recipeDao.getRecipeById(inserted.id);
        assertEquals("Buttered Toast", updated.title);
    }

    @Test
    public void deleteRecipe_removesData() {
        // Add a recipe so we can delete it
        Recipe recipe = new Recipe();
        recipe.title = "Salad";
        recipe.ingredients = "Lettuce, Tomato";
        recipe.instructions = "Mix ingredients";
        recipe.category = "Lunch";
        recipe.imagePath = "";

        recipeDao.insert(recipe);

        // Grab the inserted recipe
        Recipe inserted = recipeDao.getAllRecipes().get(0);

        // Delete the recipe
        recipeDao.delete(inserted);

        // Check that the database is empty now
        List<Recipe> remaining = recipeDao.getAllRecipes();
        assertTrue(remaining.isEmpty());
    }

    @Test
    public void getRecipesByCategory_returnsCorrectRecipes() {
        // Create two recipes in different categories
        Recipe r1 = new Recipe();
        r1.title = "Omelette";
        r1.ingredients = "Eggs";
        r1.instructions = "Cook eggs";
        r1.category = "Breakfast";
        r1.imagePath = "";

        Recipe r2 = new Recipe();
        r2.title = "Cake";
        r2.ingredients = "Flour, Sugar";
        r2.instructions = "Bake it";
        r2.category = "Desserts";
        r2.imagePath = "";

        // Insert both into the database
        recipeDao.insert(r1);
        recipeDao.insert(r2);

        // Pull all recipes from the "Breakfast" category
        List<Recipe> breakfastRecipes = recipeDao.getRecipesByCategory("Breakfast");

        // Make sure we only get the Omelette recipe back
        assertEquals(1, breakfastRecipes.size());
        assertEquals("Omelette", breakfastRecipes.get(0).title);
    }
}
