package ru.skillbox.skillfitbox.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lockers")
public class Locker {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "number")
    private Integer number;

    @CreationTimestamp  // автоматически проставляет дату при вставке
    @Column(name = "created_datetime", updatable = false)
    private LocalDateTime createdDatetime;

    @UpdateTimestamp  // автоматически обновляется при изменении
    @Column(name = "updated_datetime")
    private LocalDateTime updatedDatetime;

    // ОБРАТНАЯ сторона - НЕТ @JoinColumn!
    @OneToOne(mappedBy = "locker", fetch = FetchType.LAZY)
    private Client client;
}
