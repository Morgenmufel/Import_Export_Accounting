package renatius.import_export_accounting.Entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Product {

    @Id @GeneratedValue
    private Long id;
    private String name;
    private String productCode;
    private String originCountry;
    private String unitOfMeasure;
    private BigDecimal basePrice;
}
