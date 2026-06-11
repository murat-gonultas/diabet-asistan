package com.murat.diabetasistan.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @NotNull(message = "familyId is required")
        Long familyId,

        @NotBlank(message = "displayName must not be blank")
        @Size(max = 100, message = "displayName must not exceed 100 characters")
        String displayName,

        @NotNull(message = "role is required")
        UserRole role,

        @Email(message = "email must be valid")
        @Size(max = 254, message = "email must not exceed 254 characters")
        String email
) {
}
