package renatius.import_export_accounting.Service;

import renatius.import_export_accounting.Entity.WarehouseProduct;
import java.util.List;

public interface WarehouseProductService {

    boolean existsByName(String productName, long warehouseId);

    boolean existsByProductCode(String productCode, long warehouseId);
    boolean existsByNameAndIdNot(String productName, long productId, long warehouseId);

    boolean existsByProductCodeAndIdNot(String productCode, long productId, long warehouseId);

    void saveWarehouseProduct(WarehouseProduct product);

    void deleteWarehouseProductByProductId(long productId);

    boolean existsByProductId(long productId);

    WarehouseProduct findByProductId(Long productId);
}
