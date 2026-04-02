package ru.skriplex.springnewsapplication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skriplex.springnewsapplication.entities.News;

import java.util.Collection;

public interface NewsRepository extends JpaRepository<News, Long> {

    Collection<News> getAllByCategoryId(Long Id);
}
