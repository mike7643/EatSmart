package com.boorisoogeo.EatSmart.web.food;

import com.boorisoogeo.EatSmart.domain.DailySummary;
import com.boorisoogeo.EatSmart.domain.Diet;
import com.boorisoogeo.EatSmart.domain.Food;
import com.boorisoogeo.EatSmart.domain.Member;
import com.boorisoogeo.EatSmart.repository.MealRepository;
import com.boorisoogeo.EatSmart.web.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/week")
public class WeekController {

    private final MealRepository mealRepository;

    @GetMapping
    public String getWeeklyDiet(@Login Member loginMember,
                                @RequestParam(name = "nutrient", required = false, defaultValue = "calories") String selectedNutrient,
                                Model model) {
        LocalDate now = LocalDate.now();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        LocalDate startOfWeek = now.with(weekFields.dayOfWeek(), 1);
        LocalDate endOfWeek = now.with(weekFields.dayOfWeek(), 7);

        List<Diet> weeklyDiet = mealRepository.getDietByUserIdAndDateRange(loginMember.getId(), startOfWeek, endOfWeek);

        List<DailySummary> weeklySummary = new ArrayList<>();
        double[] nutrientData = new double[7];

        for (LocalDate date = startOfWeek; !date.isAfter(endOfWeek); date = date.plusDays(1)) {
            DailySummary summary = new DailySummary();
            summary.setDate(date.toString());
            summary.setDayOfWeek(getDayOfWeekKorean(date.getDayOfWeek()));

            double totalCarbs = 0;
            double totalProtein = 0;
            double totalFat = 0;
            double totalCalories = 0;

            for (Diet diet : weeklyDiet) {
                if (diet.getDateTime().toLocalDate().equals(date)) {
                    totalCarbs += diet.getBreakfast().stream().mapToDouble(Food::getFoodCarbo).sum();
                    totalProtein += diet.getBreakfast().stream().mapToDouble(Food::getFoodProtein).sum();
                    totalFat += diet.getBreakfast().stream().mapToDouble(Food::getFoodFat).sum();
                    totalCalories += diet.getBreakfast().stream().mapToDouble(Food::getFoodKcal).sum();

                    totalCarbs += diet.getLunch().stream().mapToDouble(Food::getFoodCarbo).sum();
                    totalProtein += diet.getLunch().stream().mapToDouble(Food::getFoodProtein).sum();
                    totalFat += diet.getLunch().stream().mapToDouble(Food::getFoodFat).sum();
                    totalCalories += diet.getLunch().stream().mapToDouble(Food::getFoodKcal).sum();

                    totalCarbs += diet.getDinner().stream().mapToDouble(Food::getFoodCarbo).sum();
                    totalProtein += diet.getDinner().stream().mapToDouble(Food::getFoodProtein).sum();
                    totalFat += diet.getDinner().stream().mapToDouble(Food::getFoodFat).sum();
                    totalCalories += diet.getDinner().stream().mapToDouble(Food::getFoodKcal).sum();
                }
            }

            summary.setCarbs(totalCarbs);
            summary.setProtein(totalProtein);
            summary.setFat(totalFat);
            summary.setCalories(totalCalories);

            weeklySummary.add(summary);

            switch (selectedNutrient) {
                case "carbs":
                    nutrientData[date.getDayOfWeek().getValue() - 1] = totalCarbs;
                    break;
                case "protein":
                    nutrientData[date.getDayOfWeek().getValue() - 1] = totalProtein;
                    break;
                case "fat":
                    nutrientData[date.getDayOfWeek().getValue() - 1] = totalFat;
                    break;
                case "calories":
                    nutrientData[date.getDayOfWeek().getValue() - 1] = totalCalories;
                    break;
            }
        }

        model.addAttribute("weeklySummary", weeklySummary);
        model.addAttribute("nutrientData", nutrientData);
        model.addAttribute("selectedNutrient", selectedNutrient);

        return "diet/weekDiet";
    }

    private static String getDayOfWeekKorean(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY:
                return "월";
            case TUESDAY:
                return "화";
            case WEDNESDAY:
                return "수";
            case THURSDAY:
                return "목";
            case FRIDAY:
                return "금";
            case SATURDAY:
                return "토";
            case SUNDAY:
                return "일";
            default:
                return "";
        }
    }
}
