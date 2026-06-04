package ru.skillbox.skillfitbox.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skillbox.skillfitbox.dto.TrainerDetailDto;
import ru.skillbox.skillfitbox.dto.TrainerDto;
import ru.skillbox.skillfitbox.entity.Trainer;
import ru.skillbox.skillfitbox.entity.TrainerStatus;
import ru.skillbox.skillfitbox.mapper.TrainerMapper;
import ru.skillbox.skillfitbox.repository.ClientRepositoryImpl;
import ru.skillbox.skillfitbox.repository.TrainerRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Сервисный класс для бизнес-логики операций с тренерами.
 */
@Service
@RequiredArgsConstructor
public class TrainerService {

    private final TrainerRepository trainerRepository;
    private final TrainerMapper trainerMapper;

    /**
     * Добавляет нового тренера в систему.
     *
     * @param trainerDto данные тренера для добавления
     * @return созданный DTO тренера
     */
    public TrainerDto addTrainer(TrainerDto trainerDto) {
        Trainer trainer = trainerMapper.toEntity(trainerDto);
        Trainer savedTrainer = trainerRepository.save(trainer);
        return trainerMapper.toDto(savedTrainer);
    }

    /**
     * Обновляет информацию о тренере.
     *
     * @param id         ID тренера
     * @param trainerDto обновленные данные тренера
     * @return обновленный DTO тренера
     */
    public TrainerDto updateTrainer(UUID id, TrainerDto trainerDto) {
        Trainer existingTrainer = trainerRepository.findById(id);
        if (existingTrainer == null) {
            throw new RuntimeException("Тренер с ID " + id + " не найден");
        }

        trainerDto.setId(id);
        Trainer trainer = trainerMapper.toEntity(trainerDto);
        trainer.setCreatedDatetime(existingTrainer.getCreatedDatetime());
        Trainer updatedTrainer = trainerRepository.update(trainer);
        return trainerMapper.toDto(updatedTrainer);
    }

    /**
     * Изменяет статус тренера.
     *
     * @param id     ID тренера
     * @param status новый статус
     */
    public void changeTrainerStatus(UUID id, TrainerStatus status) {
        Trainer trainer = trainerRepository.findById(id);
        if (trainer == null) {
            throw new RuntimeException("Тренер с ID " + id + " не найден");
        }
        trainer.setStatus(status);

        trainerRepository.update(trainer);
    }

    /**
     * Получает тренера по ID.
     *
     * @param id ID тренера
     * @return DTO тренера или null если не найден
     */
    public TrainerDto getTrainerById(UUID id) {
        Trainer trainer = trainerRepository.findById(id);
        if (trainer == null) {
            return null;
        }
        return trainerMapper.toDto(trainer);
    }

    /**
     * Получает полную информацию о тренере включая имена клиентов.
     *
     * @param id ID тренера
     * @return подробный DTO тренера или null если не найден
     */
    public TrainerDetailDto getTrainerDetailById(UUID id) {

        Trainer detailById = trainerRepository.findTrainerDetailById(id);
        List<String> clientNames = extractClientNames(detailById);

        return trainerMapper.toDetailDto(detailById, clientNames);
    }

    /**
     * Получает список тренеров с краткой информацией.
     *
     * @return список DTO тренеров
     */
    public List<TrainerDto> getAllTrainers() {
        List<Trainer> trainers = trainerRepository.findAll();
        return trainers.stream()
                .map(trainerMapper::toDto)
                .collect(Collectors.toList());
    }

    private List<String> extractClientNames(Trainer trainer) {
        if (trainer.getClients() == null || trainer.getClients().isEmpty()) {
            return List.of();
        }

        return trainer.getClients().stream()
                .map(client -> String.format("%s %s %s",
                                client.getSurname(),
                                client.getName(),
                                client.getPatronymic() != null ? client.getPatronymic() : "")
                        .trim())
                .collect(Collectors.toList());
    }
}

