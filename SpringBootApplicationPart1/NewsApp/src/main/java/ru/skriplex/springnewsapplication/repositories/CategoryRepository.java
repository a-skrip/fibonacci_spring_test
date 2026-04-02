package ru.skriplex.springnewsapplication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skriplex.springnewsapplication.entities.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByTitle(String title);
}
