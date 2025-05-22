package renatius.import_export_accounting.Controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import renatius.import_export_accounting.Entity.Product;
import renatius.import_export_accounting.Entity.Warehouse;
import renatius.import_export_accounting.Entity.WarehouseProductId;
import renatius.import_export_accounting.Service.ProductService;
import renatius.import_export_accounting.Service.WarehouseProductService;
import renatius.import_export_accounting.Service.WarehouseService;
import org.springframework.ui.Model;
import renatius.import_export_accounting.Entity.WarehouseProduct;

import java.time.LocalDateTime;
import java.util.*;

@Controller
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final WarehouseProductService warehouseProductService;

    private final ProductService productService;

    private final WarehouseService warehouseService;

    @GetMapping("/add/{warehouseId}")
    public String showAddForm(@PathVariable("warehouseId") long id,Model model) {
        model.addAttribute("warehouseProduct", new WarehouseProduct());
        model.addAttribute("product", new Product());
        model.addAttribute("warehouseId", id);
        return "AddWarehouseProduct";
    }

    @PostMapping("/add")
    public String addProduct(@ModelAttribute("product") Product product,
                             BindingResult result,
                             @RequestParam long warehouseId,
                             RedirectAttributes redirectAttributes, @ModelAttribute("warehouseProduct") WarehouseProduct warehouseProduct) {

        Warehouse warehouse = warehouseService.findWarehouseById(warehouseId).get();

        // Проверка на уникальность названия товара
        if (warehouseProductService.existsByName(product.getName(), warehouseId)) {
            result.rejectValue("name", "error.product", "Товар с таким названием уже существует");
        }

        // Проверка на уникальность кода товара
        if (warehouseProductService.existsByProductCode(product.getProductCode(), warehouseId)) {
            result.rejectValue("productCode", "error.product", "Товар с таким кодом уже существует");
        }

        if (result.hasErrors()) {
            return "AddWarehouseProduct";
        }

        try {
            productService.saveProduct(product);
            WarehouseProduct warehouseProduct1 = new WarehouseProduct();
            warehouseProduct1.setProduct(product);
            warehouseProduct1.setWarehouse(warehouse);
            warehouseProduct1.setQuantity(warehouseProduct.getQuantity());
            warehouseProduct1.setUpdatedAt(LocalDateTime.now());

            WarehouseProductId id = new WarehouseProductId();
            id.setWarehouseId(warehouse.getId());
            id.setProductId(product.getId());
            warehouseProduct1.setId(id);

            warehouseProductService.saveWarehouseProduct(warehouseProduct1);
            redirectAttributes.addFlashAttribute("successMessage", "Товар успешно добавлен");
            return "redirect:/employee/warehouse/" + warehouseId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при добавлении товара: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/employee/warehouse/" + warehouseId;
        }
    }

    @GetMapping("/edit/{warehouseId}/{productId}")
    public String showEditForm(
            @PathVariable("warehouseId") Long warehouseId,
            @PathVariable("productId") Long productId,
            Model model) {
        try {
            Product product = productService.getProductById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Товар с ID " + productId + " не найден"));
            WarehouseProduct wp;
            WarehouseProduct warehouseProduct = warehouseProductService.findByProductId(productId);
            if(warehouseProduct == null) {
                wp = new WarehouseProduct();
                wp.setId(new WarehouseProductId(warehouseId, productId));
                wp.setQuantity(0);
            }

            model.addAttribute("product", product);
            model.addAttribute("warehouseProduct", warehouseProduct);
            model.addAttribute("warehouseId", warehouseId);
            return "EditWarehouseProduct";
        } catch (Exception e) {
            model.addAttribute("alertMessage", "Ошибка изменения товара");
            return "redirect:/employee/warehouse/" + warehouseId;
        }
    }

    @PostMapping("/edit")
    public String updateProduct(
            @Valid @ModelAttribute("product") Product product,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            @RequestParam("warehouseId") Long warehouseId,
            @RequestParam("quantity") Integer quantity) {

        if (result.hasErrors()) {
            return "redirect:/products/edit/" + warehouseId + "/" + product.getId();
        }

        try {
            // Проверка уникальности имени и кода товара
            if (warehouseProductService.existsByNameAndIdNot(product.getName(), product.getId() ,warehouseId)) {
                result.rejectValue("name", "error.product", "Товар с таким названием уже существует");
            }
            if (warehouseProductService.existsByProductCodeAndIdNot(product.getProductCode(), product.getId() ,warehouseId)) {
                result.rejectValue("productCode", "error.product", "Товар с таким кодом уже существует");
            }
            if (result.hasErrors()) {
                return "redirect:/products/edit/" + warehouseId + "/" + product.getId();
            }

            Product savedProduct = productService.saveProduct(product);

            WarehouseProduct warehouseProduct = warehouseProductService.findByProductId(savedProduct.getId());

            if (warehouseProduct == null) {
                WarehouseProductId id = new WarehouseProductId(warehouseId, savedProduct.getId());
                warehouseProduct = new WarehouseProduct();
                warehouseProduct.setId(id);
            } else {
            }

            warehouseProduct.setProduct(savedProduct);
            warehouseProduct.setQuantity(quantity);
            warehouseProduct.setUpdatedAt(LocalDateTime.now());
            warehouseProduct.setWarehouse(warehouseService.findWarehouseById(warehouseId)
                    .orElseThrow(() -> new IllegalArgumentException("Склад не найден")));

            warehouseProductService.saveWarehouseProduct(warehouseProduct);

            redirectAttributes.addFlashAttribute("successMessage", "Товар успешно обновлен");
            return "redirect:/employee/warehouse/" + warehouseId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при обновлении товара: " + e.getMessage());
            e.printStackTrace();
            return "redirect:/products/edit/" + warehouseId + "/" + product.getId();
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, @RequestParam long warehouseId ,RedirectAttributes redirectAttributes) {
        try {
            warehouseProductService.deleteWarehouseProductByProductId(id);
            if (!warehouseProductService.existsByProductId(id)) {
                productService.deleteProduct(id);
            }
            redirectAttributes.addFlashAttribute("successMessage", "Товар успешно удален");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при удалении товара: " + e.getMessage());
        }
        return "redirect:/employee/warehouse/" + warehouseId;
    }
}
