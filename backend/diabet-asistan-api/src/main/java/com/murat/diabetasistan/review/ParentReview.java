package com.murat.diabetasistan.review;

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
import jakarta.persistence.Table;

@Entity
@Table(name = "parent_reviews")
public class ParentReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long mealRecordId;

    @Column(nullable = false)
    private Long parentId;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal originalCarbsGram;

    @Column(precision = 8, scale = 2)
    private BigDecimal correctedCarbsGram;

    @Column(length = 1000)
    private String comment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ReviewStatus reviewStatus;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    protected ParentReview() {
    }

    public ParentReview(
            Long mealRecordId,
            Long parentId,
            BigDecimal originalCarbsGram,
            BigDecimal correctedCarbsGram,
            String comment,
            ReviewStatus reviewStatus
    ) {
        this.mealRecordId = mealRecordId;
        this.parentId = parentId;
        this.originalCarbsGram = originalCarbsGram;
        this.correctedCarbsGram = correctedCarbsGram;
        this.comment = comment;
        this.reviewStatus = reviewStatus;
    }

    @PrePersist
    void onCreate() {
        createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public Long getMealRecordId() {
        return mealRecordId;
    }

    public Long getParentId() {
        return parentId;
    }

    public BigDecimal getOriginalCarbsGram() {
        return originalCarbsGram;
    }

    public BigDecimal getCorrectedCarbsGram() {
        return correctedCarbsGram;
    }

    public String getComment() {
        return comment;
    }

    public ReviewStatus getReviewStatus() {
        return reviewStatus;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
