package ru.skriplex.springnewsapplication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skriplex.springnewsapplication.entities.News;

public interface NewsRepository extends JpaRepository<News, Long> {
}
