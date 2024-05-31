package com.delidronehubs.delidronehubs.services;

import com.delidronehubs.delidronehubs.classes.Merchandiser;
import com.delidronehubs.delidronehubs.repositories.MerchandiserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MerchandiserService {
    private final MerchandiserRepository merchandiserRepository;

    @Autowired
    public MerchandiserService(MerchandiserRepository merchandiserRepository) {
        this.merchandiserRepository = merchandiserRepository;
    }

    public List<Merchandiser> getAllMerchandisers() {
        return merchandiserRepository.findAll();
    }

    public Merchandiser getMerchandiserById(UUID id) {
        return merchandiserRepository.findById(id).orElse(null);
    }

    public Merchandiser createMerchandiser(Merchandiser merchandiser) {
        return merchandiserRepository.save(merchandiser);
    }

    public Merchandiser updateMerchandiser(UUID id, Merchandiser merchandiser) {
        if (merchandiserRepository.existsById(id)) {
            merchandiser.setId(id);
            return merchandiserRepository.save(merchandiser);
        }
        return null;
    }

    public void deleteMerchandiser(UUID id) {
        merchandiserRepository.deleteById(id);
    }
}
