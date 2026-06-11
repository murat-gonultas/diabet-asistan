package com.murat.diabetasistan.meal;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.murat.diabetasistan.common.BadRequestException;
import com.murat.diabetasistan.common.ResourceNotFoundException;
import com.murat.diabetasistan.family.FamilyService;
import com.murat.diabetasistan.user.UserAccount;
import com.murat.diabetasistan.user.UserAccountService;
import com.murat.diabetasistan.user.UserRole;

@Service
@Transactional(readOnly = true)
public class MealService {

    private final MealRecordRepository mealRecordRepository;
    private final FamilyService familyService;
    private final UserAccountService userAccountService;

    public MealService(
            MealRecordRepository mealRecordRepository,
            FamilyService familyService,
            UserAccountService userAccountService
    ) {
        this.mealRecordRepository = mealRecordRepository;
        this.familyService = familyService;
        this.userAccountService = userAccountService;
    }

    @Transactional
    public MealResponse createMeal(CreateMealRequest request) {
        familyService.getFamilyEntity(request.familyId());

        UserAccount child = userAccountService.getUserEntity(request.childId());
        validateChildUser(child, request.familyId());

        UserAccount creator = userAccountService.getUserEntity(request.createdByUserId());
        validateSameFamily(creator, request.familyId(), "creator");

        MealRecord mealRecord = new MealRecord(
                request.familyId(),
                request.childId(),
                request.mealTime(),
                request.mealType(),
                request.foodName().trim(),
                normalizeOptionalText(request.description()),
                normalizeOptionalText(request.photoPath()),
                request.estimatedCarbsGram(),
                request.createdByUserId()
        );

        MealRecord savedMealRecord = mealRecordRepository.save(mealRecord);
        return MealResponse.from(savedMealRecord);
    }

    public MealResponse getMeal(Long mealId) {
        return MealResponse.from(getMealEntity(mealId));
    }

    public List<MealResponse> getMealsByChild(Long childId) {
        UserAccount child = userAccountService.getUserEntity(childId);

        if (child.getRole() != UserRole.CHILD) {
            throw new BadRequestException("User is not a child user: " + childId);
        }

        return mealRecordRepository.findByChildIdOrderByMealTimeDesc(childId)
                .stream()
                .map(MealResponse::from)
                .toList();
    }

    public List<MealResponse> getMealsByFamily(Long familyId) {
        familyService.getFamilyEntity(familyId);

        return mealRecordRepository.findByFamilyIdOrderByMealTimeDesc(familyId)
                .stream()
                .map(MealResponse::from)
                .toList();
    }

    public List<MealResponse> getPendingReviewsByFamily(Long familyId) {
        familyService.getFamilyEntity(familyId);

        return mealRecordRepository.findByFamilyIdAndStatusOrderByMealTimeDesc(
                        familyId,
                        MealStatus.PENDING_PARENT_REVIEW
                )
                .stream()
                .map(MealResponse::from)
                .toList();
    }

    private MealRecord getMealEntity(Long mealId) {
        return mealRecordRepository.findById(mealId)
                .orElseThrow(() -> new ResourceNotFoundException("Meal record not found with id: " + mealId));
    }

    private void validateChildUser(UserAccount child, Long familyId) {
        validateSameFamily(child, familyId, "child");

        if (child.getRole() != UserRole.CHILD) {
            throw new BadRequestException("Meal records must be assigned to a child user.");
        }

        if (!child.isActive()) {
            throw new BadRequestException("Child user is not active.");
        }
    }

    private void validateSameFamily(UserAccount user, Long familyId, String userLabel) {
        if (!user.getFamilyId().equals(familyId)) {
            throw new BadRequestException("The " + userLabel + " user does not belong to the given family.");
        }

        if (!user.isActive()) {
            throw new BadRequestException("The " + userLabel + " user is not active.");
        }
    }

    private String normalizeOptionalText(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}
