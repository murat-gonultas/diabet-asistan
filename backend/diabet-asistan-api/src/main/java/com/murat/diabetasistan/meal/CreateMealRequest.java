package com.murat.diabetasistan.meal;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateMealRequest(
        @NotNull(message = "familyId is required")
        Long familyId,

        @NotNull(message = "childId is required")
        Long childId,

        @NotNull(message = "mealTime is required")
        Instant mealTime,

        @NotNull(message = "mealType is required")
        MealType mealType,

        @NotBlank(message = "foodName must not be blank")
        @Size(max = 150, message = "foodName must not exceed 150 characters")
        String foodName,

        @Size(max = 1000, message = "description must not exceed 1000 characters")
        String description,

        @Size(max = 500, message = "photoPath must not exceed 500 characters")
        String photoPath,

        @NotNull(message = "estimatedCarbsGram is required")
        @DecimalMin(value = "0.0", inclusive = true, message = "estimatedCarbsGram must be greater than or equal to 0")
        @DecimalMax(value = "300.0", inclusive = true, message = "estimatedCarbsGram must not exceed 300 in MVP")
        BigDecimal estimatedCarbsGram,

        @NotNull(message = "createdByUserId is required")
        Long createdByUserId
) {
}
