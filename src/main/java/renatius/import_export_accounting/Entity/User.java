package renatius.import_export_accounting.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import renatius.import_export_accounting.Entity.Enum.UserRole;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;

    private String passwordHash;

    private UserRole role;

    private String email;

    @CreationTimestamp
    private LocalDateTime createAt;
}
