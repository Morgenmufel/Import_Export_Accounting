package renatius.import_export_accounting.Service;

import renatius.import_export_accounting.Entity.Product;

import java.util.Map;

public interface BasketService {
    void addProductToExport(Long partnerId, Long productId, int quantity);
    void addProductToImport(Long partnerId, Long productId, int quantity);
    Map<Product, Integer> getExportBasket(Long partnerId);
    Map<Product, Integer> getImportBasket(Long partnerId);
    void clearExportBasket(Long partnerId);
    void clearImportBasket(Long partnerId);
}
