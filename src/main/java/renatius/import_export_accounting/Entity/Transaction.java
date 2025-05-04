package renatius.import_export_accounting.Entity;


import jakarta.persistence.*;
import lombok.*;
import renatius.import_export_accounting.Entity.Enum.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Transaction {
    @Id @GeneratedValue
    private Long id;
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @ManyToOne
    private Partner partner;

    @ManyToOne
    private User user;

    @ManyToOne
    private Warehouse warehouse;

    private BigDecimal totalValue;
}
