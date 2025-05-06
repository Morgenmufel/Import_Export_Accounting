package renatius.import_export_accounting.Entity;


import jakarta.persistence.*;
import lombok.*;

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

    private String country;

    private String contactInfo;

    private String taxNumber;


    @OneToMany
    private List<Request> request;
}
