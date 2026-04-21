package com.collectivity.service;

import com.collectivity.datasource.CollectivityRepository;
import com.collectivity.datasource.MemberRepository;
import com.collectivity.dto.CreateMemberDto;
import com.collectivity.dto.MemberDto;
import com.collectivity.dto.MemberInformationDto;
import com.collectivity.entity.MemberEntity;
import com.collectivity.exception.BadRequestException;
import com.collectivity.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final CollectivityRepository collectivityRepository;

    public MemberService(MemberRepository memberRepository,
                         CollectivityRepository collectivityRepository) {
        this.memberRepository = memberRepository;
        this.collectivityRepository = collectivityRepository;
    }

    public List<MemberDto> createMembers(List<CreateMemberDto> requests) {
        List<MemberDto> result = new ArrayList<>();
        for (CreateMemberDto request : requests) {
            result.add(createMember(request));
        }
        return result;
    }

    private MemberDto createMember(CreateMemberDto request) {
        // Validate collectivity exists
        if (request.getCollectivityIdentifier() != null
                && !collectivityRepository.existsById(request.getCollectivityIdentifier())) {
            throw new ResourceNotFoundException(
                    "Collectivity not found: " + request.getCollectivityIdentifier());
        }

        // Validate referees exist
        List<String> refereeIds = request.getReferees();
        if (refereeIds != null) {
            for (String refereeId : refereeIds) {
                if (!memberRepository.existsById(refereeId)) {
                    throw new ResourceNotFoundException("Referee member not found: " + refereeId);
                }
            }
        } else {
            throw new BadRequestException("Member must have at least two referees.");
        }

        if (refereeIds.size() < 2) {
            throw new BadRequestException("Member must have at least two referees.");
        }

        // Validate payment
        if (!request.isRegistrationFeePaid()) {
            throw new BadRequestException("Registration fee has not been paid.");
        }
        if (!request.isMembershipDuesPaid()) {
            throw new BadRequestException("Membership dues have not been paid.");
        }

        // Build and persist entity
        MemberEntity entity = toEntity(request);
        entity.setRefereeIds(refereeIds);
        entity.setCollectivityIdentifier(request.getCollectivityIdentifier());
        entity = memberRepository.save(entity);

        return toDto(entity);
    }

    public MemberDto toDto(MemberEntity entity) {
        MemberDto dto = new MemberDto();
        dto.setId(entity.getId());
        copyInfo(entity, dto);

        List<MemberDto> refereeDtos = new ArrayList<>();
        if (entity.getRefereeIds() != null) {
            for (String refereeId : entity.getRefereeIds()) {
                memberRepository.findById(refereeId).ifPresent(ref -> refereeDtos.add(toDto(ref)));
            }
        }
        dto.setReferees(refereeDtos);
        return dto;
    }

    private MemberEntity toEntity(MemberInformationDto dto) {
        MemberEntity entity = new MemberEntity();
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setBirthDate(dto.getBirthDate());
        entity.setGender(dto.getGender());
        entity.setAddress(dto.getAddress());
        entity.setProfession(dto.getProfession());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setEmail(dto.getEmail());
        entity.setOccupation(dto.getOccupation());
        return entity;
    }

    private void copyInfo(MemberEntity entity, MemberInformationDto dto) {
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setBirthDate(entity.getBirthDate());
        dto.setGender(entity.getGender());
        dto.setAddress(entity.getAddress());
        dto.setProfession(entity.getProfession());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setEmail(entity.getEmail());
        dto.setOccupation(entity.getOccupation());
    }
}
