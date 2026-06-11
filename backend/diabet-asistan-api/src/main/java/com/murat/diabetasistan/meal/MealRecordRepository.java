package com.murat.diabetasistan.meal;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MealRecordRepository extends JpaRepository<MealRecord, Long> {

    List<MealRecord> findByChildIdOrderByMealTimeDesc(Long childId);

    List<MealRecord> findByFamilyIdOrderByMealTimeDesc(Long familyId);

    List<MealRecord> findByFamilyIdAndStatusOrderByMealTimeDesc(Long familyId, MealStatus status);
}
