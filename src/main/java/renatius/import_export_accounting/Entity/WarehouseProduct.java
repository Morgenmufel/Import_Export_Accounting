package renatius.import_export_accounting.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class WarehouseProduct {

    @EmbeddedId
    private WarehouseProductId id;

    @ManyToOne(fetch = FetchType.EAGER) @MapsId("productId")
    private Product product;

    @ManyToOne
    @MapsId("warehouseId")
    private Warehouse warehouse;

    private int quantity;

    private LocalDateTime updatedAt;
}
