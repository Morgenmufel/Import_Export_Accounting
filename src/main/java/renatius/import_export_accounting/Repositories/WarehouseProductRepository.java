package renatius.import_export_accounting.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import renatius.import_export_accounting.Entity.WarehouseProduct;

public interface WarehouseProductRepository extends JpaRepository<WarehouseProduct, Long> {
}
