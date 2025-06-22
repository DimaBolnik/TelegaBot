package ru.bolnik.messagedbhandler.service;

import org.springframework.stereotype.Service;
import ru.bolnik.messagedbhandler.entity.Bolt;
import ru.bolnik.messagedbhandler.repository.BoltRepository;

import java.util.Optional;

@Service
public class BoltService {

    private final BoltRepository boltRepository;

    public BoltService(BoltRepository boltRepository) {
        this.boltRepository = boltRepository;
    }

    public Optional<Bolt> findBolt(String gost, String size, Integer length) {
        return boltRepository.findFirstByGostAndSizeAndLength(gost, size, length);
    }

    public Double getWeightBolt(String gost, String size, Integer length) {
        Optional<Bolt> bolt = findBolt(gost, size, length);
        if (bolt.isPresent()) {
            return bolt.get().getWeight();
        }
        return 0.0;
    }

}