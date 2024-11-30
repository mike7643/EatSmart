package com.boorisoogeo.EatSmart.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Meal {
    private List<Food> foods;
    private String description;
}
