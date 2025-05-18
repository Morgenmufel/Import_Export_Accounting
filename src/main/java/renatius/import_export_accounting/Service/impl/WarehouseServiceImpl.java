package renatius.import_export_accounting.Service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import renatius.import_export_accounting.Entity.Warehouse;
import renatius.import_export_accounting.Repositories.WarehouseRepository;
import renatius.import_export_accounting.Service.WarehouseService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;

    @Override
    public List<Warehouse> findAllWarehouses() {
        return warehouseRepository.findAll();
    }
}
