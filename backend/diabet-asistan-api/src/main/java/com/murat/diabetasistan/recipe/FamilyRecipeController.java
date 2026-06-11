package com.murat.diabetasistan.recipe;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/family-recipes")
public class FamilyRecipeController {

    private final FamilyRecipeService familyRecipeService;

    public FamilyRecipeController(FamilyRecipeService familyRecipeService) {
        this.familyRecipeService = familyRecipeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FamilyRecipeResponse createRecipe(@Valid @RequestBody CreateFamilyRecipeRequest request) {
        return familyRecipeService.createRecipe(request);
    }

    @GetMapping("/family/{familyId}")
    public List<FamilyRecipeResponse> getRecipesByFamily(@PathVariable Long familyId) {
        return familyRecipeService.getRecipesByFamily(familyId);
    }
}
