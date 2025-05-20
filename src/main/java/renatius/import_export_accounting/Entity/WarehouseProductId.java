package renatius.import_export_accounting.Entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseProductId implements Serializable {
    private Long productId;
    private Long warehouseId;
}
