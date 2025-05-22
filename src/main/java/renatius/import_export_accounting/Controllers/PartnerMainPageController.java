package renatius.import_export_accounting.Controllers;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import renatius.import_export_accounting.Configurations.security.SecurityUtil;
import renatius.import_export_accounting.Entity.Enum.OperationType;
import renatius.import_export_accounting.Entity.Enum.RequestStatus;
import renatius.import_export_accounting.Entity.Partner;
import renatius.import_export_accounting.Entity.Product;
import renatius.import_export_accounting.Entity.Request;
import renatius.import_export_accounting.Service.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.ui.Model;

@Controller
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
@RequestMapping("/partner")
public class PartnerMainPageController {

    private final ProductService productService;
    private final BasketService basketService;
    private final PartnerService partnerService;
    private final RequestService requestService;

    @GetMapping("/mainPage")
    public String showPartnerMainPage(Model model, Principal principal) {
        Partner partner = partnerService.findByEmail(principal.getName()).get();

        model.addAttribute("userFullName", partner.getName());
        model.addAttribute("userEmail", partner.getEmail());
        model.addAttribute("userRole", partner.getRole().name());

        model.addAttribute("exportProducts", productService.getExportProducts());
        model.addAttribute("importProducts", productService.getImportProducts());

        model.addAttribute("selectedExportProducts", basketService.getExportBasket(partner.getId()));
        model.addAttribute("selectedImportProducts", basketService.getImportBasket(partner.getId()));

        return "PartnerMainPage";
    }

    @PostMapping("/export/add")
    public String addExportProduct(@RequestParam Long productId,
                                   @RequestParam int quantity,
                                   Principal principal) {
        Long partnerId = partnerService.findByEmail(principal.getName()).get().getId();
        basketService.addProductToExport(partnerId, productId, quantity);
        return "redirect:/partner/mainPage#export";
    }

    @PostMapping("/import/add")
    public String addImportProduct(@RequestParam Long productId,
                                   @RequestParam int quantity,
                                   Principal principal) {
        Long partnerId = partnerService.findByEmail(principal.getName()).get().getId();
        basketService.addProductToImport(partnerId, productId, quantity);
        return "redirect:/partner/mainPage#import";
    }

    @PostMapping("/export/clear")
    public String clearExportBasket(Principal principal) {
        Long partnerId = partnerService.findByEmail(principal.getName()).get().getId();
        basketService.clearExportBasket(partnerId);
        return "redirect:/partner/mainPage#export";
    }

    @PostMapping("/import/clear")
    public String clearImportBasket(Principal principal) {
        Long partnerId = partnerService.findByEmail(principal.getName()).get().getId();
        basketService.clearImportBasket(partnerId);
        return "redirect:/partner/mainPage#import";
    }

    @PostMapping("/request/export")
    public String submitExportRequest(Principal principal, RedirectAttributes redirectAttributes,
                                      @RequestParam("productIds") List<Long> productIds,
                                      @RequestParam("quantities") List<Integer> quantities) {
        Partner partner = partnerService.findByEmail(principal.getName()).orElseThrow(() ->
                new IllegalArgumentException("Partner not found"));

        Map<Product, Integer> basket = basketService.getExportBasket(partner.getId());

        if (productIds.size() != quantities.size()) {
            throw new IllegalArgumentException("Список товаров и количеств не совпадает");
        }

        if (basket.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Корзина экспорта пуста.");
            return "redirect:/partner/mainPage#export";
        }

        Request request = new Request();
        request.setPartner(partner);
        request.setStatus(RequestStatus.NEW);
        request.setCreatedAt(LocalDateTime.now());
        request.setType(OperationType.EXPORT);

        // Добавляем товары из productIds и quantities
        for (int i = 0; i < productIds.size(); i++) {
            Long productId = productIds.get(i);
            Integer quantity = quantities.get(i);

            if (quantity == null || quantity <= 0) {
                redirectAttributes.addFlashAttribute("error", "Количество товара должно быть больше 0.");
                return "redirect:/partner/mainPage#export";
            }

            Product product = productService.findById(productId);
            if (product == null) {
                redirectAttributes.addFlashAttribute("error", "Товар с id " + productId + " не найден.");
                return "redirect:/partner/mainPage#export";
            }

            request.addProduct(product, quantity);
        }


        requestService.saveRequest(request);

        basketService.clearExportBasket(partner.getId());

        redirectAttributes.addFlashAttribute("success", "Заявка на экспорт успешно создана.");
        return "redirect:/partner/mainPage#export";
    }

    @PostMapping("/request/import")
    public String submitImportRequest(Principal principal,
                                      RedirectAttributes redirectAttributes,
                                      @RequestParam("productIds") List<Long> productIds,
                                      @RequestParam("quantities") List<Integer> quantities) {
        Partner partner = partnerService.findByEmail(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("Partner not found"));

        Map<Product, Integer> basket = basketService.getImportBasket(partner.getId());

        if (productIds.size() != quantities.size()) {
            throw new IllegalArgumentException("Список товаров и количеств не совпадает");
        }

        if (basket.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Корзина импорта пуста.");
            return "redirect:/partner/mainPage#import";
        }

        Request request = new Request();
        request.setPartner(partner);
        request.setStatus(RequestStatus.NEW);
        request.setCreatedAt(LocalDateTime.now());
        request.setType(OperationType.EXPORT);

        for (int i = 0; i < productIds.size(); i++) {
            Product product = productService.findById(productIds.get(i));
            Integer quantity = quantities.get(i);
            request.addProduct(product, quantity);
        }

        requestService.saveRequest(request);
        basketService.clearImportBasket(partner.getId());

        redirectAttributes.addFlashAttribute("success", "Заявка на импорт успешно создана.");
        return "redirect:/partner/mainPage#import";
    }

    @GetMapping("/profile")
    public String showProfile(Model model) {
        String email = SecurityUtil.getSessionUser();
        Partner partner = partnerService.findByEmail(email).get();
        model.addAttribute("partner", partner);
        List<Request> partnerRequests = requestService.getRequestsByPartnerId(partner.getId());
        model.addAttribute("partnerRequests", partnerRequests);
        return "PartnerProfilePage";
    }
}
