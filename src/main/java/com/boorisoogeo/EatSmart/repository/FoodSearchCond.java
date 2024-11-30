package com.boorisoogeo.EatSmart.repository;

import lombok.Data;

@Data
public class FoodSearchCond {

    private String foodName;


    public FoodSearchCond(String foodName) {
        this.foodName = foodName;
    }
}
