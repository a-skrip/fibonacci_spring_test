package ru.skillbox.skillfitbox.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.skillbox.skillfitbox.entity.AdditionalService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Slf4j
public class AdditionalServiceRepositoryJdbcTemplateImpl implements AdditionalServiceRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public AdditionalService findById(String id) {
        String sql = "SELECT * FROM services WHERE id = ?";

        log.info("Поиск услуги по id: {}", id);

        try {
            return jdbcTemplate.queryForObject(sql, additionalServiceRowMapper, id);
        } catch (Exception e) {
            log.error("Услуга с id {} не найдена", id);
            return null;
        }
    }

    @Override
    public List<AdditionalService> findAll() {
        String sql = "SELECT * FROM services ORDER BY name";

        log.info("Получение списка всех услуг");

        return jdbcTemplate.query(sql, additionalServiceRowMapper);
    }

    @Override
    public void addServiceToClient(UUID clientId, String serviceId) {
        String sql = "INSERT INTO client_services (client_id, service_id) VALUES (?, ?) ON CONFLICT (client_id, service_id) DO NOTHING";

        log.info("Добавление услуги {} клиенту {}", serviceId, clientId);

        int rowsAffected = jdbcTemplate.update(sql, clientId, serviceId);

        if (rowsAffected > 0) {
            log.info("Услуга {} успешно добавлена клиенту {}", serviceId, clientId);
        } else {
            log.warn("Услуга {} уже существует у клиента {} или не была добавлена", serviceId, clientId);
        }
    }

    @Override
    public List<String> findClientNamesByServiceId(String serviceId) {
        String sql = """
            SELECT CONCAT(c.surname, ' ', c.name, ' ', COALESCE(c.patronymic, '')) as client_name
                                FROM clients c
                                INNER JOIN client_services cs ON c.id = cs.client_id
                                WHERE cs.service_id = ? ORDER BY client_name
            """;

        log.info("Поиск клиентов по услуге: {}", serviceId);

        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("client_name"), serviceId);
    }

    // RowMapper для маппинга AdditionalService
    private final RowMapper<AdditionalService> additionalServiceRowMapper = (rs, rowNum) -> {
        AdditionalService service = new AdditionalService();
        service.setId(rs.getString("id"));
        service.setName(rs.getString("name"));
        service.setCreatedDatetime(rs.getObject("created_datetime", LocalDateTime.class));
        service.setUpdatedDatetime(rs.getObject("updated_datetime", LocalDateTime.class));
        return service;
    };

}