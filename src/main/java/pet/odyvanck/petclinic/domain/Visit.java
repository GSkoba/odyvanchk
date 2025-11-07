package pet.odyvanck.petclinic.domain;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Table(name = "visits")
@Data
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;

    @ManyToOne
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "vet_id", nullable = false)
    private Vet vet;

    @ManyToOne
    @JoinColumn(name = "slot_id", nullable = false)
    private VetSlot slot;

    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable = false)
    private VisitStatus status = VisitStatus.SCHEDULED;

    @Column(length = 500)
    private String cancellationReason;

    @Column(name = "completion_date_time")
    private LocalDateTime completionDateTime;

}

