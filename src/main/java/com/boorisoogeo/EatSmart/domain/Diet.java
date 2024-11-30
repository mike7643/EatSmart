package com.boorisoogeo.EatSmart.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class Diet {
    private Long userId;
    private Long id;
    private LocalDateTime dateTime;

    private List<Food> breakfast = new ArrayList<>();
    private List<Food> lunch = new ArrayList<>();
    private List<Food> dinner = new ArrayList<>();


    public void addBreakfast(Food food) {
        this.breakfast.add(food);
    }

    public void addLunch(Food food) {
        this.lunch.add(food);
    }

    public void addDinner(Food food) {
        this.dinner.add(food);
    }

    public boolean isExistAlready(Food food) {

        return breakfast.contains(food);
    }
}
