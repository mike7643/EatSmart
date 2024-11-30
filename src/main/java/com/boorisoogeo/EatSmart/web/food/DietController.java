package com.boorisoogeo.EatSmart.web.food;

import com.boorisoogeo.EatSmart.domain.Diet;
import com.boorisoogeo.EatSmart.domain.Food;
import com.boorisoogeo.EatSmart.domain.Member;
import com.boorisoogeo.EatSmart.repository.FoodRepository;
import com.boorisoogeo.EatSmart.repository.FoodSearchCond;
import com.boorisoogeo.EatSmart.repository.FoodUpdateDto;
import com.boorisoogeo.EatSmart.repository.MealRepository;
import com.boorisoogeo.EatSmart.service.FoodService;
import com.boorisoogeo.EatSmart.web.Login;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("add")
@RequiredArgsConstructor
public class DietController {
    private final FoodService foodService;
    private final MealRepository mealRepository;
    private final FoodRepository foodRepository;

    @GetMapping
    public String getMeals(@Login Member loginMember, Model model) {
        if (loginMember != null) {
            LocalDate today = LocalDate.now();
            Diet diet = mealRepository.getDietByUserIdAndDate(loginMember.getId(), today);
            log.info("loginMember={}, diet={}", loginMember, diet);

            double breakfastCalories = diet.getBreakfast().stream().mapToDouble(Food::getFoodKcal).sum();
            double lunchCalories = diet.getLunch().stream().mapToDouble(Food::getFoodKcal).sum();
            double dinnerCalories = diet.getDinner().stream().mapToDouble(Food::getFoodKcal).sum();
            double totalCalories = breakfastCalories + lunchCalories + dinnerCalories;

            model.addAttribute("breakfastFoods", diet.getBreakfast());
            model.addAttribute("lunchFoods", diet.getLunch());
            model.addAttribute("dinnerFoods", diet.getDinner());
            model.addAttribute("breakfastCalories", breakfastCalories);
            model.addAttribute("lunchCalories", lunchCalories);
            model.addAttribute("dinnerCalories", dinnerCalories);
            model.addAttribute("totalCalories", totalCalories);
        }
        return "diet/diets";
    }

    @GetMapping("/{mealType}")
    public String showDietForm(@PathVariable String mealType, @ModelAttribute("foodSearch") FoodSearchCond cond,
                               @RequestParam(defaultValue = "0")int page,
                               @RequestParam(defaultValue = "10")int size,
                               HttpServletRequest request, Model model) {

        List<Food> foods = foodService.findFoods(cond, page, size);
        int totalPages = (int) Math.ceil((double) foodRepository.countTotalFoods(cond)/ size);

        // 페이지 값 검증: 범위를 벗어나면 조정
        if (page < 0) {
            page = 0;  // 최소 페이지 값으로 조정
        } else if (page >= totalPages) {
            page = totalPages - 1;  // 최대 페이지 값으로 조정
        }

        model.addAttribute("foods", foods);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("totalPages", totalPages);
        String requestURI = request.getRequestURI();
        model.addAttribute("currentURI", requestURI);
        model.addAttribute("mealType",mealType);

        switch (mealType) {
            case "breakfast":
            case "lunch":
            case "dinner":
                return "selectDiet";
            default:
                return "redirect:/"; // 잘못된 경로의 경우 홈으로 리다이렉트
        }
    }
}


