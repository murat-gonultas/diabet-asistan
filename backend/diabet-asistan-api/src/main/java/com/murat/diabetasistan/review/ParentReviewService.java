package com.murat.diabetasistan.review;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.murat.diabetasistan.common.BadRequestException;
import com.murat.diabetasistan.common.ResourceNotFoundException;
import com.murat.diabetasistan.meal.MealRecord;
import com.murat.diabetasistan.meal.MealRecordRepository;
import com.murat.diabetasistan.meal.MealResponse;
import com.murat.diabetasistan.meal.MealStatus;
import com.murat.diabetasistan.user.UserAccount;
import com.murat.diabetasistan.user.UserAccountService;
import com.murat.diabetasistan.user.UserRole;

@Service
@Transactional(readOnly = true)
public class ParentReviewService {

    private final ParentReviewRepository parentReviewRepository;
    private final MealRecordRepository mealRecordRepository;
    private final UserAccountService userAccountService;

    public ParentReviewService(
            ParentReviewRepository parentReviewRepository,
            MealRecordRepository mealRecordRepository,
            UserAccountService userAccountService
    ) {
        this.parentReviewRepository = parentReviewRepository;
        this.mealRecordRepository = mealRecordRepository;
        this.userAccountService = userAccountService;
    }

    @Transactional
    public ParentReviewResponse reviewMeal(Long mealId, CreateParentReviewRequest request) {
        MealRecord mealRecord = getMealRecord(mealId);
        UserAccount parent = userAccountService.getUserEntity(request.parentId());

        validateMealCanBeReviewed(mealRecord);
        validateParent(parent, mealRecord.getFamilyId());

        BigDecimal correctedCarbsGram = validateAndResolveCorrectedCarbs(request);

        ParentReview parentReview = new ParentReview(
                mealRecord.getId(),
                parent.getId(),
                mealRecord.getEstimatedCarbsGram(),
                correctedCarbsGram,
                normalizeOptionalText(request.comment()),
                request.reviewStatus()
        );

        if (request.reviewStatus() == ReviewStatus.APPROVED) {
            mealRecord.approveByParent(parent.getId());
        } else if (request.reviewStatus() == ReviewStatus.CORRECTED) {
            mealRecord.correctByParent(parent.getId(), correctedCarbsGram);
        } else {
            throw new BadRequestException("REJECTED reviews are reserved for later versions.");
        }

        mealRecordRepository.save(mealRecord);
        ParentReview savedReview = parentReviewRepository.save(parentReview);

        return ParentReviewResponse.from(savedReview, MealResponse.from(mealRecord));
    }

    public List<ParentReviewResponse> getReviewsByMeal(Long mealId) {
        MealRecord mealRecord = getMealRecord(mealId);
        MealResponse mealResponse = MealResponse.from(mealRecord);

        return parentReviewRepository.findByMealRecordIdOrderByCreatedAtDesc(mealId)
                .stream()
                .map(review -> ParentReviewResponse.from(review, mealResponse))
                .toList();
    }

    private MealRecord getMealRecord(Long mealId) {
        return mealRecordRepository.findById(mealId)
                .orElseThrow(() -> new ResourceNotFoundException("Meal record not found with id: " + mealId));
    }

    private void validateMealCanBeReviewed(MealRecord mealRecord) {
        if (mealRecord.getStatus() != MealStatus.PENDING_PARENT_REVIEW) {
            throw new BadRequestException("Only meals pending parent review can be reviewed.");
        }
    }

    private void validateParent(UserAccount parent, Long familyId) {
        if (parent.getRole() != UserRole.PARENT) {
            throw new BadRequestException("Only parent users can review meal records.");
        }

        if (!parent.getFamilyId().equals(familyId)) {
            throw new BadRequestException("Parent user does not belong to the meal family.");
        }

        if (!parent.isActive()) {
            throw new BadRequestException("Parent user is not active.");
        }
    }

    private BigDecimal validateAndResolveCorrectedCarbs(CreateParentReviewRequest request) {
        if (request.reviewStatus() == ReviewStatus.APPROVED) {
            if (request.correctedCarbsGram() != null) {
                throw new BadRequestException("correctedCarbsGram must be empty when approving a meal.");
            }

            return null;
        }

        if (request.reviewStatus() == ReviewStatus.CORRECTED) {
            if (request.correctedCarbsGram() == null) {
                throw new BadRequestException("correctedCarbsGram is required when correcting a meal.");
            }

            return request.correctedCarbsGram();
        }

        if (request.reviewStatus() == ReviewStatus.REJECTED) {
            throw new BadRequestException("REJECTED reviews are reserved for later versions.");
        }

        throw new BadRequestException("Unsupported review status.");
    }

    private String normalizeOptionalText(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}
