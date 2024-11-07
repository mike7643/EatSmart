package com.boorisoogeo.EatSmart.domain;

import lombok.Data;

@Data
public class DailySummary {
    private String date;
    private String dayOfWeek;
    private double carbs;
    private double protein;
    private double fat;
    private double calories;
}
