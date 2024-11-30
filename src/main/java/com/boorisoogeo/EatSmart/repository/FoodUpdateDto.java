package com.boorisoogeo.EatSmart.repository;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class FoodUpdateDto {


    @NotEmpty(message="필수입니다.")
    private String foodName;

    @Positive
    @NotNull(message="필수입니다.")
    private Double foodKcal;

    @Positive @NotNull(message="필수입니다.")
    private Double foodProtein;

    @Positive @NotNull(message="필수입니다.")
    private Double foodFat;

    @Positive @NotNull(message="필수입니다.")
    private Double foodCarbo;

    private Double foodSugar;
    private Double foodNat;
    private Double foodColest;
}
