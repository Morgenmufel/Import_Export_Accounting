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
public class TransactionProduct {
    @EmbeddedId
    private TransactionProductId id;

    @ManyToOne
    @MapsId("transactionId")
    private Transaction transaction;

    @ManyToOne
    @MapsId("productId")
    private Product product;

    private int quantity;


    private BigDecimal unitPrice;
}
