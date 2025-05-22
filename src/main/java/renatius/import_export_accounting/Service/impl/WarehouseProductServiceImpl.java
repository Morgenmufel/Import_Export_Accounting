package renatius.import_export_accounting.Service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import renatius.import_export_accounting.Entity.WarehouseProduct;
import renatius.import_export_accounting.Repositories.ProductRepository;
import renatius.import_export_accounting.Repositories.WarehouseProductRepository;
import renatius.import_export_accounting.Repositories.WarehouseRepository;
import renatius.import_export_accounting.Service.WarehouseProductService;

@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class WarehouseProductServiceImpl implements WarehouseProductService {

    private final WarehouseProductRepository warehouseProductRepository;



    @Override
    public boolean existsByName(String productName, long warehouseId) {
        return warehouseProductRepository.existsByProductNameAndWarehouseId(productName, warehouseId);
    }

    @Override
    public boolean existsByProductCode(String productCode, long warehouseId) {
        return warehouseProductRepository.existsByProductProductCodeAndWarehouseId(productCode, warehouseId);
    }

    @Override
    public boolean existsByNameAndIdNot(String productName, long productId, long warehouseId) {
        return warehouseProductRepository.existsWarehouseProductByNameAndNotId(productName, productId, warehouseId);
    }

    @Override
    public boolean existsByProductCodeAndIdNot(String productCode, long productId, long warehouseId) {
        return warehouseProductRepository.existsWarehouseProductByProductCodeAndNotId(productCode, productId, warehouseId);
    }

    @Override
    public void saveWarehouseProduct(WarehouseProduct product) {
        warehouseProductRepository.save(product);
    }

    @Override
    public void deleteWarehouseProductByProductId(long productId) {
        warehouseProductRepository.deleteByProductId(productId);
    }

    @Override
    public boolean existsByProductId(long productId) {
        return warehouseProductRepository.existsByProductId(productId);
    }

    @Override
    public WarehouseProduct findByProductId(Long productId) {
        return warehouseProductRepository.findById(productId).get();
    }
}
