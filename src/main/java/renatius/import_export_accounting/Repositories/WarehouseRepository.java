package renatius.import_export_accounting.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import renatius.import_export_accounting.Entity.Warehouse;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
}
