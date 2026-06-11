package com.murat.diabetasistan.meal;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/meals")
public class MealController {

    private final MealService mealService;

    public MealController(MealService mealService) {
        this.mealService = mealService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MealResponse createMeal(@Valid @RequestBody CreateMealRequest request) {
        return mealService.createMeal(request);
    }

    @GetMapping("/{mealId}")
    public MealResponse getMeal(@PathVariable Long mealId) {
        return mealService.getMeal(mealId);
    }

    @GetMapping("/child/{childId}")
    public List<MealResponse> getMealsByChild(@PathVariable Long childId) {
        return mealService.getMealsByChild(childId);
    }

    @GetMapping("/family/{familyId}")
    public List<MealResponse> getMealsByFamily(@PathVariable Long familyId) {
        return mealService.getMealsByFamily(familyId);
    }

    @GetMapping("/family/{familyId}/pending-review")
    public List<MealResponse> getPendingReviewsByFamily(@PathVariable Long familyId) {
        return mealService.getPendingReviewsByFamily(familyId);
    }
}
