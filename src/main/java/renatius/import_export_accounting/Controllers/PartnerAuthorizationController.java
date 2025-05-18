package renatius.import_export_accounting.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import renatius.import_export_accounting.Entity.Enum.UserRole;
import renatius.import_export_accounting.Entity.Partner;
import renatius.import_export_accounting.Service.PartnerService;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/partner")
public class PartnerAuthorizationController {

    private final PartnerService partnerService;

    private  final PasswordEncoder passwordEncoder;

    @RequestMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("partner", new Partner());
        return "PartnerLoginPage";
    }
    @RequestMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("partner", new Partner());
        return "PartnerRegistrationPage";
    }

    @PostMapping("/registerPartner")
    public String registerPartner(@ModelAttribute("partner") Partner partner, BindingResult bindingResult, Model model) {
        Partner partner1 = null;
        Optional<Partner> excitingPartnerEmail = partnerService.findByEmail(partner.getEmail());
        if (excitingPartnerEmail.isPresent()) {
            partner1 = excitingPartnerEmail.get();
        }
        if(partner1 != null && partner1.getEmail() != null && !partner1.getEmail().isEmpty()) {
            model.addAttribute("alertMessage", "Этот email уже используется. Выберите другой.");
            return "PartnerRegistrationPage";
        }
        if (partner1 != null && partner1.getTaxNumber() != null && !partner1.getTaxNumber().isEmpty()) {
            model.addAttribute("alertMessage", "Этот идентификационный номер уже используется. Выберите другой.");
            return "PartnerRegistrationPage";
        }
        if (partner1 != null && partner1.getNameOfCompany() != null && !partner1.getNameOfCompany().isEmpty()) {
            model.addAttribute("alertMessage", "Эта компания уже зарегистрирована. Выберите другую.");
            return "PartnerRegistrationPage";
        }
        if(bindingResult.hasErrors()) {
            model.addAttribute("alertMessage", "Возникли непредвиденные ошибки.");
            model.addAttribute("partner", partner);
            return "PartnerRegistrationPage";
        }
        else {
            partner.setPasswordHash(passwordEncoder.encode(partner.getPasswordHash()));
            partner.setRole(UserRole.ROLE_PARTNER);
            partnerService.savePartner(partner);
        }
        return "redirect:/partner/login";
    }

}
