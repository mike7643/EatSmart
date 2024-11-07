package com.boorisoogeo.EatSmart.service;

import com.boorisoogeo.EatSmart.domain.Food;
import com.boorisoogeo.EatSmart.repository.FoodRepository;
import com.boorisoogeo.EatSmart.repository.FoodSearchCond;
import com.boorisoogeo.EatSmart.repository.FoodUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;

    public Food save(Food food) {
        return foodRepository.save(food);
    }

    public void update(Long itemId, FoodUpdateDto updateParam) {
        foodRepository.update(itemId, updateParam);
    }

    public Optional<Food> findById(Long id) {
        return foodRepository.findById(id);
    }

    public List<Food> findFoods(FoodSearchCond cond) {
        return foodRepository.findAll(cond);
    }

    public void delete(Long foodId) {
        foodRepository.delete(foodId);
    }
}
