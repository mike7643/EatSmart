package com.boorisoogeo.EatSmart.web.food;

import com.boorisoogeo.EatSmart.domain.Diet;
import com.boorisoogeo.EatSmart.domain.Food;
import com.boorisoogeo.EatSmart.domain.Member;
import com.boorisoogeo.EatSmart.repository.FoodRepository;
import com.boorisoogeo.EatSmart.repository.MealRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/meals")
@SessionAttributes("loginMember")
@RequiredArgsConstructor
public class MealController {

    private final MealRepository mealRepository;
    private final FoodRepository foodRepository;

    @PostMapping("/addFood")
    public String addFood(@SessionAttribute(name = "loginMember", required = false) Member loginMember,
                          @RequestParam("foodId") Long foodId,
                          @RequestParam(value = "date", required = false) String date,
                          HttpServletRequest request) {
        log.info("Received request to add food: {}", foodId);
        if (loginMember == null) {
            log.info("로그인 안됨, 로그인 창으로 리 다이렉트");
            return "redirect:/login";  // 로그인되지 않은 경우 로그인 페이지로 리다이렉트
        }

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

        switch (path) {
            case "/add/breakfast":
                diet.addBreakfast(selectedFood.get());
                break;
            case "/add/lunch":
                diet.addLunch(selectedFood.get());
                break;
            case "/add/dinner":
                diet.addDinner(selectedFood.get());
                break;
            default:
                throw new IllegalArgumentException("Unknown meal type: " + beforeAddress);
        }

        mealRepository.saveDiet(diet);
        log.info("음식 식단에 저장됨.");

        return "redirect:" + beforeAddress;
    }

    @GetMapping("/deleteFood")
    public String deleteFood(@SessionAttribute(name = "loginMember", required = false) Member loginMember,
                             @RequestParam("foodId") Long foodId,
                             @RequestParam("mealType") String mealType,
                             @RequestParam(value = "date", required = false) String date,
                             HttpServletRequest request) {
        log.info("Received request to delete food: {}", foodId);
        if (loginMember == null) {
            log.info("로그인 안됨, 로그인 창으로 리다이렉트");
            return "redirect:/login";  // 로그인되지 않은 경우 로그인 페이지로 리다이렉트
        }

        String beforeAddress = request.getHeader("referer");
        log.debug("Referer address: {}", beforeAddress);

        LocalDate dietDate = date != null ? LocalDate.parse(date) : LocalDate.now();
        Diet diet = mealRepository.getDietByUserIdAndDate(loginMember.getId(), dietDate);

        mealRepository.deleteFoodFromDiet(diet.getId(), foodId, mealType);
        log.info("음식 삭제됨");

        return "redirect:/add";
    }
}
