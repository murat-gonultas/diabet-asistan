package com.murat.diabetasistan.review;

import java.math.BigDecimal;
import java.time.Instant;

import com.murat.diabetasistan.meal.MealResponse;

public record ParentReviewResponse(
        Long id,
        Long mealRecordId,
        Long parentId,
        BigDecimal originalCarbsGram,
        BigDecimal correctedCarbsGram,
        String comment,
        ReviewStatus reviewStatus,
        Instant createdAt,
        MealResponse meal
) {
    public static ParentReviewResponse from(ParentReview parentReview, MealResponse meal) {
        return new ParentReviewResponse(
                parentReview.getId(),
                parentReview.getMealRecordId(),
                parentReview.getParentId(),
                parentReview.getOriginalCarbsGram(),
                parentReview.getCorrectedCarbsGram(),
                parentReview.getComment(),
                parentReview.getReviewStatus(),
                parentReview.getCreatedAt(),
                meal
        );
    }
}
