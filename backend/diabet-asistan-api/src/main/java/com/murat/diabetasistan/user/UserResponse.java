package com.murat.diabetasistan.user;

public record UserResponse(
        Long id,
        Long familyId,
        String displayName,
        UserRole role,
        String email,
        boolean active
) {
    public static UserResponse from(UserAccount userAccount) {
        return new UserResponse(
                userAccount.getId(),
                userAccount.getFamilyId(),
                userAccount.getDisplayName(),
                userAccount.getRole(),
                userAccount.getEmail(),
                userAccount.isActive()
        );
    }
}
