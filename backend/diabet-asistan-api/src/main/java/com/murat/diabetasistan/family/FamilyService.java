package com.murat.diabetasistan.family;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.murat.diabetasistan.common.ResourceNotFoundException;

@Service
@Transactional(readOnly = true)
public class FamilyService {

    private final FamilyRepository familyRepository;

    public FamilyService(FamilyRepository familyRepository) {
        this.familyRepository = familyRepository;
    }

    @Transactional
    public FamilyResponse createFamily(CreateFamilyRequest request) {
        Family family = new Family(request.name().trim());
        Family savedFamily = familyRepository.save(family);
        return FamilyResponse.from(savedFamily);
    }

    public Family getFamilyEntity(Long familyId) {
        return familyRepository.findById(familyId)
                .orElseThrow(() -> new ResourceNotFoundException("Family not found with id: " + familyId));
    }

    public FamilyResponse getFamily(Long familyId) {
        return FamilyResponse.from(getFamilyEntity(familyId));
    }
}
