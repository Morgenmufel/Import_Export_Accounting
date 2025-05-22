package renatius.import_export_accounting.Service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import renatius.import_export_accounting.Entity.*;
import renatius.import_export_accounting.Entity.Enum.OperationType;
import renatius.import_export_accounting.Entity.Enum.RequestStatus;
import renatius.import_export_accounting.Repositories.*;
import renatius.import_export_accounting.Service.PdfGeneratorService;
import renatius.import_export_accounting.Service.RequestService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final PartnerRepository partnerRepository;
    private final ProductRepository productRepository;
    private final RequestRepository requestRepository;
    private final WarehouseProductRepository warehouseProductRepository;
    private final DocumentRepository documentRepository;
    private final PdfGeneratorService pdfGeneratorService;

    @Transactional
    public Request createRequest(Long partnerId, List<Long> productIds) {
        Partner partner = partnerRepository.findById(partnerId)
                .orElseThrow(() -> new RuntimeException("Partner not found"));

        List<Product> selectedProducts = productRepository.findAllById(productIds);

        Request request = new Request();
        request.setPartner(partner);
        for (Product product : selectedProducts) {
            product.setRequest(request);
        }
        productRepository.saveAll(selectedProducts);
        request.setStatus(RequestStatus.NEW);
        return requestRepository.save(request);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public void saveRequest(Request request) {
        requestRepository.save(request);
    }

    @Override
    public List<Request> getRequestsByPartnerId(Long partnerId) {
        return requestRepository.findByPartnerId(partnerId);
    }


    @Override
    public Page<Request> findByStatus(RequestStatus requestStatus, Pageable pageable) {
        return requestRepository.findByStatus(requestStatus, pageable);
    }

    //TODO
    @Override
    public void approveRequest(Long requestId) {
        Request request = requestRepository.findWithProductsById(requestId)
                .orElseThrow(() -> new RuntimeException("Заявка не найдена"));

        Map<Product, Integer> products = request.getProducts();

        if (request.getType() == OperationType.EXPORT && !isExportPossible(requestId)) {
            throw new IllegalStateException("Недостаточно товара на складах для выполнения экспорта.");
        }

        switch (request.getType()) {
            case EXPORT -> handleExport(products);
            case IMPORT -> handleImport(products);
        }

        // Устанавливаем статус
        request.setStatus(RequestStatus.APPROVED);

        String basePath = "D:\\DOCSFORACCOUNTING(PROGSP)";
        File dir = new File(basePath);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (!created) {
                throw new RuntimeException("Не удалось создать директорию для документов: " + basePath);
            }
        }
        String fileName = "request-" + request.getId() + ".pdf";
        String fullPath = basePath + File.separator + fileName;

        // Создание PDF-документа
        byte[] pdfBytes = pdfGeneratorService.generateRequestDocument(request); // реализуем отдельно

        try (FileOutputStream fos = new FileOutputStream(fullPath)) {
            fos.write(pdfBytes);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сохранении PDF файла: " + fullPath, e);
        }

        Document document = new Document();
        document.setFileAsArrayOfBytes(pdfBytes);
        document.setFilePath(fullPath);
        document.setDocType("PDF");
        document.setPartner(request.getPartner());
        document.setRequest(request);
        documentRepository.save(document);
        request.setDocument(document);
        requestRepository.save(request);

    }

        //TODO
    @Override
    public void rejectRequest(Long id) {

    }

    public boolean isExportPossible(Long requestId) {
        Request request = requestRepository.findWithProductsById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        if (request.getType() != OperationType.EXPORT) {
            return true;
        }

        Map<Product, Integer> requestedProducts = request.getProducts();
        List<Object[]> available = warehouseProductRepository.findTotalQuantitiesByProducts(requestedProducts.keySet());

        Map<Long, Long> availableMap = available.stream()
                .collect(Collectors.toMap(
                        o -> (Long) o[0],
                        o -> (Long) o[1]
                ));

        for (Map.Entry<Product, Integer> entry : requestedProducts.entrySet()) {
            Long productId = entry.getKey().getId();
            int requestedQty = entry.getValue();
            long availableQty = availableMap.getOrDefault(productId, 0L);

            if (availableQty < requestedQty) {
                return false;
            }
        }

        return true;
    }

    private void handleImport(Map<Product, Integer> products) {
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            Product product = entry.getKey();
            int qtyToAdd = entry.getValue();

            // Ищем хоть одну запись продукта на складе
            List<WarehouseProduct> existing = warehouseProductRepository
                    .findByProductId(product.getId());

            if (!existing.isEmpty()) {
                WarehouseProduct wp = existing.get(0); // на любой склад
                wp.setQuantity(wp.getQuantity() + qtyToAdd);
                wp.setUpdatedAt(LocalDateTime.now());
                warehouseProductRepository.save(wp);
            } else {
                // Если вообще нет — выбрасываем или создаём новую запись (на конкретный склад, если известно)
                throw new RuntimeException("Продукт " + product.getName() + " не найден ни на одном складе");
            }
        }
    }

    private void handleExport(Map<Product, Integer> products) {
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            Product product = entry.getKey();
            int neededQty = entry.getValue();

            // Получаем склады, где есть этот продукт
            List<WarehouseProduct> available = warehouseProductRepository
                    .findByProductIdOrderByQuantityDesc(product.getId());

            for (WarehouseProduct wp : available) {
                if (neededQty == 0) break;

                int availableQty = wp.getQuantity();
                if (availableQty == 0) continue;

                int toDeduct = Math.min(availableQty, neededQty);
                wp.setQuantity(availableQty - toDeduct);
                wp.setUpdatedAt(LocalDateTime.now());
                warehouseProductRepository.save(wp);

                neededQty -= toDeduct;
            }

            if (neededQty > 0) {
                throw new IllegalStateException("Недостаточно товара для экспорта: " + product.getName());
            }
        }
    }

}

