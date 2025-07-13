package com.example.culinarycompanion;

import org.junit.Test;
import static org.junit.Assert.*;

public class RecipeSaverTest {

    @Test
    public void validRecipe_allFieldsFilled_returnsTrue() {
        // Check that a recipe with all valid fields passes validation
        boolean result = RecipeSaver.isValidRecipe("Pasta", "Noodles", "Boil it", "Dinner");
        assertTrue(result);
    }

    @Test
    public void invalidRecipe_emptyTitle_returnsFalse() {
        // Title is empty — validation should fail
        boolean result = RecipeSaver.isValidRecipe("", "Noodles", "Boil it", "Dinner");
        assertFalse(result);
    }

    @Test
    public void invalidRecipe_emptyIngredients_returnsFalse() {
        // Ingredients field is empty — should be invalid
        boolean result = RecipeSaver.isValidRecipe("Pasta", "", "Boil it", "Dinner");
        assertFalse(result);
    }

    @Test
    public void invalidRecipe_emptyInstructions_returnsFalse() {
        // Instructions missing — validation should fail
        boolean result = RecipeSaver.isValidRecipe("Pasta", "Noodles", "", "Dinner");
        assertFalse(result);
    }

    @Test
    public void invalidRecipe_defaultCategory_returnsFalse() {
        // Category is still set to the default placeholder — must be changed, so fail
        boolean result = RecipeSaver.isValidRecipe("Pasta", "Noodles", "Boil it", "Select Category");
        assertFalse(result);
    }
}
