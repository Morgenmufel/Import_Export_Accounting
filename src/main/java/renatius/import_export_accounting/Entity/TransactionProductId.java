package renatius.import_export_accounting.Entity;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class TransactionProductId {
    private Long transactionId;
    private Long productId;
}
