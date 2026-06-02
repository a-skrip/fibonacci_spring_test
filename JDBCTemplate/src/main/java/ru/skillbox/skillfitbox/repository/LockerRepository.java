package ru.skillbox.skillfitbox.repository;

import org.springframework.stereotype.Repository;
import ru.skillbox.skillfitbox.entity.Locker;

import java.util.List;
import java.util.UUID;

@Repository

public interface LockerRepository {

    Locker findById(UUID id);

    List<Locker> findAllWithClientInfo();

    void update(Locker locker);
}
