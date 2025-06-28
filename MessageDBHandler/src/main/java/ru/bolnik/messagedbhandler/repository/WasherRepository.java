package ru.bolnik.messagedbhandler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bolnik.messagedbhandler.entity.Washer;

import java.util.Optional;

@Repository
public interface WasherRepository extends JpaRepository<Washer, Long> {

    Optional<Washer> findFirstByGostAndSize(String gost, String size);

}