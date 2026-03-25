package ru.skriplex.springnewsapplication.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import ru.skriplex.springnewsapplication.dto.NewsDto;

import java.time.Instant;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NewsService implements CRUDServices<NewsDto> {

    private static final Logger log = LogManager.getLogger(NewsService.class);
    private final ConcurrentHashMap<Long, NewsDto> storage = new ConcurrentHashMap<>();

    @Override
    public NewsDto getById(Long id) {
        if (id == null) {
            throw new RuntimeException("Не передан id");
        }
        log.info("Вызов метода getById({}) ", id);
        return storage.get(id);
    }

    @Override
    public Collection<NewsDto> getAll() {
        log.info("Вызов метода getAll()");
        return storage.values();
    }

    @Override
    public void create(NewsDto item) {
        long nextId = storage.isEmpty() ? 1 : storage.keySet().stream()
                .max(Long::compareTo).get() + 1;
        item.setId(nextId);
        item.setDate(Instant.now());
        storage.put(item.getId(), item);
        log.info("Вызов метода creat, присвоен id = {}", item.getId());
    }

    @Override
    public void update(NewsDto item) {
        Long newsId = item.getId();
        if (!storage.containsKey(newsId)) {
            throw new RuntimeException();
        }
        item.setDate(Instant.now());
        storage.put(newsId, item);
        log.info("Вызов метода update для id = {}", item.getId());
    }

    @Override
    public void delete(Long id) {
        System.out.println("delete");
        if (!storage.containsKey(id)) {
            throw new RuntimeException();
        }
        storage.remove(id);
        log.info("Вызов метода delete для id = {}", id);
    }
}
