package ru.skriplex.springnewsapplication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skriplex.springnewsapplication.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
