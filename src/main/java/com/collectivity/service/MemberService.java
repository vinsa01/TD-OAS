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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
      
        if (request.getCollectivityIdentifier() != null
                && !collectivityRepository.existsById(request.getCollectivityIdentifier())) {
            throw new ResourceNotFoundException(
                    "Collectivity not found: " + request.getCollectivityIdentifier());
        }

        List<String> refereeIds = request.getReferees();
        if (refereeIds == null || refereeIds.size() < 2) {
            throw new BadRequestException("Member must have at least two referees.");
        }

     
        List<MemberEntity> refereesInDb = memberRepository.findAllById(refereeIds);
        if (refereesInDb.size() != refereeIds.size()) {
            throw new ResourceNotFoundException("One or more referees not found in database.");
        }

        long localReferees = refereesInDb.stream()
                .filter(r -> r.getCollectivityIdentifier() != null && 
                        r.getCollectivityIdentifier().equals(request.getCollectivityIdentifier()))
                .count();
        
        long externalReferees = refereesInDb.size() - localReferees;

        if (localReferees < externalReferees) {
            throw new BadRequestException("Rule B-2 violation: Local referees must be >= external referees.");
        }

     
        if (!request.isRegistrationFeePaid() || !request.isMembershipDuesPaid()) {
            throw new BadRequestException("All fees (registration and membership) must be paid.");
        }

      
        MemberEntity entity = toEntity(request);
        entity.setId(UUID.randomUUID().toString());
        entity.setRefereeIds(refereeIds);
        entity.setCollectivityIdentifier(request.getCollectivityIdentifier());
        
        entity.setJoinDate(LocalDate.now()); 

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
                memberRepository.findById(refereeId)
                        .ifPresent(ref -> refereeDtos.add(toSimpleDto(ref)));
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

    private MemberDto toSimpleDto(MemberEntity entity) {
        MemberDto dto = new MemberDto();
        dto.setId(entity.getId());
        copyInfo(entity, dto);
        return dto;
    }
}
