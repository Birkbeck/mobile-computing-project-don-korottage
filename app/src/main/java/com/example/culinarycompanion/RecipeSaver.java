package com.example.culinarycompanion;

// this class is made for testing purposes
public class RecipeSaver {

    public static boolean isValidRecipe(String title, String ingredients, String instructions, String category) {
        return title != null && !title.isEmpty()
                && ingredients != null && !ingredients.isEmpty()
                && instructions != null && !instructions.isEmpty()
                && category != null && !category.equals("Select Category");
    }
}
