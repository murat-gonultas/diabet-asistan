package com.murat.diabetasistan.recipe;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateFamilyRecipeRequest(
        @NotNull(message = "familyId is required")
        Long familyId,

        @NotBlank(message = "recipe name must not be blank")
        @Size(max = 150, message = "recipe name must not exceed 150 characters")
        String name,

        @Size(max = 1000, message = "description must not exceed 1000 characters")
        String description,

        @NotNull(message = "totalCarbsGram is required")
        @DecimalMin(value = "0.0", inclusive = true, message = "totalCarbsGram must be greater than or equal to 0")
        @DecimalMax(value = "2000.0", inclusive = true, message = "totalCarbsGram must not exceed 2000 in MVP")
        BigDecimal totalCarbsGram,

        @NotNull(message = "servings is required")
        @DecimalMin(value = "0.1", inclusive = true, message = "servings must be greater than 0")
        @DecimalMax(value = "100.0", inclusive = true, message = "servings must not exceed 100 in MVP")
        BigDecimal servings,

        @Size(max = 150, message = "defaultPortionDescription must not exceed 150 characters")
        String defaultPortionDescription,

        @NotNull(message = "createdByParentId is required")
        Long createdByParentId
) {
}
