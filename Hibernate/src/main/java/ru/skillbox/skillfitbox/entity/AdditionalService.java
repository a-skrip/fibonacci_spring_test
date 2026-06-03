package ru.skillbox.skillfitbox.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "services")
public class AdditionalService {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @CreationTimestamp  // автоматически проставляет дату при вставке
    @Column(name = "created_datetime", updatable = false)
    private LocalDateTime createdDatetime;

    @UpdateTimestamp  // автоматически обновляется при изменении
    @Column(name = "updated_datetime")
    private LocalDateTime updatedDatetime;


    @ManyToMany(mappedBy = "services", fetch = FetchType.LAZY)  // ← mappedBy!
    private List<Client> clients /*= new ArrayList<>()*/;
}
