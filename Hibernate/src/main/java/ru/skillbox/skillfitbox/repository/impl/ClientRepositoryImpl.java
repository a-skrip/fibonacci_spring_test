package ru.skillbox.skillfitbox.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.skillbox.skillfitbox.entity.Client;
import ru.skillbox.skillfitbox.repository.ClientRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Slf4j
@RequiredArgsConstructor
public class ClientRepositoryImpl implements ClientRepository {

    private final EntityManager em;


    @Override
    public Client save(Client client) {
        log.info("Вызов метода save для Client: {}", client.getId());
        em.persist(client);
        return client;

    }

    @Override
    public Client update(Client client) {
        log.info("Вызов метода update для Client: {}", client.getId());

        em.merge(client);

        return client;
    }

    @Override
    public Optional<Client> findById(UUID id) {
        log.info("Вызов метода findById для id {}", id);

        Client client = em.find(Client.class, id);

        return Optional.ofNullable(client);

    }

    @Override
    public List<Client> findAll() {
        log.info("Вызов метода findAll для Clients");

        TypedQuery<Client> query = em.createQuery("FROM Client", Client.class);
        return query.getResultList();

    }

    @Override
    public Optional<Client> findClientDetailById(UUID id) {
        log.info("Получение детальной информации клиента с ID: {}", id);

        return Optional.of(em.createQuery(
                        "SELECT DISTINCT c FROM Client c " +
                                "LEFT JOIN FETCH c.trainer " +
                                "LEFT JOIN FETCH c.locker " +
                                "LEFT JOIN FETCH c.services " +
                                "WHERE c.id = :id", Client.class)
                .setParameter("id", id)
                .getSingleResult());
    }
}
