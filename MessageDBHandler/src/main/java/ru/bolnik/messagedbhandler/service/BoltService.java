package ru.bolnik.messagedbhandler.service;

import org.springframework.stereotype.Service;
import ru.bolnik.messagedbhandler.repository.BoltRepository;

@Service
public class BoltService {

    private final BoltRepository boltRepository;

    public BoltService(BoltRepository boltRepository) {
        this.boltRepository = boltRepository;
    }

}