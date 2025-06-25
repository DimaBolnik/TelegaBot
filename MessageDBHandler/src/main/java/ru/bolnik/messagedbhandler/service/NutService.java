package ru.bolnik.messagedbhandler.service;

import org.springframework.stereotype.Service;
import ru.bolnik.messagedbhandler.entity.Nut;
import ru.bolnik.messagedbhandler.repository.NutRepository;

import java.util.Optional;

@Service
public class NutService {

    private final NutRepository nutRepository;

    public NutService(ru.bolnik.messagedbhandler.repository.NutRepository nutRepository) {
        this.nutRepository = nutRepository;
    }

    public Optional<Nut> findFirstByGostAndSize(String gost, String size) {
        return nutRepository.findFirstByGostAndSize(gost, size);
    }
}
