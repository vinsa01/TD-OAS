package com.collectivity.service;

import com.collectivity.datasource.CollectivityRepository;
import com.collectivity.datasource.MemberRepository;
import com.collectivity.dto.*;
import com.collectivity.entity.CollectivityEntity;
import com.collectivity.entity.MemberEntity;
import com.collectivity.exception.BadRequestException;
import com.collectivity.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        List<CollectivityDto> result = new ArrayList<>();
        for (CreateCollectivityDto request : requests) {
            result.add(createCollectivity(request));
        }
        return result;
    }

    private CollectivityDto createCollectivity(CreateCollectivityDto request) {
        // Validate federation approval
        if (!request.isFederationApproval()) {
            throw new BadRequestException("Collectivity must have federation approval.");
        }

        // Validate structure presence
        CreateCollectivityStructureDto structure = request.getStructure();
        if (structure == null
                || structure.getPresident() == null
                || structure.getVicePresident() == null
                || structure.getTreasurer() == null
                || structure.getSecretary() == null) {
            throw new BadRequestException("Collectivity structure is missing or incomplete.");
        }

        // Validate that all members exist
        List<String> memberIds = request.getMembers() != null ? request.getMembers() : new ArrayList<>();
        for (String memberId : memberIds) {
            if (!memberRepository.existsById(memberId)) {
                throw new ResourceNotFoundException("Member not found: " + memberId);
            }
        }

        // Validate that structure members exist
        resolveOrThrow(structure.getPresident(), "President");
        resolveOrThrow(structure.getVicePresident(), "Vice-president");
        resolveOrThrow(structure.getTreasurer(), "Treasurer");
        resolveOrThrow(structure.getSecretary(), "Secretary");

        // Persist
        CollectivityEntity entity = new CollectivityEntity();
        entity.setLocation(request.getLocation());
        entity.setFederationApproval(true);
        entity.setMemberIds(memberIds);
        entity.setPresidentId(structure.getPresident());
        entity.setVicePresidentId(structure.getVicePresident());
        entity.setTreasurerId(structure.getTreasurer());
        entity.setSecretaryId(structure.getSecretary());
        entity = collectivityRepository.save(entity);

        return toDto(entity);
    }

    private MemberEntity resolveOrThrow(String memberId, String role) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        role + " member not found: " + memberId));
    }

    private CollectivityDto toDto(CollectivityEntity entity) {
        CollectivityDto dto = new CollectivityDto();
        dto.setId(entity.getId());
        dto.setLocation(entity.getLocation());

        // Build structure
        CollectivityStructureDto structureDto = new CollectivityStructureDto();
        structureDto.setPresident(memberService.toDto(resolveOrThrow(entity.getPresidentId(), "President")));
        structureDto.setVicePresident(memberService.toDto(resolveOrThrow(entity.getVicePresidentId(), "Vice-president")));
        structureDto.setTreasurer(memberService.toDto(resolveOrThrow(entity.getTreasurerId(), "Treasurer")));
        structureDto.setSecretary(memberService.toDto(resolveOrThrow(entity.getSecretaryId(), "Secretary")));
        dto.setStructure(structureDto);

        // Build members
        List<MemberDto> memberDtos = new ArrayList<>();
        for (String memberId : entity.getMemberIds()) {
            memberRepository.findById(memberId)
                    .ifPresent(m -> memberDtos.add(memberService.toDto(m)));
        }
        dto.setMembers(memberDtos);

        return dto;
    }
}
