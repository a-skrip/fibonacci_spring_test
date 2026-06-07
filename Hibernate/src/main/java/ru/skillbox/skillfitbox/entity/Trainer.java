package ru.skillbox.skillfitbox.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trainers")
public class Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "patronymic")
    private String patronymic;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private TrainerStatus status;

    @CreationTimestamp  // автоматически проставляет дату при вставке
    @Column(name = "created_datetime", updatable = false)
    private LocalDateTime createdDatetime;

    @UpdateTimestamp  // автоматически обновляется при изменении
    @Column(name = "updated_datetime")
    private LocalDateTime updatedDatetime;

    //Инициализировать коллекцию (чтобы избежать NullPointerException)
    @OneToMany(mappedBy = "trainer", fetch = FetchType.LAZY)
    private List<Client> clients /*= new ArrayList<>()*/;
}
