package com.example.myhomefood;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DetailOfIngredient {
    private String recipe_id;
    //private ArrayList<ArrayList<String>> main_ingredients;
    //private ArrayList<ArrayList<String>> addition_ingredients;
    //private ArrayList<ArrayList<String>> source_ingredients;
    private ArrayList<ArrayList<String>> ingredients_list;

    public DetailOfIngredient(String recipe_id, ArrayList<ArrayList<String>> ingredients_list) {
        this.recipe_id = recipe_id;
        this.ingredients_list = ingredients_list;
    }

    public String getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(String recipe_id) {
        this.recipe_id = recipe_id;
    }

    public ArrayList<ArrayList<String>> getIngredients_list() {
        return ingredients_list;
    }

    public void setIngredients_list(ArrayList<ArrayList<String>> ingredients_list) {
        this.ingredients_list = ingredients_list;
    }

    /*
    public DetailOfIngredient(String recipe_id, ArrayList<ArrayList<String>> main_ingredients, ArrayList<ArrayList<String>> addition_ingredients, ArrayList<ArrayList<String>> source_ingredients) {
        this.recipe_id = recipe_id;
        this.main_ingredients = main_ingredients;
        this.addition_ingredients = addition_ingredients;
        this.source_ingredients = source_ingredients;
    }

    public String getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(String recipe_id) {
        this.recipe_id = recipe_id;
    }

    public ArrayList<ArrayList<String>> getMain_ingredients() {
        return main_ingredients;
    }

    public void setMain_ingredients(ArrayList<ArrayList<String>> main_ingredients) {
        this.main_ingredients = main_ingredients;
    }

    public ArrayList<ArrayList<String>> getAddition_ingredients() {
        return addition_ingredients;
    }

    public void setAddition_ingredients(ArrayList<ArrayList<String>> addition_ingredients) {
        this.addition_ingredients = addition_ingredients;
    }

    public ArrayList<ArrayList<String>> getSource_ingredients() {
        return source_ingredients;
    }

    public void setSource_ingredients(ArrayList<ArrayList<String>> source_ingredients) {
        this.source_ingredients = source_ingredients;
    }

     */
}
