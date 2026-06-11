package com.murat.diabetasistan.recipe;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.murat.diabetasistan.common.BadRequestException;
import com.murat.diabetasistan.family.FamilyService;
import com.murat.diabetasistan.user.UserAccount;
import com.murat.diabetasistan.user.UserAccountService;
import com.murat.diabetasistan.user.UserRole;

@Service
@Transactional(readOnly = true)
public class FamilyRecipeService {

    private final FamilyRecipeRepository familyRecipeRepository;
    private final FamilyService familyService;
    private final UserAccountService userAccountService;

    public FamilyRecipeService(
            FamilyRecipeRepository familyRecipeRepository,
            FamilyService familyService,
            UserAccountService userAccountService
    ) {
        this.familyRecipeRepository = familyRecipeRepository;
        this.familyService = familyService;
        this.userAccountService = userAccountService;
    }

    @Transactional
    public FamilyRecipeResponse createRecipe(CreateFamilyRecipeRequest request) {
        familyService.getFamilyEntity(request.familyId());

        UserAccount parent = userAccountService.getUserEntity(request.createdByParentId());
        validateParent(parent, request.familyId());

        FamilyRecipe familyRecipe = new FamilyRecipe(
                request.familyId(),
                request.name().trim(),
                normalizeOptionalText(request.description()),
                request.totalCarbsGram(),
                request.servings(),
                normalizeOptionalText(request.defaultPortionDescription()),
                request.createdByParentId()
        );

        FamilyRecipe savedRecipe = familyRecipeRepository.save(familyRecipe);
        return FamilyRecipeResponse.from(savedRecipe);
    }

    public List<FamilyRecipeResponse> getRecipesByFamily(Long familyId) {
        familyService.getFamilyEntity(familyId);

        return familyRecipeRepository.findByFamilyIdAndActiveTrueOrderByNameAsc(familyId)
                .stream()
                .map(FamilyRecipeResponse::from)
                .toList();
    }

    private void validateParent(UserAccount parent, Long familyId) {
        if (parent.getRole() != UserRole.PARENT) {
            throw new BadRequestException("Only parent users can create family recipes.");
        }

        if (!parent.getFamilyId().equals(familyId)) {
            throw new BadRequestException("Parent user does not belong to the given family.");
        }

        if (!parent.isActive()) {
            throw new BadRequestException("Parent user is not active.");
        }
    }

    private String normalizeOptionalText(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}
