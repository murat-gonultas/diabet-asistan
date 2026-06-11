package com.murat.diabetasistan.family;

public record FamilyResponse(
        Long id,
        String name
) {
    public static FamilyResponse from(Family family) {
        return new FamilyResponse(
                family.getId(),
                family.getName()
        );
    }
}
