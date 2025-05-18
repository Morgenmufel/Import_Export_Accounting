package renatius.import_export_accounting.Entity;


import jakarta.persistence.*;
import lombok.*;
import renatius.import_export_accounting.Entity.Enum.UserRole;

import java.util.List;


@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Partner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String nameOfCompany;

    private String passwordHash;

    private String country;

    private String contactInfo;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.ROLE_PARTNER;

    private String taxNumber;

    private String email;

    @OneToMany
    private List<Request> request;
}
