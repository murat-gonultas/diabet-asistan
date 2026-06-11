package com.murat.diabetasistan.recipe;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FamilyRecipeRepository extends JpaRepository<FamilyRecipe, Long> {

    List<FamilyRecipe> findByFamilyIdAndActiveTrueOrderByNameAsc(Long familyId);
}
