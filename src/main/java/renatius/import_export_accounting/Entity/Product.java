package renatius.import_export_accounting.Entity;


import jakarta.persistence.*;
import lombok.*;
import renatius.import_export_accounting.Entity.Enum.OperationType;

import java.math.BigDecimal;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String productCode;

    private String originCountry;

    private String unitOfMeasure;

    private BigDecimal basePrice;

    @Enumerated(EnumType.STRING)
    private OperationType operationType;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private Request request;

}
