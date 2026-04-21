package com.collectivity.datasource;

import com.collectivity.entity.CollectivityEntity;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class CollectivityRepository {

    private final Map<String, CollectivityEntity> store = new HashMap<>();

    public CollectivityEntity save(CollectivityEntity collectivity) {
        if (collectivity.getId() == null) {
            collectivity.setId(UUID.randomUUID().toString());
        }
        store.put(collectivity.getId(), collectivity);
        return collectivity;
    }

    public Optional<CollectivityEntity> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    public boolean existsById(String id) {
        return store.containsKey(id);
    }

    public List<CollectivityEntity> findAll() {
        return new ArrayList<>(store.values());
    }
}
