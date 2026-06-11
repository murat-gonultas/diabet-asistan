package com.murat.diabetasistan.family;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateFamilyRequest(
        @NotBlank(message = "family name must not be blank")
        @Size(max = 100, message = "family name must not exceed 100 characters")
        String name
) {
}
