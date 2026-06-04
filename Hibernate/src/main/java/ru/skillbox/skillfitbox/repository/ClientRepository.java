package ru.skillbox.skillfitbox.repository;

import ru.skillbox.skillfitbox.entity.Client;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientRepository {

    Client save(Client client);

    Client update(Client client);

    Optional<Client> findById(UUID id);

    List<Client> findAll();

//    List<String> findClientNamesByTrainerId(UUID trainerId);

    Optional<Client> findClientDetailById(UUID id);

}
