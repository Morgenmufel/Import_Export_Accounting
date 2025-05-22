package renatius.import_export_accounting.Controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import renatius.import_export_accounting.Configurations.security.SecurityUtil;
import renatius.import_export_accounting.Entity.Document;
import renatius.import_export_accounting.Entity.Partner;
import renatius.import_export_accounting.Service.DocumentService;
import renatius.import_export_accounting.Service.PartnerService;
import org.springframework.ui.Model;

import java.security.Principal;

@Controller
@AllArgsConstructor
@RequestMapping("/partner")
public class PartnerProfileController {

    private final PartnerService partnerService;

    private final PasswordEncoder passwordEncoder;

    private final DocumentService documentService;

    @GetMapping("/edit")
    public String editAccount(Model model) {
        String email = SecurityUtil.getSessionUser();
        Partner partner = partnerService.findByEmail(email).get();
        model.addAttribute("partner", partner);
        return "EditPartnerPage";
    }

    @GetMapping("/change-password")
    public String changePassword(Model model) {

        String currentPassword = new String();

        String newPassword = new String();

        String confirmPassword = new String();
        model.addAttribute("currentPassword", currentPassword);
        model.addAttribute("newPassword", newPassword);
        model.addAttribute("confirmPassword", confirmPassword);

        return "PartnerChangePassword";
    }

    @PostMapping("/edit")
    public String updateProfile(
            @ModelAttribute("partner") Partner partner,
            BindingResult result,
            Principal principal) {

        Partner currentPartner = partnerService.findByEmail(principal.getName()).get();

        if (!partner.getEmail().equals(currentPartner.getEmail()) &&
                partnerService.existsByEmail(partner.getEmail())) {
            result.rejectValue("email", "error.email", "Этот email уже используется");
        }

        if (!partner.getName().equals(currentPartner.getName()) &&
                partnerService.existsByName(partner.getName())) {
            result.rejectValue("name", "error.name", "Это ФИО уже занято");
        }

        if (!partner.getNameOfCompany().equals(currentPartner.getNameOfCompany()) &&
                partnerService.existsByNameOfCompany(partner.getNameOfCompany())) {
            result.rejectValue("nameOfCompany", "error.nameOfCompany", "Это название компании уже используется");
        }

        if (result.hasErrors()) {
            return "EditProfilePage";
        }

        partnerService.savePartner(partner);

        return "redirect:/partner/profile";
    }


    @PostMapping("/change-password")
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 Principal principal,
                                 Model model) {
        // Получаем текущего пользователя по логину (email)
        String email = principal.getName();
        Partner partner = partnerService.findByEmail(email).get();

        if (!passwordEncoder.matches(currentPassword, partner.getPasswordHash())) {
            model.addAttribute("errorOldPassword", "Старый пароль неверен");
            model.addAttribute("currentPassword", currentPassword);
            model.addAttribute("newPassword", newPassword);
            model.addAttribute("confirmPassword", confirmPassword);
            return "PartnerChangePassword";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("errorConfirmPassword", "Пароли не совпадают");
            model.addAttribute("currentPassword", currentPassword);
            model.addAttribute("newPassword", newPassword);
            model.addAttribute("confirmPassword", confirmPassword);
            return "PartnerChangePassword";
        }

        partner.setPasswordHash(passwordEncoder.encode(newPassword));
        partnerService.savePartner(partner);

        model.addAttribute("successMessage", "Пароль успешно изменён");
        return "redirect:/partner/profile";
    }

    //TODO скачка файла
    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable Long id) {
        Document doc = documentService.getDocument(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename("document_" + id + ".pdf").build());

        return new ResponseEntity<>(doc.getFileAsArrayOfBytes(), headers, HttpStatus.OK);
    }
}
