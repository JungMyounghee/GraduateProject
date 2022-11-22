package com.example.myhomefood.BasicInfoDirectory;

public class BasicInfo {
    private String recipe_id;
    private String recipe_name;
    private String summary;
    private String nation_name;
    private String type_name;
    private String cooking_time;
    private String calorie;
    private String quantity;
    private String level;
    private String ingredients_code;
    private String price;

    public BasicInfo(String recipe_id, String recipe_name, String summary, String nation_name, String type_name, String cooking_time, String calorie, String quantity, String level, String ingredients_code, String price) {
        this.recipe_id = recipe_id;
        this.recipe_name = recipe_name;
        this.summary = summary;
        this.nation_name = nation_name;
        this.type_name = type_name;
        this.cooking_time = cooking_time;
        this.calorie = calorie;
        this.quantity = quantity;
        this.level = level;
        this.ingredients_code = ingredients_code;
        this.price = price;
    }

    public BasicInfo() {

    }

    public String getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(String recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getRecipe_name() {
        return recipe_name;
    }

    public void setRecipe_name(String recipe_name) {
        this.recipe_name = recipe_name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getNation_name() {
        return nation_name;
    }

    public void setNation_name(String nation_name) {
        this.nation_name = nation_name;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getCooking_time() {
        return cooking_time;
    }

    public void setCooking_time(String cooking_time) {
        this.cooking_time = cooking_time;
    }

    public String getCalorie() {
        return calorie;
    }

    public void setCalorie(String calorie) {
        this.calorie = calorie;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getIngredients_code() {
        return ingredients_code;
    }

    public void setIngredients_code(String ingredients_code) {
        this.ingredients_code = ingredients_code;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
