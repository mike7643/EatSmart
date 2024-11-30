package com.boorisoogeo.EatSmart.domain;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class Food {

    private Long foodId;

    //필수
    private String foodName;
    private Double foodKcal;
    private Double foodProtein;
    private Double foodFat;
    private Double foodCarbo;

    //선택
    private Double foodSugar;
    private Double foodNat;
    private Double foodColest;
}
