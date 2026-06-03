package ru.skillbox.skillfitbox.repository;

import org.springframework.stereotype.Repository;
import ru.skillbox.skillfitbox.entity.AdditionalService;

import java.util.List;
import java.util.UUID;

@Repository

public interface AdditionalServiceRepository {

    AdditionalService findById(String id);

    List<AdditionalService> findAll();

    void addServiceToClient(UUID clientId, String serviceId);

    List<String> findClientNamesByServiceId(String serviceId);
}
