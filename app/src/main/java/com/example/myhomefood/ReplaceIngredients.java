package com.example.myhomefood;

public class ReplaceIngredients {
    private String ingredient;
    private String replace;

    public ReplaceIngredients() {
    }

    public ReplaceIngredients(String ingredient, String replace) {
        this.ingredient = ingredient;
        this.replace = replace;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getReplace() {
        return replace;
    }

    public void setReplace(String replace) {
        this.replace = replace;
    }
}
