package ru.skillbox.skillfitbox.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.skillbox.skillfitbox.entity.Client;
import ru.skillbox.skillfitbox.entity.Locker;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Slf4j
public class LockerRepositoryJdbcTemplateImpl implements LockerRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Locker findById(UUID id) {
        String sql = """
                SELECT c.*,
                    l.id as l_id,
                    l.number as l_number,
                    l.created_datetime as l_created_datetime,
                    l.updated_datetime as l_updated_datetime
                FROM lockers l LEFT JOIN clients c ON l.client_id = c.id WHERE l.id = ?
                """;

        log.info("Поиск шкафчика по id: {}", id);

        try {
            return jdbcTemplate.queryForObject(sql, lockerWithClientRowMapper, id);
        } catch (Exception e) {
            log.error("Ошибка поиска шкафчика по id: {}", id, e);
            return null;
        }
    }

    @Override
    public List<Locker> findAllWithClientInfo() {
        String sql = """
                SELECT c.*,
                    l.id as l_id,
                    l.number as l_number,
                    l.created_datetime as l_created_datetime,
                    l.updated_datetime as l_updated_datetime
                FROM lockers l LEFT JOIN clients c ON l.client_id = c.id ORDER BY l.number
                """;

        log.info("Получение списка всех шкафчиков с информацией о клиентах");

        return jdbcTemplate.query(sql, lockerWithClientRowMapper);
    }

    @Override
    public void update(Locker locker) {
        String sql = "UPDATE lockers SET client_id = ?, updated_datetime = ? WHERE id = ?";

        UUID clientId = Optional.ofNullable(locker.getClient())
                .map(Client::getId)
                .orElse(null);

        log.info("Обновление шкафчика id: {}, clientId: {}", locker.getId(), clientId);

        int updatedRows = jdbcTemplate.update(sql,
                clientId,
                LocalDateTime.now(),
                locker.getId()
        );

        if (updatedRows == 0) {
            log.warn("Шкафчик с id: {} не найден для обновления", locker.getId());
        }
    }

    // RowMapper для маппинга Locker без клиента
    private final RowMapper<Locker> lockerRowMapper = (rs, rowNum) -> {
        Locker locker = new Locker();
        locker.setId(rs.getObject("l_id", UUID.class));
        locker.setNumber(rs.getInt("l_number"));
        locker.setCreatedDatetime(rs.getObject("l_created_datetime", LocalDateTime.class));
        locker.setUpdatedDatetime(rs.getObject("l_updated_datetime", LocalDateTime.class));
        return locker;
    };

    // RowMapper для маппинга Client
    private final RowMapper<Client> clientRowMapper = (rs, rowNum) -> {
        Client client = new Client();
        client.setId(rs.getObject("id", UUID.class));
        client.setSurname(rs.getString("surname"));
        client.setName(rs.getString("name"));
        client.setPatronymic(rs.getString("patronymic"));
        client.setBirthday(rs.getObject("birthday", LocalDate.class));
        client.setPhone(rs.getString("phone"));
        client.setEmail(rs.getString("email"));
        client.setIsActive(rs.getBoolean("is_active"));
        client.setCreatedDatetime(rs.getObject("created_datetime", LocalDateTime.class));
        client.setUpdatedDatetime(rs.getObject("updated_datetime", LocalDateTime.class));
        return client;
    };

    // RowMapper для маппинга Locker с клиентом
    private final RowMapper<Locker> lockerWithClientRowMapper = (rs, rowNum) -> {
        Locker locker = lockerRowMapper.mapRow(rs, rowNum);

        // Проверяем, есть ли данные о клиенте (ID не null)
        UUID clientId = rs.getObject("id", UUID.class);
        if (clientId != null) {
            Client client = clientRowMapper.mapRow(rs, rowNum);
            locker.setClient(client);
        }

        return locker;
    };

}




