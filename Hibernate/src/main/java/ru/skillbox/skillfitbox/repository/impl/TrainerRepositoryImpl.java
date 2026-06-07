package ru.skillbox.skillfitbox.repository.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.skillbox.skillfitbox.entity.Trainer;
import ru.skillbox.skillfitbox.repository.TrainerRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Slf4j
public class TrainerRepositoryImpl implements TrainerRepository {

    private final EntityManager em;


    public Trainer save(Trainer trainer) {
        log.info("Создание тренера");

        em.persist(trainer);

        return trainer;
    }

    public Trainer update(Trainer trainer) {
        log.info("Обновление данных о тренере");

        return em.merge(trainer);
    }

    public Optional<Trainer> findById(UUID id) {
        log.info("Получение тренера по id: {}", id);

        Trainer trainer = em.find(Trainer.class, id);
        return Optional.of(trainer);
    }

    public List<Trainer> findAll() {
        log.info("Получение списка тренеров");

        TypedQuery<Trainer> query = em.createQuery("FROM Trainer", Trainer.class);
        return query.getResultList();
    }

    public Optional<Trainer> findTrainerDetailById(UUID trainerId) {
        log.info("Получение информации о тренере со списком клиентов ");

        Trainer trainer = em.createQuery(
                        "SELECT DISTINCT t FROM Trainer t " +
                                "LEFT JOIN FETCH t.clients " +
                                "WHERE t.id = :id", Trainer.class)
                .setParameter("id", trainerId)
                .getSingleResult();
        return Optional.of(trainer);
    }
}
