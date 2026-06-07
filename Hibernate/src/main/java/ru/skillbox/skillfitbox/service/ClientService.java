package ru.skillbox.skillfitbox.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.skillfitbox.dto.ClientDetailDto;
import ru.skillbox.skillfitbox.dto.ClientDto;
import ru.skillbox.skillfitbox.entity.AdditionalService;
import ru.skillbox.skillfitbox.entity.Client;
import ru.skillbox.skillfitbox.entity.Locker;
import ru.skillbox.skillfitbox.entity.Trainer;
import ru.skillbox.skillfitbox.mapper.ClientMapper;
import ru.skillbox.skillfitbox.mapper.LockerMapper;
import ru.skillbox.skillfitbox.mapper.TrainerMapper;
import ru.skillbox.skillfitbox.repository.AdditionalServiceRepository;
import ru.skillbox.skillfitbox.repository.ClientRepository;
import ru.skillbox.skillfitbox.repository.LockerRepository;
import ru.skillbox.skillfitbox.repository.TrainerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Сервисный класс для бизнес-логики операций с клиентами.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {

    private final ClientRepository clientRepository;
    private final TrainerRepository trainerRepository;
    private final LockerRepository lockerRepository;
    private final AdditionalServiceRepository additionalServiceRepository;
    private final ClientMapper clientMapper;
    private final TrainerMapper trainerMapper;
    private final LockerMapper lockerMapper;

    /**
     * Добавляет нового клиента в систему.
     *
     * @param clientDto данные клиента для добавления
     * @return созданный DTO клиента
     */
    @Transactional
    public ClientDto addClient(ClientDto clientDto) {
        Client client = clientMapper.toEntity(clientDto);
        client.setIsActive(true);
        Client savedClient = clientRepository.save(client);
        return clientMapper.toDto(savedClient);
    }

    /**
     * Обновляет информацию о клиенте.
     *
     * @param id        ID клиента
     * @param clientDto обновленные данные клиента
     * @return обновленный DTO клиента
     */
    @Transactional
    public ClientDto updateClient(UUID id, ClientDto clientDto) {
        Client existingClient = clientRepository.findClientDetailById(id)
                .orElseThrow(() -> new RuntimeException("Клиент с ID " + id + " не найден"));

        clientDto.setId(id);
        Client client = clientMapper.toEntity(clientDto);
        client.setServices(existingClient.getServices());
        client.setLocker(existingClient.getLocker());
        client.setTrainer(existingClient.getTrainer());
        client.setCreatedDatetime(existingClient.getCreatedDatetime());

        Client updatedClient = clientRepository.update(client);
        return clientMapper.toDto(updatedClient);
    }

    /**
     * Получает всех клиентов.
     *
     * @return список всех DTO клиентов
     */
    @Transactional(readOnly = true)
    public List<ClientDto> getAllClients() {
        List<Client> clients = clientRepository.findAll();
        return clients.stream()
                .map(clientMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Получает краткую информацию о клиенте по ID.
     *
     * @param id ID клиента
     * @return DTO клиента или null если не найден
     */
    @Transactional(readOnly = true)
    public ClientDto getClientById(UUID id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Клиент с ID " + id + " не найден"));
        ;
        return client != null ? clientMapper.toDto(client) : null;
    }

    /**
     * Получает подробную информацию о клиенте по ID включая тренера, услуги и шкафчик.
     * Использует один SQL-запрос с JOIN для оптимизации производительности.
     *
     * @param id ID клиента
     * @return подробный DTO клиента или null если не найден
     */
    @Transactional(readOnly = true)
    public ClientDetailDto getClientDetailById(UUID id) {
        Client client = clientRepository.findClientDetailById(id)
                .orElseThrow(() -> new RuntimeException("Клиент с ID " + id + " не найден"));

        ClientDetailDto detailDto = new ClientDetailDto();
        detailDto.setId(client.getId());
        detailDto.setSurname(client.getSurname());
        detailDto.setName(client.getName());
        detailDto.setPatronymic(client.getPatronymic());
        detailDto.setBirthday(client.getBirthday());
        detailDto.setPhone(client.getPhone());
        detailDto.setEmail(client.getEmail());
        detailDto.setIsActive(client.getIsActive());

        // Заполняем информацию о тренере
        if (client.getTrainer() != null) {
            detailDto.setTrainer(trainerMapper.toDto(client.getTrainer()));
        }

        // Заполняем информацию о шкафчике
        if (client.getLocker() != null) {
            detailDto.setLocker(lockerMapper.toDto(client.getLocker()));
        }

        // Заполняем услуги
        if (client.getServices() != null && !client.getServices().isEmpty()) {
            List<String> serviceDtos = client.getServices().stream()
                    .map(AdditionalService::getName)
                    .collect(Collectors.toList());
            detailDto.setServices(serviceDtos);
        } else {
            detailDto.setServices(new ArrayList<>());
        }

        return detailDto;
    }

    /**
     * Активирует или деактивирует клиента.
     *
     * @param id       ID клиента
     * @param isActive новый статус активности
     */
    @Transactional
    public void updateClientStatus(UUID id, Boolean isActive) {
        Client client = clientRepository.findClientDetailById(id)
                .orElseThrow(() -> new RuntimeException("Клиент с ID " + id + " не найден"));
        ;
        client.setIsActive(isActive);
        clientRepository.update(client);
    }

    /**
     * Назначает тренера клиенту.
     *
     * @param clientId  ID клиента
     * @param trainerId ID тренера
     */
    @Transactional
    public void assignTrainer(UUID clientId, UUID trainerId) {
        Client client = clientRepository.findClientDetailById(clientId)
                .orElseThrow(() -> new RuntimeException("Клиент с ID " + clientId + " не найден"));
        ;

        Trainer trainer = trainerRepository.findById(trainerId)
                .orElseThrow(() -> new RuntimeException("Тренер с ID " + trainerId + " не найден"));

        client.setTrainer(trainer);

        clientRepository.update(client);
    }

    /**
     * Добавляет дополнительную услугу клиенту.
     *
     * @param clientId  ID клиента
     * @param serviceId ID услуги
     */
    @Transactional
    public void addServiceToClient(UUID clientId, String serviceId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Клиент с ID " + clientId + " не найден"));

        AdditionalService service = additionalServiceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Услуга с ID " + serviceId + " не найдена"));

        boolean alreadyHasService = client.getServices().stream()
                .anyMatch(s -> s.getId().equals(serviceId));

        if (alreadyHasService) {
            log.warn("Услуга {} уже добавлена клиенту {}", serviceId, clientId);
            return;
        }
        additionalServiceRepository.addServiceToClient(clientId, serviceId);
    }

    /**
     * Назначает шкафчик клиенту.
     *
     * @param clientId ID клиента
     * @param lockerId ID шкафчика
     */
    @Transactional
    public void assignLocker(UUID clientId, UUID lockerId) {
        log.info("Назначение шкафчика {} клиенту {}", lockerId, clientId);

        // Загружаем сущности
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Клиент не найден"));

        Locker locker = lockerRepository.findById(lockerId)
                .orElseThrow(() -> new RuntimeException("Шкафчик не найден"));

        // ВАЖНО: Проверяем, не занят ли шкафчик (нужно инициализировать LAZY поле)
        if (locker.getClient() != null && !locker.getClient().getId().equals(clientId)) {
            throw new RuntimeException("Шкафчик уже занят другим клиентом");
        }

        // Проверка: не пытаемся ли назначить тот же шкафчик
        if (client.getLocker() != null && client.getLocker().getId().equals(lockerId)) {
            log.info("У клиента уже назначен этот шкафчик");
            return;
        }

        // Освобождаем старый шкафчик (если был)
        if (client.getLocker() != null) {
            Locker oldLocker = client.getLocker();
            oldLocker.setClient(null);  // Разрываем связь в обратной стороне
            // Не нужно вызывать update!
        }

        // КРИТИЧНО: Синхронизируем ОБЕ стороны!
        client.setLocker(locker);  // Устанавливаем связь с клиента
        locker.setClient(client);   // Устанавливаем обратную связь (ВАЖНО!)

        // После этого Hibernate сам выполнит:
        // UPDATE clients SET locker_id = ? WHERE id = ?

        log.info("Шкафчик {} назначен клиенту {}", locker.getNumber(), client.getSurname());

        // НЕ вызывайте clientRepository.update(client) - это лишнее!
        // Hibernate автоматически сохранит изменения в конце транзакции
    }
}
