package com.delidronehubs.delidronehubs.services;

import com.delidronehubs.delidronehubs.classes.Hub;
import com.delidronehubs.delidronehubs.repositories.HubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class HubService {
    private final HubRepository hubRepository;

    @Autowired
    public HubService(HubRepository hubRepository) {
        this.hubRepository = hubRepository;
    }

    public List<Hub> getAllHubs() {
        return hubRepository.findAll();
    }

    public Hub getHubById(UUID id) {
        return hubRepository.findById(id).orElse(null);
    }

    public Hub createHub(Hub hub) {
        return hubRepository.save(hub);
    }

    public Hub updateHub(UUID id, Hub hub) {
        if (hubRepository.existsById(id)) {
            hub.setId(id);
            return hubRepository.save(hub);
        }
        return null;
    }

    public void deleteHub(UUID id) {
        hubRepository.deleteById(id);
    }
}
