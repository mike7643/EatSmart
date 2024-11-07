package com.boorisoogeo.EatSmart.ai;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FoodData {

    @JsonProperty("Food_ID")
    private String foodId;
    @JsonProperty("Fast_Food")
    private String fastFood;
    @JsonProperty("Ingredient")
    private String ingredient;
    @JsonProperty("Type")
    private String type;
    @JsonProperty("Process")
    private String process;
    @JsonProperty("raw/processed")
    private String rawProcessed;
    @JsonProperty("food group")
    private String foodGroup;
    @JsonProperty("Air (g)")
    private double air;
    @JsonProperty("Energy (Kcal)")
    private double energy;
    @JsonProperty("Protein (g)")
    private double protein;
    @JsonProperty("Fat (g)")
    private double fat;
    @JsonProperty("Carbo (g)")
    private double carbo;
    @JsonProperty("Fiber (g)")
    private double fiber;
    @JsonProperty("Ash (g)")
    private double ash;
    @JsonProperty("Calsium (Ca) (mg)")
    private double calcium;
    @JsonProperty("Phos (P) (mg)")
    private double phosphorus;
    @JsonProperty("Iron (Fe) (mg)")
    private double iron;
    @JsonProperty("Natrium (Na)(mg)")
    private double sodium;
    @JsonProperty("Kalium (Ka) (mg)")
    private double potassium;
    @JsonProperty("Copper (Cu) (mg)")
    private double copper;
    @JsonProperty("Zinc (Zn) (mg)")
    private double zinc;
    @JsonProperty("Retinol (vit. A) (mcg)")
    private double retinol;
    @JsonProperty("Karoten (mcg)")
    private double karoten;
    @JsonProperty("Karoten total (mcg)")
    private double karotenTotal;
    @JsonProperty("Thiamin (vit. B1) (mg)")
    private double thiamin;
    @JsonProperty("Riboflavin (vit. B2) (mg)")
    private double riboflavin;
    @JsonProperty("Niasin (mg)")
    private double niacin;
    @JsonProperty("Vitamin C (mg)")
    private double vitaminC;
    @JsonProperty("BDD (%)")
    private double bdd;

    // Getters and setters
}
