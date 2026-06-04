package ru.skillbox.skillfitbox.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.skillfitbox.dto.ServiceDto;
import ru.skillbox.skillfitbox.entity.AdditionalService;
import ru.skillbox.skillfitbox.mapper.ServiceMapper;
import ru.skillbox.skillfitbox.repository.AdditionalServiceRepository;
import ru.skillbox.skillfitbox.repository.impl.AdditionalServiceRepositoryImpl;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервисный класс для бизнес-логики операций с услугами.
 */
@Service
@RequiredArgsConstructor
public class AdditionalServicesHandlingService {

    private final AdditionalServiceRepository additionalServiceRepository;
    private final ServiceMapper serviceMapper;

    /**
     * Получает список всех услуг с именами клиентов.
     *
     * @return список DTO услуг с именами клиентов
     */
    @Transactional(readOnly = true)
    public List<ServiceDto> getAllServices() {
        List<AdditionalService> additionalServices = additionalServiceRepository.findAllWithDetails();
        return additionalServices.stream()
                .map(service -> {
                    ServiceDto serviceDto = serviceMapper.toDto(service);
//                    List<String> clientNames = additionalServiceRepository.findDetailsById(serviceDto.getId());

                    serviceDto.setClientNames(extractClientNames(service));
                    return serviceDto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Получает информацию о конкретной услуге по ID включая имена клиентов.
     *
     * @param serviceId ID услуги
     * @return DTO услуги с именами клиентов или null если не найдена
     */
    @Transactional(readOnly = true)
    public ServiceDto getServiceByIdWithClients(String serviceId) {
        AdditionalService service = additionalServiceRepository.findDetailsById(serviceId)
                .orElseThrow(() -> new RuntimeException("Ошибка поиска услуги по id"));

        ServiceDto serviceDto = serviceMapper.toDto(service);

        serviceDto.setClientNames(extractClientNames(service));

        return serviceDto;
    }

    private List<String> extractClientNames(AdditionalService service) {
        return service.getClients().stream()
                .map(client -> String.format("%s %s %s",
                                client.getName(),
                                client.getSurname(),
                                client.getPatronymic() != null ? client.getPatronymic() : "")
                        .trim())
                .toList();
    }
}
