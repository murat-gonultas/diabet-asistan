package com.murat.diabetasistan.recipe;

import java.math.BigDecimal;

public record FamilyRecipeResponse(
        Long id,
        Long familyId,
        String name,
        String description,
        BigDecimal totalCarbsGram,
        BigDecimal servings,
        BigDecimal carbsPerServing,
        String defaultPortionDescription,
        Long createdByParentId,
        boolean active
) {
    public static FamilyRecipeResponse from(FamilyRecipe familyRecipe) {
        return new FamilyRecipeResponse(
                familyRecipe.getId(),
                familyRecipe.getFamilyId(),
                familyRecipe.getName(),
                familyRecipe.getDescription(),
                familyRecipe.getTotalCarbsGram(),
                familyRecipe.getServings(),
                familyRecipe.getCarbsPerServing(),
                familyRecipe.getDefaultPortionDescription(),
                familyRecipe.getCreatedByParentId(),
                familyRecipe.isActive()
        );
    }
}
