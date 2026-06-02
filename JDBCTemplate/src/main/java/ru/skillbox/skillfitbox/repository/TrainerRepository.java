package ru.skillbox.skillfitbox.repository;

import org.springframework.stereotype.Repository;
import ru.skillbox.skillfitbox.entity.Trainer;

import java.util.List;
import java.util.UUID;

@Repository

public interface TrainerRepository {

    Trainer save(Trainer trainer);

    Trainer update(Trainer trainer);

    Trainer findById(UUID id);

    List<Trainer> findAll();
}
