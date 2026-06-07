package ru.skillbox.skillfitbox.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.skillbox.skillfitbox.entity.AdditionalService;
import ru.skillbox.skillfitbox.entity.Client;
import ru.skillbox.skillfitbox.repository.AdditionalServiceRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Slf4j
public class AdditionalServiceRepositoryImpl implements AdditionalServiceRepository {

    private final EntityManager em;


    @Override
    public Optional<AdditionalService> findById(String serviceId) {
        log.info("Получить услугу по id: {}", serviceId);

        AdditionalService service = em.find(AdditionalService.class, serviceId);

        return Optional.ofNullable(service);
    }

    @Override
    public void addServiceToClient(UUID clientId, String serviceId) {
        log.info("Добавление услуги {} клиенту {}", serviceId, clientId);
        Client client = em.find(Client.class, clientId);
        AdditionalService service = em.find(AdditionalService.class, serviceId);

        client.getServices().add(service);
        service.getClients().add(client);

    }

    @Override
    public List<AdditionalService> findAllWithDetails() {
        log.info("Получение всех дополнительных услуг с клиентами");

        TypedQuery<AdditionalService> query = em.createQuery(
                "SELECT s FROM AdditionalService s " +
                "LEFT JOIN FETCH s.clients "
                , AdditionalService.class);
        return query.getResultList();
    }

    @Override
    public Optional<AdditionalService> findDetailsById(String serviceId) {
        log.info("Получение услуги {} с подключенными клиентами", serviceId);

        AdditionalService listServices = em.createQuery(
                        "SELECT s FROM AdditionalService s " +
                                "LEFT JOIN FETCH s.clients " +
                                "WHERE s.id = :id ", AdditionalService.class)
                .setParameter("id", serviceId)
                .getSingleResult();
        return Optional.ofNullable(listServices);

    }
}
