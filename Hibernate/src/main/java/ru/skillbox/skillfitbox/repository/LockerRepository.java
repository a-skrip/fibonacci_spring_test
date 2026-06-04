package ru.skillbox.skillfitbox.repository;

import ru.skillbox.skillfitbox.entity.Locker;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LockerRepository {

    Optional<Locker> findById(UUID id);

    List<Locker> findAllWithClientInfo();

    void update(Locker locker);

    }
