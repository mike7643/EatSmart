package com.boorisoogeo.EatSmart.web.food;

import com.boorisoogeo.EatSmart.domain.Diet;
import com.boorisoogeo.EatSmart.domain.Food;
import com.boorisoogeo.EatSmart.domain.Member;
import com.boorisoogeo.EatSmart.repository.FoodRepository;
import com.boorisoogeo.EatSmart.repository.MealRepository;
import com.boorisoogeo.EatSmart.web.Login;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/meals")
@RequiredArgsConstructor
public class MealController {

    private final MealRepository mealRepository;
    private final FoodRepository foodRepository;

    @PostMapping("/addFood")
    public String addFood(@Login Member loginMember,
                          @RequestParam("foodId") Long foodId,
                          @RequestParam(value = "date", required = false) String date,
                          HttpServletRequest request, RedirectAttributes redirectAttributes) {
        log.info("Received request to add food: {}", foodId);

        String beforeAddress = request.getHeader("referer");
        String path;
        try {
            path = new URI(beforeAddress).getPath();
            log.info("Parsed path: {}", path);
        } catch (URISyntaxException e) {
            log.error("Error parsing URI", e);
            throw new RuntimeException(e);
        }

        Optional<Food> selectedFood = foodRepository.findById(foodId);
        if (selectedFood.isEmpty()) {
            log.info("음식 식단에 저장 안됨!");
            return "redirect:" + beforeAddress;
        }

        LocalDate dietDate = date != null ? LocalDate.parse(date) : LocalDate.now();
        Diet diet = mealRepository.getDietByUserIdAndDate(loginMember.getId(), dietDate);

        // 아침, 점심, 저녁별로 중복 체크
        boolean isAlreadyExists;
        switch (path) {
            case "/add/breakfast":
                isAlreadyExists = diet.getBreakfast().contains(selectedFood.get());
                if (!isAlreadyExists) {
                    diet.addBreakfast(selectedFood.get());
                }
                break;
            case "/add/lunch":
                isAlreadyExists = diet.getLunch().contains(selectedFood.get());
                if (!isAlreadyExists) {
                    diet.addLunch(selectedFood.get());
                }
                break;
            case "/add/dinner":
                isAlreadyExists = diet.getDinner().contains(selectedFood.get());
                if (!isAlreadyExists) {
                    diet.addDinner(selectedFood.get());
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown meal type: " + beforeAddress);
        }

        if (isAlreadyExists) {
            log.info("이미 {}에 등록된 음식", path);
            redirectAttributes.addFlashAttribute("already", "이미 선택된 음식입니다!");
            return "redirect:" + beforeAddress;
        }


        mealRepository.saveDiet(diet);
        log.info("음식 식단에 저장됨.");

        return "redirect:" + beforeAddress;
    }

    @GetMapping("/deleteFood")
    public String deleteFood(@Login Member loginMember,
                             @RequestParam("foodId") Long foodId,
                             @RequestParam("mealType") String mealType,
                             @RequestParam(value = "date", required = false) String date,
                             HttpServletRequest request) {
        log.info("Received request to delete food: {}", foodId);

        String beforeAddress = request.getHeader("referer");
        log.debug("Referer address: {}", beforeAddress);

        LocalDate dietDate = date != null ? LocalDate.parse(date) : LocalDate.now();
        Diet diet = mealRepository.getDietByUserIdAndDate(loginMember.getId(), dietDate);

        mealRepository.deleteFoodFromDiet(diet.getId(), foodId, mealType);
        log.info("음식 삭제됨");

        return "redirect:/add";
    }
}
