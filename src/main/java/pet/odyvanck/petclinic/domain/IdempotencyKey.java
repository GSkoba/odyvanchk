package pet.odyvanck.petclinic.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "idempotency_keys")
@Data
public class IdempotencyKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "key", unique = true, nullable = false)
    private String key;

    @Column(columnDefinition = "jsonb", nullable = false)
    private String response;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;
}
