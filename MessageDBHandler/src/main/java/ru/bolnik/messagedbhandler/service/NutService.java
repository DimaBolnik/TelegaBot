package ru.bolnik.messagedbhandler.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bolnik.messagedbhandler.entity.Nut;
import ru.bolnik.messagedbhandler.repository.NutRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NutService {

    private final NutRepository nutRepository;

    public Optional<Nut> findFirstByGostAndSize(String gost, String size) {
        return nutRepository.findFirstByGostAndSize(gost, size);
    }
}
