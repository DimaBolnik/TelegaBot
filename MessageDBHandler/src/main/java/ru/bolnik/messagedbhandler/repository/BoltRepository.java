package ru.bolnik.messagedbhandler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bolnik.messagedbhandler.entity.Bolt;

import java.util.Optional;

@Repository
public interface BoltRepository extends JpaRepository<Bolt, Long> {

    Optional<Bolt> findFirstByGostAndSizeAndLength(String gost, String size, Integer length);

}