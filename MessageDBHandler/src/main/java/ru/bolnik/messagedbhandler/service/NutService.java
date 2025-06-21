package ru.bolnik.messagedbhandler.service;

import org.springframework.stereotype.Service;
import ru.bolnik.messagedbhandler.repository.NutRepository;

@Service
public class NutService {

    private final NutRepository NutRepository;

    public NutService(ru.bolnik.messagedbhandler.repository.NutRepository nutRepository) {
        NutRepository = nutRepository;
    }
}
