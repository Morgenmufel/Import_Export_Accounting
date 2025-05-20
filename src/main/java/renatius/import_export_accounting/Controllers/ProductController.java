package renatius.import_export_accounting.Controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import renatius.import_export_accounting.Entity.Product;
import renatius.import_export_accounting.Service.ProductService;
import renatius.import_export_accounting.Service.WarehouseService;
import org.springframework.ui.Model;
import renatius.import_export_accounting.Entity.WarehouseProduct;
import java.util.*;

@Controller
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    private final WarehouseService warehouseService;

    @GetMapping("/add/{warehouseId}")
    public String showAddForm(@PathVariable("warehouseId") long id,Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("warehouseId", id);
        return "AddWarehouseProduct";
    }

    @PostMapping("/add")
    public String addProduct(@Valid @ModelAttribute("product") Product product,
                             BindingResult result,
                             @RequestParam long warehouseId,
                             RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "AddWarehouseProduct";
        }

        try {
            productService.saveProduct(product);
            redirectAttributes.addFlashAttribute("successMessage", "Товар успешно добавлен");
            return "redirect:/employee/warehouse/" + warehouseId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при добавлении товара: " + e.getMessage());
            return "redirect:/employee/warehouse/" + warehouseId;
        }
    }

    @GetMapping("/edit/{warehouseId}/{id}")
    public String showEditForm(@PathVariable("warehouseId") Long warehouseId,@PathVariable("id") Long id, Model model) {
        try {
            Product product = productService.getProductById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Товар с ID " + id + " не найден"));

            List<WarehouseProduct> warehouseProducts = warehouseService.getProductWarehouseInfo(id);

            model.addAttribute("product", product);
            model.addAttribute("warehouseProducts", warehouseProducts);
            return "EditWarehouseProduct";
        } catch (Exception e) {
            model.addAttribute("alertMessage", "Ошибка изменения товара");
            return "redirect:/employee/warehouse/" + warehouseId;
        }
    }

    @PostMapping("/edit")
    public String updateProduct(@Valid @ModelAttribute("product") Product product,
                                BindingResult result,
                                RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "products/edit-product";
        }

        try {
            productService.saveProduct(product);
            redirectAttributes.addFlashAttribute("successMessage", "Товар успешно обновлен");
            return "redirect:/products";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при обновлении товара: " + e.getMessage());
            return "redirect:/products/edit/" + product.getId();
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("successMessage", "Товар успешно удален");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при удалении товара: " + e.getMessage());
        }
        return "redirect:/products";
    }
}
