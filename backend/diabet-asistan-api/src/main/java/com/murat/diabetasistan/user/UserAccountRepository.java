package com.murat.diabetasistan.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    List<UserAccount> findByFamilyIdOrderByIdAsc(Long familyId);
}
