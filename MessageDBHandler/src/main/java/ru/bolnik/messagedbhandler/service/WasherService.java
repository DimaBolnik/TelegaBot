package ru.bolnik.messagedbhandler.service;

import org.springframework.stereotype.Service;
import ru.bolnik.messagedbhandler.entity.Washer;
import ru.bolnik.messagedbhandler.repository.WasherRepository;

import java.util.Optional;

@Service
public class WasherService {

    private final WasherRepository washerRepository;

    public WasherService(WasherRepository washerRepository) {
        this.washerRepository = washerRepository;
    }

    public Optional<Washer> findWasher(String gost, String size) {
        return washerRepository.findFirstByGostAndSize(gost, size);
    }

}