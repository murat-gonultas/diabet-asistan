package com.murat.diabetasistan.user;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.murat.diabetasistan.common.ResourceNotFoundException;
import com.murat.diabetasistan.family.FamilyService;

@Service
@Transactional(readOnly = true)
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final FamilyService familyService;

    public UserAccountService(UserAccountRepository userAccountRepository, FamilyService familyService) {
        this.userAccountRepository = userAccountRepository;
        this.familyService = familyService;
    }

    @Transactional
    public UserResponse createUser(CreateUserRequest request) {
        familyService.getFamilyEntity(request.familyId());

        String email = request.email() == null || request.email().isBlank()
                ? null
                : request.email().trim();

        UserAccount userAccount = new UserAccount(
                request.familyId(),
                request.displayName().trim(),
                request.role(),
                email
        );

        UserAccount savedUser = userAccountRepository.save(userAccount);
        return UserResponse.from(savedUser);
    }

    public UserAccount getUserEntity(Long userId) {
        return userAccountRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    public UserResponse getUser(Long userId) {
        return UserResponse.from(getUserEntity(userId));
    }

    public List<UserResponse> getUsersByFamily(Long familyId) {
        familyService.getFamilyEntity(familyId);

        return userAccountRepository.findByFamilyIdOrderByIdAsc(familyId)
                .stream()
                .map(UserResponse::from)
                .toList();
    }
}
