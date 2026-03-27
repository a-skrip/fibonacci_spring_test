package ru.skriplex.springnewsapplication.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "news")
@ToString
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "text_news")
    private String text;

    @Column(name = "creation_time")
    private Instant date;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
