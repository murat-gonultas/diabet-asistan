package com.murat.diabetasistan.meal;

import java.math.BigDecimal;
import java.time.Instant;

public record MealResponse(
        Long id,
        Long familyId,
        Long childId,
        Instant mealTime,
        MealType mealType,
        String foodName,
        String description,
        String photoPath,
        BigDecimal estimatedCarbsGram,
        BigDecimal finalCarbsGram,
        ConfidenceLevel confidenceLevel,
        MealStatus status,
        Long createdByUserId,
        Long reviewedByUserId
) {
    public static MealResponse from(MealRecord mealRecord) {
        return new MealResponse(
                mealRecord.getId(),
                mealRecord.getFamilyId(),
                mealRecord.getChildId(),
                mealRecord.getMealTime(),
                mealRecord.getMealType(),
                mealRecord.getFoodName(),
                mealRecord.getDescription(),
                mealRecord.getPhotoPath(),
                mealRecord.getEstimatedCarbsGram(),
                mealRecord.getFinalCarbsGram(),
                mealRecord.getConfidenceLevel(),
                mealRecord.getStatus(),
                mealRecord.getCreatedByUserId(),
                mealRecord.getReviewedByUserId()
        );
    }
}
