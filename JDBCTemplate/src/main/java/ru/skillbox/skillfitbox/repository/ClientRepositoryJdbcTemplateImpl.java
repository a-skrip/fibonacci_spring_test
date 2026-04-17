package ru.skillbox.skillfitbox.repository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.skillbox.skillfitbox.entity.AdditionalService;
import ru.skillbox.skillfitbox.entity.Client;
import ru.skillbox.skillfitbox.entity.Locker;
import ru.skillbox.skillfitbox.entity.Trainer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
@AllArgsConstructor
@Slf4j
public class ClientRepositoryJdbcTemplateImpl implements ClientRepository {

    private final JdbcTemplate jdbcTemplate;


    @Override
    public Client save(Client client) {
        String sql = """
                INSERT INTO clients (id, surname, name, patronymic, birthday, phone, email,
                is_active, locker_id, created_datetime, updated_datetime)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                RETURNING *
                """;
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        client.setId(id);
        client.setCreatedDatetime(now);
        client.setUpdatedDatetime(now);
        log.info("Вызов метода сохранения Клиента");


        jdbcTemplate.queryForObject(sql, ROW_MAPPER,
                client.getId(),
                client.getSurname(),
                client.getName(),
                client.getPatronymic(),
                client.getBirthday(),
                client.getPhone(),
                client.getEmail(),
                client.getIsActive(),
                client.getLocker() != null ? client.getLocker().getId() : null,
                client.getCreatedDatetime(),
                client.getUpdatedDatetime()
        );

        return client;
    }

    @Override
    public Client update(Client client) {
        String sql = """
                UPDATE clients SET surname = ?, name = ?, patronymic = ?, birthday = ?, phone = ?, email = ?,
                is_active = ?, locker_id = ?, trainer_id = ?, updated_datetime = ? WHERE id = ?
                RETURNING *
                """;

        LocalDateTime now = LocalDateTime.now();
        client.setUpdatedDatetime(now);

        log.info("Обновление данных клиента с id: {}", client.getId());

        try {
            return jdbcTemplate.queryForObject(sql, ROW_MAPPER,
                    client.getSurname(),
                    client.getName(),
                    client.getPatronymic(),
                    client.getBirthday(),
                    client.getPhone(),
                    client.getEmail(),
                    client.getIsActive(),
                    client.getLocker() != null ? client.getLocker().getId() : null,
                    client.getTrainer() != null ? client.getTrainer().getId() : null,
                    client.getUpdatedDatetime(),
                    client.getId());
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Клиент id: " + client.getId() +  " не НАЙДЕН");

        }
    }

    @Override
    public Client findById(UUID clientId) {
        String sql = "SELECT * FROM clients WHERE id = ?";

        log.info("Поиск клиента по id: {}", clientId);

        Client client = null;
        try {
            return client = jdbcTemplate.queryForObject(sql, ROW_MAPPER, clientId);
        } catch (DataAccessException e) {
            throw new EntityNotFoundException("Клиент не найден");
        }
    }


    @Override
    public List<Client> findAll() {
        String sql = "SELECT * FROM clients ORDER BY created_datetime DESC";

        log.info("Поиск всех клиентов");

        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    @Override
    public List<String> findClientNamesByTrainerId(UUID trainerId) {
        String sql = """
                SELECT CONCAT(surname, ' ', name, ' ', COALESCE(patronymic, '')) as client_name
                FROM clients WHERE trainer_id = ? ORDER BY surname, name
                """;
        List<String> names = new ArrayList<>();

        List<Client> query = jdbcTemplate.query(sql, ROW_MAPPER, trainerId);

        return query.stream()
                .map(Client::getName)
                .toList();
    }

    @Override
    public Client findClientDetailById(UUID id) {
        // Используем существующий ROW_MAPPER для основного запроса
        String mainSql = """
                SELECT c.*,
                       t.id as trainer_id,
                       l.id as locker_id
                FROM clients c
                LEFT JOIN trainers t ON c.trainer_id = t.id
                LEFT JOIN lockers l ON c.locker_id = l.id
                WHERE c.id = ?
                """;

        Client client = null;
        try {
            client = jdbcTemplate.queryForObject(mainSql, ROW_MAPPER, id);
        } catch (DataAccessException e) {
            log.warn("Клиент с id {} не найден", id);
        }

        if (client == null) {
            return null;
        }

        // Догружаем услуги отдельным запросом
        String servicesSql = """
                SELECT s.id, s.name, s.created_datetime, s.updated_datetime
                FROM services s
                JOIN client_services cs ON s.id = cs.service_id
                WHERE cs.client_id = ?
                """;

        List<AdditionalService> services = jdbcTemplate.query(servicesSql,
                (rs, rowNum) -> {
                    AdditionalService service = new AdditionalService();
                    service.setId(rs.getString("id"));
                    service.setName(rs.getString("name"));
                    service.setCreatedDatetime(rs.getObject("created_datetime", LocalDateTime.class));
                    service.setUpdatedDatetime(rs.getObject("updated_datetime", LocalDateTime.class));
                    return service;
                }, id);

        client.setServices(services);
        return client;
    }

    private static final RowMapper<Client> ROW_MAPPER = ((rs, rowNum) -> {
        Client client = new Client();
        client.setId(rs.getObject("id", UUID.class));
        client.setSurname(rs.getString("surname"));
        client.setName(rs.getString("name"));
        client.setPatronymic(rs.getString("patronymic"));
        client.setBirthday(rs.getObject("birthday", LocalDate.class));
        client.setPhone(rs.getString("phone"));
        client.setEmail(rs.getString("email"));
        client.setIsActive(rs.getObject("is_active", Boolean.class));
        // Исправление: получаем UUID locker_id и создаем объект Locker
        UUID lockerId = rs.getObject("locker_id", UUID.class);
        if (lockerId != null) {
            Locker locker = new Locker();
            locker.setId(lockerId);
            client.setLocker(locker);
        }

        // Исправление: получаем UUID trainer_id и создаем объект Trainer
        UUID trainerId = rs.getObject("trainer_id", UUID.class);
        if (trainerId != null) {
            Trainer trainer = new Trainer();
            trainer.setId(trainerId);
            client.setTrainer(trainer);
        }
        client.setCreatedDatetime(rs.getObject("created_datetime", LocalDateTime.class));
        client.setUpdatedDatetime(rs.getObject("updated_datetime", LocalDateTime.class));

        return client;
    });
}
