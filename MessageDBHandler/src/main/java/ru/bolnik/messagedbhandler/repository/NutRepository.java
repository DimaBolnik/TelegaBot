package ru.bolnik.messagedbhandler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bolnik.messagedbhandler.entity.Nut;

import java.util.Optional;

@Repository
public interface NutRepository extends JpaRepository<Nut, Long> {

    Optional<Nut> findFirstByGostAndSize(String gost, String size);
}