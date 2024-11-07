package com.boorisoogeo.EatSmart.repository;

import lombok.Data;

@Data
public class FoodUpdateDto {
    private String foodName;
    private Double foodKcal;
    private Double foodProtein;
    private Double foodFat;
    private Double foodCarbo;
    private Double foodSugar;
    private Double foodNat;
    private Double foodColest;
}
