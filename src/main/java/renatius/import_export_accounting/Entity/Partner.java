package renatius.import_export_accounting.Entity;


import jakarta.persistence.*;
import lombok.*;
import renatius.import_export_accounting.Entity.Enum.PartnerRole;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Partner {
    @Id @GeneratedValue
    private Long id;
    private String name;
    private String country;
    private String contactInfo;
    private String taxNumber;

    @Enumerated(EnumType.STRING)
    private PartnerRole type;
}
