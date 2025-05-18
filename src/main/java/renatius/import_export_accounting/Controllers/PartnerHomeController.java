package renatius.import_export_accounting.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import renatius.import_export_accounting.Entity.Request;
import renatius.import_export_accounting.Service.PartnerService;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/partner")
@RequiredArgsConstructor
public class PartnerHomeController {

    private final PartnerService partnerService;

    @GetMapping("/homePage")
    public String homePage(Model model) {
        model.addAttribute("request", new Request());
        return "PartnerMainPage";
    }
}
