package renatius.import_export_accounting.Entity;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
public class WarehouseProductId implements Serializable {
    private Long productId;
    private Long warehouseId;
}
