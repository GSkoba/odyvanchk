package pet.odyvanck.petclinic.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.ACTIVE;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now(ZoneOffset.UTC);

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now(ZoneOffset.UTC);

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Owner owner;

    @PreUpdate
    public void setLastUpdate() {
        this.updatedAt = LocalDateTime.now(ZoneOffset.UTC);
    }

}
