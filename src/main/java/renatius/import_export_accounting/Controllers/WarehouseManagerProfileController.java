package renatius.import_export_accounting.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import renatius.import_export_accounting.Configurations.security.SecurityUtil;
import renatius.import_export_accounting.Entity.Employee;
import renatius.import_export_accounting.Service.EmployeeService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/employee")
public class WarehouseManagerProfileController {

    private final EmployeeService employeeService;

    @GetMapping("/profile")
    public String profilePage(Model model) {
        String username = SecurityUtil.getSessionUser();
        Employee employee = employeeService.findByUsername(username).get();
        model.addAttribute("employee", employee);
        return "EmployeeProfile";
    }
}
