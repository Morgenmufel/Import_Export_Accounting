package renatius.import_export_accounting.Service;


import renatius.import_export_accounting.Entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    public List<Product> getAllProducts();

    public Optional<Product> getProductById(Long id);

    public Product saveProduct(Product product);

    public void deleteProduct(Long id);




}
