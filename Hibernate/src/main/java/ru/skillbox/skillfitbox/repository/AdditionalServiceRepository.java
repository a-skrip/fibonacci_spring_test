package ru.skillbox.skillfitbox.repository;

import ru.skillbox.skillfitbox.entity.AdditionalService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AdditionalServiceRepository {

    Optional<AdditionalService> findById(String id);

    List<AdditionalService> findAllWithDetails();

    void addServiceToClient(UUID clientId, String serviceId);

    Optional<AdditionalService> findDetailsById(String serviceId);
}
