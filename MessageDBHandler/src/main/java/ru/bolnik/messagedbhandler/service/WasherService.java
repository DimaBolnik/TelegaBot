package ru.bolnik.messagedbhandler.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bolnik.messagedbhandler.entity.Washer;
import ru.bolnik.messagedbhandler.repository.WasherRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WasherService {

    private final WasherRepository washerRepository;

    public Optional<Washer> findFirstByGostAndSize(String gost, String size) {
        return washerRepository.findFirstByGostAndSize(gost, size);
    }

}