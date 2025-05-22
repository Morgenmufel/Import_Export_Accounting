package renatius.import_export_accounting.Service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import renatius.import_export_accounting.Entity.Enum.OperationType;
import renatius.import_export_accounting.Entity.Product;
import renatius.import_export_accounting.Repositories.ProductRepository;
import renatius.import_export_accounting.Repositories.WarehouseProductRepository;
import renatius.import_export_accounting.Service.ProductService;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final WarehouseProductRepository warehouseProductRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product saveProduct(Product product) {
        if (product.getId() != null) {
            Product existing = productRepository.findById(product.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Товар не найден"));
        }

        if (productRepository.existsByProductCodeAndIdNot(product.getProductCode(),
                product.getId() != null ? product.getId() : -1L)) {
            throw new IllegalArgumentException("Товар с таким кодом уже существует");
        }

        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        if (warehouseProductRepository.existsByProductId(id)) {
            throw new IllegalStateException("Невозможно удалить товар, так как он присутствует на складах");
        }

        productRepository.deleteById(id);
    }

    @Override
    public List<Product> getExportProducts() {
        return productRepository.findByOperationType(OperationType.EXPORT);
    }

    @Override
    public List<Product> getImportProducts() {
        return productRepository.findByOperationType(OperationType.IMPORT);
    }

    @Override
    public Product findById(Long productId) {
        return productRepository.findById(productId).get();
    }

}
