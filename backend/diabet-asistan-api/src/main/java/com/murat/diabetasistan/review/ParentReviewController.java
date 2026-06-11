package com.murat.diabetasistan.review;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
public class ParentReviewController {

    private final ParentReviewService parentReviewService;

    public ParentReviewController(ParentReviewService parentReviewService) {
        this.parentReviewService = parentReviewService;
    }

    @PostMapping("/api/meals/{mealId}/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    public ParentReviewResponse reviewMeal(
            @PathVariable Long mealId,
            @Valid @RequestBody CreateParentReviewRequest request
    ) {
        return parentReviewService.reviewMeal(mealId, request);
    }

    @GetMapping("/api/meals/{mealId}/reviews")
    public List<ParentReviewResponse> getReviewsByMeal(@PathVariable Long mealId) {
        return parentReviewService.getReviewsByMeal(mealId);
    }
}
