package ru.skillbox.skillfitbox.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.skillbox.skillfitbox.entity.Trainer;
import ru.skillbox.skillfitbox.entity.TrainerStatus;

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

public interface TrainerRepository {


    Trainer save(Trainer trainer);

    Trainer update(Trainer trainer) ;

    Trainer findById(UUID id);

    List<Trainer> findAll();
}
