package renatius.import_export_accounting.Service;

import renatius.import_export_accounting.Entity.Warehouse;
import renatius.import_export_accounting.Entity.WarehouseProduct;

import java.util.List;
import java.util.Optional;

public interface WarehouseService {

    List<Warehouse> findAllWarehouses();

    Optional<Warehouse> findWarehouseByName(String name);

    void saveWarehouse(Warehouse warehouse);

    void updateWarehouse(Warehouse warehouse);

    Optional<Warehouse> findWarehouseById(long id);

    void deleteWarehouseById(long id);

    Optional<Warehouse> findWarehouseByAddress(String address);

    boolean excitingWarehouseByName(String name, Long notId);

    boolean excitingWarehouseByAddress(String address, Long notId);

    List<WarehouseProduct> getProductWarehouseInfo(Long productId);

    List<WarehouseProduct> findWarehouseProductByWarehouseId(Long warehouseId);


}
