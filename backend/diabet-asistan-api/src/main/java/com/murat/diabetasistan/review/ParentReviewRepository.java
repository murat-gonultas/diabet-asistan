package com.murat.diabetasistan.review;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ParentReviewRepository extends JpaRepository<ParentReview, Long> {

    List<ParentReview> findByMealRecordIdOrderByCreatedAtDesc(Long mealRecordId);
}
