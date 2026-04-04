package ru.skriplex.springhotelapplication.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "hotel")
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "stars")
    private Integer stars;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private Instant creationDate;

    @Column(name = "updated_at")
    private Instant updatedDate;
}

