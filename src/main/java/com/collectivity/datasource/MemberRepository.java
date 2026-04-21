package com.collectivity.datasource;

import com.collectivity.entity.MemberEntity;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MemberRepository {

    private final Map<String, MemberEntity> store = new HashMap<>();

    public MemberEntity save(MemberEntity member) {
        if (member.getId() == null) {
            member.setId(UUID.randomUUID().toString());
        }
        store.put(member.getId(), member);
        return member;
    }

    public Optional<MemberEntity> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    public List<MemberEntity> findAllById(List<String> ids) {
        List<MemberEntity> result = new ArrayList<>();
        for (String id : ids) {
            MemberEntity entity = store.get(id);
            if (entity != null) result.add(entity);
        }
        return result;
    }

    public boolean existsById(String id) {
        return store.containsKey(id);
    }

    public List<MemberEntity> findAll() {
        return new ArrayList<>(store.values());
    }
}
