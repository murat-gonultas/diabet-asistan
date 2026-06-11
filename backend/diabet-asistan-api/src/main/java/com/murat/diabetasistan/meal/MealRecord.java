package com.murat.diabetasistan.meal;

import java.math.BigDecimal;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "meal_records")
public class MealRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long familyId;

    @Column(nullable = false)
    private Long childId;

    @Column(nullable = false)
    private Instant mealTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private MealType mealType;

    @Column(nullable = false, length = 150)
    private String foodName;

    @Column(length = 1000)
    private String description;

    @Column(length = 500)
    private String photoPath;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal estimatedCarbsGram;

    @Column(precision = 8, scale = 2)
    private BigDecimal finalCarbsGram;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ConfidenceLevel confidenceLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private MealStatus status;

    @Column(nullable = false)
    private Long createdByUserId;

    private Long reviewedByUserId;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    protected MealRecord() {
    }

    public MealRecord(
            Long familyId,
            Long childId,
            Instant mealTime,
            MealType mealType,
            String foodName,
            String description,
            String photoPath,
            BigDecimal estimatedCarbsGram,
            Long createdByUserId
    ) {
        this.familyId = familyId;
        this.childId = childId;
        this.mealTime = mealTime;
        this.mealType = mealType;
        this.foodName = foodName;
        this.description = description;
        this.photoPath = photoPath;
        this.estimatedCarbsGram = estimatedCarbsGram;
        this.createdByUserId = createdByUserId;
        this.confidenceLevel = ConfidenceLevel.MANUAL;
        this.status = MealStatus.PENDING_PARENT_REVIEW;
    }

    @PrePersist
    void onCreate() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = Instant.now();
    }

    public void approveByParent(Long parentId) {
        this.finalCarbsGram = this.estimatedCarbsGram;
        this.reviewedByUserId = parentId;
        this.status = MealStatus.APPROVED;
    }

    public void correctByParent(Long parentId, BigDecimal correctedCarbsGram) {
        this.finalCarbsGram = correctedCarbsGram;
        this.reviewedByUserId = parentId;
        this.status = MealStatus.CORRECTED;
    }

    public Long getId() {
        return id;
    }

    public Long getFamilyId() {
        return familyId;
    }

    public Long getChildId() {
        return childId;
    }

    public Instant getMealTime() {
        return mealTime;
    }

    public MealType getMealType() {
        return mealType;
    }

    public String getFoodName() {
        return foodName;
    }

    public String getDescription() {
        return description;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public BigDecimal getEstimatedCarbsGram() {
        return estimatedCarbsGram;
    }

    public BigDecimal getFinalCarbsGram() {
        return finalCarbsGram;
    }

    public ConfidenceLevel getConfidenceLevel() {
        return confidenceLevel;
    }

    public MealStatus getStatus() {
        return status;
    }

    public Long getCreatedByUserId() {
        return createdByUserId;
    }

    public Long getReviewedByUserId() {
        return reviewedByUserId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
