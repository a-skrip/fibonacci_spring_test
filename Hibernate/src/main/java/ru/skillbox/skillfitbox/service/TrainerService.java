package ru.skillbox.skillfitbox.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.skillfitbox.dto.TrainerDetailDto;
import ru.skillbox.skillfitbox.dto.TrainerDto;
import ru.skillbox.skillfitbox.entity.Trainer;
import ru.skillbox.skillfitbox.entity.TrainerStatus;
import ru.skillbox.skillfitbox.mapper.TrainerMapper;
import ru.skillbox.skillfitbox.repository.TrainerRepository;
import ru.skillbox.skillfitbox.repository.TrainerRepositoryImpl;

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
    @Transactional
    public TrainerDto addTrainer(TrainerDto trainerDto) {
        Trainer trainer = trainerMapper.toEntity(trainerDto);
        Trainer savedTrainer = trainerRepository.save(trainer);
        return trainerMapper.toDto(savedTrainer);
    }

    /**
     * Обновляет информацию о тренере.
     *
     * @param trainerId  ID тренера
     * @param trainerDto обновленные данные тренера
     * @return обновленный DTO тренера
     */
    @Transactional
    public TrainerDto updateTrainer(UUID trainerId, TrainerDto trainerDto) {
        Trainer existingTrainer = trainerRepository.findById(trainerId)
                .orElseThrow(() -> new RuntimeException("Тренер с ID " + trainerId + " не найден"));

        trainerDto.setId(trainerId);
        Trainer trainer = trainerMapper.toEntity(trainerDto);
//        trainer.setCreatedDatetime(existingTrainer.getCreatedDatetime());
        Trainer updatedTrainer = trainerRepository.update(trainer);
        return trainerMapper.toDto(updatedTrainer);
    }

    /**
     * Изменяет статус тренера.
     *
     * @param trainerId ID тренера
     * @param status    новый статус
     */
    @Transactional
    public void changeTrainerStatus(UUID trainerId, TrainerStatus status) {
        Trainer trainer = trainerRepository.findById(trainerId)
                .orElseThrow(() -> new RuntimeException("Тренер с ID " + trainerId + " не найден"));

        trainer.setStatus(status);

        trainerRepository.update(trainer);
    }

    /**
     * Получает тренера по ID.
     *
     * @param trainerId ID тренера
     * @return DTO тренера или null если не найден
     */
    @Transactional(readOnly = true)
    public TrainerDto getTrainerById(UUID trainerId) {
        Trainer trainer = trainerRepository.findById(trainerId)
                .orElseThrow(() -> new RuntimeException("Тренер с ID " + trainerId + " не найден"));

        return trainerMapper.toDto(trainer);
    }

    /**
     * Получает полную информацию о тренере включая имена клиентов.
     *
     * @param trainerId ID тренера
     * @return подробный DTO тренера или null если не найден
     */
    @Transactional(readOnly = true)
    public TrainerDetailDto getTrainerDetailById(UUID trainerId) {

        Trainer detailById = trainerRepository.findTrainerDetailById(trainerId)
                .orElseThrow(() -> new RuntimeException("Тренер с ID " + trainerId + " не найден"));

        List<String> clientNames = extractClientNames(detailById);

        return trainerMapper.toDetailDto(detailById, clientNames);
    }

    /**
     * Получает список тренеров с краткой информацией.
     *
     * @return список DTO тренеров
     */
    @Transactional(readOnly = true)
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

