package renatius.import_export_accounting.Service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import renatius.import_export_accounting.Entity.Warehouse;
import renatius.import_export_accounting.Entity.WarehouseProduct;
import renatius.import_export_accounting.Repositories.WarehouseProductRepository;
import renatius.import_export_accounting.Repositories.WarehouseRepository;
import renatius.import_export_accounting.Service.WarehouseService;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseProductRepository warehouseProductRepository;

    private final WarehouseRepository warehouseRepository;

    @Override
    public List<Warehouse> findAllWarehouses() {
        return warehouseRepository.findAll();
    }

    @Override
    public Optional<Warehouse> findWarehouseByName(String name) {
        return warehouseRepository.findByName(name);
    }

    @Override
    public void saveWarehouse(Warehouse warehouse) {
        warehouseRepository.save(warehouse);
    }

    @Override
    public void updateWarehouse(Warehouse warehouse) {
        warehouseRepository.save(warehouse);
    }

    @Override
    public Optional<Warehouse> findWarehouseById(long id) {
        return warehouseRepository.findById(id);
    }

    @Override
    public void deleteWarehouseById(long id) {
        warehouseRepository.deleteById(id);
    }

    @Override
    public Optional<Warehouse> findWarehouseByAddress(String address) {
        return warehouseRepository.findByAddress(address);
    }

    @Override
    public boolean excitingWarehouseByName(String name, Long notId) {
        return warehouseRepository.existsByNameAndIdNot(name, notId);
    }

    @Override
    public boolean excitingWarehouseByAddress(String address, Long notId) {
        return warehouseRepository.existsByAddressAndIdNot(address, notId);
    }

    @Override
    public List<WarehouseProduct> getProductWarehouseInfo(Long productId) {
        return warehouseProductRepository.findByProductIdWithWarehouse(productId);
    }

}
