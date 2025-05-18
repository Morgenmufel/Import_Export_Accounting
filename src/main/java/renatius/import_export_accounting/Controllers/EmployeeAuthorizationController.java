package renatius.import_export_accounting.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import renatius.import_export_accounting.Entity.Employee;
import renatius.import_export_accounting.Service.EmployeeService;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeAuthorizationController {

    private final EmployeeService employeeService;

    private  final PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("employee", new Employee());
        return "EmpLoginPage";
    }
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("employee", new Employee());
        return "EmpRegistrationPage";
    }

    @PostMapping("/registerEmployee")
    public String registerEmployee(@ModelAttribute("employee") Employee employee, BindingResult bindingResult, Model model) {
        Employee emp1 = null;
        Optional <Employee> excitingEmpUsername = employeeService.findByUsername(employee.getUsername());
        if (excitingEmpUsername.isPresent()) {
            emp1 = excitingEmpUsername.get();
        }
        if(emp1 != null && emp1.getUsername() !=null && !emp1.getUsername().isEmpty()){
            model.addAttribute("alertMessage", "Никнейм уже занят. Выбирайте другой");
            return "redirect:/employee/register";
        }
        if(emp1 !=null && emp1.getEmail() !=null && !emp1.getEmail().isEmpty()){
            model.addAttribute("alertMessage", "Данный email уже используется. Выберите другой");
            return "redirect:/employee/register";
        }
        else{
            employee.setCreateAt(LocalDateTime.now());
            employee.setPasswordHash(passwordEncoder.encode(employee.getPasswordHash()));
            employeeService.saveEmp(employee);
        }
        return "redirect:/employee/login";
    }



}
