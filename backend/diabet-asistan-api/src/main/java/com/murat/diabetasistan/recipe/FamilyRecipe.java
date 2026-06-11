package com.murat.diabetasistan.recipe;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "family_recipes")
public class FamilyRecipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long familyId;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal totalCarbsGram;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal servings;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal carbsPerServing;

    @Column(length = 150)
    private String defaultPortionDescription;

    @Column(nullable = false)
    private Long createdByParentId;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    protected FamilyRecipe() {
    }

    public FamilyRecipe(
            Long familyId,
            String name,
            String description,
            BigDecimal totalCarbsGram,
            BigDecimal servings,
            String defaultPortionDescription,
            Long createdByParentId
    ) {
        this.familyId = familyId;
        this.name = name;
        this.description = description;
        this.totalCarbsGram = totalCarbsGram;
        this.servings = servings;
        this.carbsPerServing = totalCarbsGram.divide(servings, 2, RoundingMode.HALF_UP);
        this.defaultPortionDescription = defaultPortionDescription;
        this.createdByParentId = createdByParentId;
        this.active = true;
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

    public Long getId() {
        return id;
    }

    public Long getFamilyId() {
        return familyId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getTotalCarbsGram() {
        return totalCarbsGram;
    }

    public BigDecimal getServings() {
        return servings;
    }

    public BigDecimal getCarbsPerServing() {
        return carbsPerServing;
    }

    public String getDefaultPortionDescription() {
        return defaultPortionDescription;
    }

    public Long getCreatedByParentId() {
        return createdByParentId;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
