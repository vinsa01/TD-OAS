package com.collectivity.service;

import com.collectivity.datasource.CollectivityRepository;
import com.collectivity.datasource.MemberRepository;
import com.collectivity.dto.*;
import com.collectivity.entity.CollectivityEntity;
import com.collectivity.entity.MemberEntity;
import com.collectivity.exception.BadRequestException;
import com.collectivity.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CollectivityService {

    private final CollectivityRepository collectivityRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    public CollectivityService(CollectivityRepository collectivityRepository,
                               MemberRepository memberRepository,
                               MemberService memberService) {
        this.collectivityRepository = collectivityRepository;
        this.memberRepository = memberRepository;
        this.memberService = memberService;
    }

    public List<CollectivityDto> createCollectivities(List<CreateCollectivityDto> requests) {
        return requests.stream()
                .map(this::createCollectivity)
                .collect(Collectors.toList());
    }

    private CollectivityDto createCollectivity(CreateCollectivityDto request) {
        // 1. Check Federation Approval
        if (!request.isFederationApproval()) {
            throw new BadRequestException("Collectivity must have federation approval.");
        }

        // 2. Rule A: Total members count check (Min 10)
        List<String> memberIds = request.getMembers() != null ? request.getMembers() : new ArrayList<>();
        if (memberIds.size() < 10) {
            throw new BadRequestException("Rule A Violation: A collectivity must have at least 10 members.");
        }

        // 3. Rule A: Seniority check (At least 5 members with 6 months)
        List<MemberEntity> membersInDb = memberRepository.findAllById(memberIds);
        LocalDate sixMonthsAgo = LocalDate.now().minusMonths(6);
        long seniorMembersCount = membersInDb.stream()
                .filter(m -> m.getJoinDate() != null && m.getJoinDate().isBefore(sixMonthsAgo))
                .count();

        if (seniorMembersCount < 5) {
            throw new BadRequestException("Rule A Violation: At least 5 members must have 6 months of seniority.");
        }

        // 4. Structure validation
        CreateCollectivityStructureDto structure = request.getStructure();
        if (structure == null || isStructureIncomplete(structure)) {
            throw new BadRequestException("Collectivity structure is missing or incomplete.");
        }

        // Validate that all structure members exist
        resolveOrThrow(structure.getPresident(), "President");
        resolveOrThrow(structure.getVicePresident(), "Vice-president");
        resolveOrThrow(structure.getTreasurer(), "Treasurer");
        resolveOrThrow(structure.getSecretary(), "Secretary");

        // 5. Persist Entity
        CollectivityEntity entity = new CollectivityEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setLocation(request.getLocation());
        entity.setFederationApproval(true);
        entity.setMemberIds(memberIds);
        entity.setPresidentId(structure.getPresident());
        entity.setVicePresidentId(structure.getVicePresident());
        entity.setTreasurerId(structure.getTreasurer());
        entity.setSecretaryId(structure.getSecretary());

        return toDto(collectivityRepository.save(entity));
    }

    // --- FUNCTIONALITY J : Assign unique name and number ---
    public CollectivityDto assignIdentification(String id, CollectivityIdentificationDto identification) {
        CollectivityEntity entity = collectivityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Collectivity not found with id: " + id));

        // CRITICAL RULE J: Immutable once assigned
        if (entity.getUniqueName() != null || entity.getUniqueNumber() != null) {
            throw new BadRequestException("Identification is already assigned and cannot be modified.");
        }

        entity.setUniqueName(identification.getUniqueName());
        entity.setUniqueNumber(identification.getUniqueNumber());

        return toDto(collectivityRepository.save(entity));
    }

    private boolean isStructureIncomplete(CreateCollectivityStructureDto s) {
        return s.getPresident() == null || s.getVicePresident() == null
                || s.getTreasurer() == null || s.getSecretary() == null;
    }

    private MemberEntity resolveOrThrow(String memberId, String role) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException(role + " member not found: " + memberId));
    }

    public CollectivityDto toDto(CollectivityEntity entity) {
        CollectivityDto dto = new CollectivityDto();
        dto.setId(entity.getId());
        dto.setLocation(entity.getLocation());

        // Map unique fields (Functionality J)
        dto.setUniqueName(entity.getUniqueName());
        dto.setUniqueNumber(entity.getUniqueNumber());

        // Map Structure
        CollectivityStructureDto structureDto = new CollectivityStructureDto();
        structureDto.setPresident(memberService.toDto(resolveOrThrow(entity.getPresidentId(), "President")));
        structureDto.setVicePresident(memberService.toDto(resolveOrThrow(entity.getVicePresidentId(), "Vice-president")));
        structureDto.setTreasurer(memberService.toDto(resolveOrThrow(entity.getTreasurerId(), "Treasurer")));
        structureDto.setSecretary(memberService.toDto(resolveOrThrow(entity.getSecretaryId(), "Secretary")));
        dto.setStructure(structureDto);

        // Map Members list
        List<MemberDto> memberDtos = entity.getMemberIds().stream()
                .map(mId -> memberRepository.findById(mId).orElse(null))
                .filter(m -> m != null)
                .map(memberService::toDto)
                .collect(Collectors.toList());
        dto.setMembers(memberDtos);

        return dto;
    }
}