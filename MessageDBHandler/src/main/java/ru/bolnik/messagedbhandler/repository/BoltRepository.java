package ru.bolnik.messagedbhandler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bolnik.messagedbhandler.entity.Bolt;

@Repository
public interface BoltRepository extends JpaRepository<Bolt, Long> {
}