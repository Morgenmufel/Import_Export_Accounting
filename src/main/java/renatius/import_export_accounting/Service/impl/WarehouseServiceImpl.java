package renatius.import_export_accounting.Service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import renatius.import_export_accounting.Entity.Warehouse;
import renatius.import_export_accounting.Repositories.WarehouseRepository;
import renatius.import_export_accounting.Service.WarehouseService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

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
}
