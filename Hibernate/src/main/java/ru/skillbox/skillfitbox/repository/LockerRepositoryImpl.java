package ru.skillbox.skillfitbox.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.skillbox.skillfitbox.entity.Client;
import ru.skillbox.skillfitbox.entity.Locker;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Slf4j
public class LockerRepositoryImpl implements LockerRepository{

    private final EntityManager em;

    @Override
    public Optional<Locker> findById(UUID id) {
        return Optional.of(em.find(Locker.class, id));

    }

    @Override
    public List<Locker> findAllWithClientInfo() {
        TypedQuery<Locker> query = em.createQuery(
                "SELECT DISTINCT l FROM Locker l " +
                        "LEFT JOIN FETCH l.client " +
                        "ORDER BY l.number", Locker.class
        );
        return query.getResultList();
    }

    @Override
    public void update(Locker locker) {
        em.merge(locker);
    }
}
