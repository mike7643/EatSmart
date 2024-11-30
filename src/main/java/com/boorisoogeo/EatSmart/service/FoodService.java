package com.boorisoogeo.EatSmart.service;

import com.boorisoogeo.EatSmart.domain.Food;
import com.boorisoogeo.EatSmart.repository.FoodRepository;
import com.boorisoogeo.EatSmart.repository.FoodSearchCond;
import com.boorisoogeo.EatSmart.repository.FoodUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FoodService {

    private final FoodRepository foodRepository;

    @Transactional
    public Food save(FoodUpdateDto foodUpdateDto) {
        return foodRepository.save(foodUpdateDto);
    }

    @Transactional
    public void update(Long itemId, FoodUpdateDto updateParam) {
        foodRepository.update(itemId, updateParam);
    }

    public Optional<Food> findById(Long id) {
        return foodRepository.findById(id);
    }

    public List<Food> findFoods(FoodSearchCond cond, int page, int size) {
        return foodRepository.findAllWithPaging(cond,page,size);
    }

    @Transactional
    public void delete(Long foodId) {
        foodRepository.delete(foodId);
    }
}
