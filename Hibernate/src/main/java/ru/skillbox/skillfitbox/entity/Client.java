package ru.skillbox.skillfitbox.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "patronymic")
    private String patronymic;

    @Column(name = "birthday", nullable = false)
    private LocalDate birthday;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @CreationTimestamp  // автоматически проставляет дату при вставке
    @Column(name = "created_datetime", updatable = false)
    private LocalDateTime createdDatetime;

    @UpdateTimestamp  // автоматически обновляется при изменении
    @Column(name = "updated_datetime")
    private LocalDateTime updatedDatetime;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locker_id", unique = true)
    private Locker locker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "client_services",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private Set<AdditionalService> services = new HashSet<>();


}
