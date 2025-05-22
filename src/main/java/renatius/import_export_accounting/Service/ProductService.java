package renatius.import_export_accounting.Service;


import renatius.import_export_accounting.Entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

     List<Product> getAllProducts();

     Optional<Product> getProductById(Long id);

     Product saveProduct(Product product);

     void deleteProduct(Long id);

     List<Product> getExportProducts();

    List<Product> getImportProducts();

    Product findById(Long productId);
}
