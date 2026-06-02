package ru.skillbox.skillfitbox.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.skillbox.skillfitbox.entity.AdditionalService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository

public interface AdditionalServiceRepository {




   AdditionalService findById(String id);

   List<AdditionalService> findAll();

    void addServiceToClient(UUID clientId, String serviceId);

    List<String> findClientNamesByServiceId(String serviceId);
}
