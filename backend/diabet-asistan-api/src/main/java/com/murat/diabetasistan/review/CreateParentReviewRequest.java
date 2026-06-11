package com.murat.diabetasistan.review;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateParentReviewRequest(
        @NotNull(message = "parentId is required")
        Long parentId,

        @NotNull(message = "reviewStatus is required")
        ReviewStatus reviewStatus,

        @DecimalMin(value = "0.0", inclusive = true, message = "correctedCarbsGram must be greater than or equal to 0")
        @DecimalMax(value = "300.0", inclusive = true, message = "correctedCarbsGram must not exceed 300 in MVP")
        BigDecimal correctedCarbsGram,

        @Size(max = 1000, message = "comment must not exceed 1000 characters")
        String comment
) {
}
