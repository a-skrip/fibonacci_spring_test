package ru.skriplex.springnewsapplication.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "categories")
public class Category {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @OneToMany
    private List<News> listNews;
}
