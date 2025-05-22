package renatius.import_export_accounting.Service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;
import renatius.import_export_accounting.Entity.Product;
import renatius.import_export_accounting.Repositories.ProductRepository;
import renatius.import_export_accounting.Service.BasketService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@SessionScope
public class BasketServiceImpl implements BasketService {

    private final ProductRepository productRepository;

    private final Map<Long, Map<Long, Integer>> exportBaskets = new HashMap<>();
    private final Map<Long, Map<Long, Integer>> importBaskets = new HashMap<>();

    public BasketServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void addProductToExport(Long partnerId, Long productId, int quantity) {
        exportBaskets
                .computeIfAbsent(partnerId, k -> new HashMap<>())
                .merge(productId, quantity, Integer::sum);
    }

    @Override
    public void addProductToImport(Long partnerId, Long productId, int quantity) {
        importBaskets
                .computeIfAbsent(partnerId, k -> new HashMap<>())
                .merge(productId, quantity, Integer::sum);
    }

    @Override
    public Map<Product, Integer> getExportBasket(Long partnerId) {
        return mapToProductMap(exportBaskets.getOrDefault(partnerId, Collections.emptyMap()));
    }

    @Override
    public Map<Product, Integer> getImportBasket(Long partnerId) {
        return mapToProductMap(importBaskets.getOrDefault(partnerId, Collections.emptyMap()));
    }

    @Override
    public void clearExportBasket(Long partnerId) {
        exportBaskets.remove(partnerId);
    }

    @Override
    public void clearImportBasket(Long partnerId) {
        importBaskets.remove(partnerId);
    }

    private Map<Product, Integer> mapToProductMap(Map<Long, Integer> idMap) {
        Map<Product, Integer> result = new LinkedHashMap<>();
        for (Map.Entry<Long, Integer> entry : idMap.entrySet()) {
            productRepository.findById(entry.getKey()).ifPresent(product ->
                    result.put(product, entry.getValue()));
        }
        return result;
    }
}

