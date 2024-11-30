package com.boorisoogeo.EatSmart.web.food;

import com.boorisoogeo.EatSmart.domain.Food;
import com.boorisoogeo.EatSmart.repository.FoodUpdateDto;
import com.boorisoogeo.EatSmart.service.FoodService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.naming.Binding;
import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@Controller
@RequestMapping("foods")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;



    @GetMapping("/{foodId}")
    public String food(@PathVariable long foodId, Model model) {
        Food food = foodService.findById(foodId).get();
        model.addAttribute("food", food);
        return "/diet/food";
    }


    @GetMapping("/add/food")
    public String addForm(Food food) {
        return "diet/foodAdd";
    }


    @PostMapping("/add/food")
    public String addFood(@Validated @ModelAttribute ("food") FoodUpdateDto food, BindingResult result,RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "diet/foodAdd";
        }
        Food savedFood = foodService.save(food);
        log.info("savedFood={}",savedFood);
        redirectAttributes.addAttribute("foodId", savedFood.getFoodId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/foods/{foodId}";
    }

    @GetMapping("/{foodId}/edit")
    public String editForm(@PathVariable Long foodId, Model model) {
        Food food = foodService.findById(foodId).get();
        model.addAttribute("food", food);
        return "diet/foodEdit";
    }

    @PostMapping("/{foodId}/edit")
    public String edit(@PathVariable Long foodId, @ModelAttribute FoodUpdateDto updateParam, @Validated BindingResult result) {
        if (result.hasErrors()) {
            return "/diet/foodEdit";
        }
        foodService.update(foodId, updateParam);
        return "redirect:/foods/{foodId}";
    }

    @PostMapping("/{foodId}/delete")
    public String delete(@PathVariable Long foodId, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String beforeAddress = request.getHeader("referer");
        String path;
        try {
            path = new URI(beforeAddress).getPath();
        } catch (URISyntaxException e) {
            log.error("Error parsing URI", e);
            throw new RuntimeException(e);
        }
        foodService.delete(foodId);
        log.info("foodId={} 삭제됨",foodId);
        redirectAttributes.addAttribute("status", true);
        return "redirect:"+ path;
    }
}

