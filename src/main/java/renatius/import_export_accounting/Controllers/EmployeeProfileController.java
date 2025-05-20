package renatius.import_export_accounting.Controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import renatius.import_export_accounting.Configurations.security.SecurityUtil;
import renatius.import_export_accounting.Entity.Employee;
import renatius.import_export_accounting.Service.EmployeeService;

import java.security.Principal;
import java.util.Collections;

@Controller
@AllArgsConstructor
@RequestMapping("/employee")
public class EmployeeProfileController {

    private final EmployeeService employeeService;

    private final PasswordEncoder passwordEncoder;

    @GetMapping("/profile")
    public String profilePage(Model model) {
        String username = SecurityUtil.getSessionUser();
        Employee employee = employeeService.findByUsername(username).get();
        model.addAttribute("employee", employee);
        return "EmployeeProfile";
    }

    @GetMapping("/profile/edit")
    public String editProfilePage(Model model) {
        String username = SecurityUtil.getSessionUser();
        Employee employee = employeeService.findByUsername(username).get();
        model.addAttribute("employee", employee);
        return "EditProfilePage";
    }

    @PostMapping("/profile/edit")
    public String editProfile(@ModelAttribute("employee") Employee employee, BindingResult bindingResult, Model model) {
        String username = SecurityUtil.getSessionUser();
        Employee employee1 = employeeService.findByUsername(username).get();
        if(!employee1.getUsername().equals(employee.getUsername())) {
            if(employeeService.excitingByUsernameNotId(employee.getUsername(), employee1.getId())){
                model.addAttribute("alertMessage", "Такой никнейм уже существует");
                model.addAttribute("employee", employee);
                return "EditProfilePage";
            }
        }
        if(!employee1.getEmail().equals(employee.getEmail())) {
            if(employeeService.excitingByEmailNotId(employee.getEmail(), employee1.getId())){
                model.addAttribute("alertMessage", "Такой email уже существует");
                model.addAttribute("employee", employee);
                return "EditProfilePage";
            }
        }
        if(bindingResult.hasErrors()) {
            model.addAttribute("alertMessage", "В форме возникли ошибки");
            model.addAttribute("employee", employee);
            return "EditProfilePage";
        }

        employee1.setEmail(employee.getEmail());
        employee1.setUsername(employee.getUsername());
        employeeService.saveEmp(employee1);

        return "redirect:/employee/profile";
    }

    @GetMapping("/profile/changePassword")
    public String changePasswordPage(Model model) {
        String username = SecurityUtil.getSessionUser();
        Employee employee = employeeService.findByUsername(username).get();
        model.addAttribute("employee", employee);
        return "ChangePasswordPage";
    }

    @PostMapping("/profile/changePassword")
    public String changePassword(
            @RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        String username = principal.getName();
        Employee employee = employeeService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Проверка текущего пароля
        if (!passwordEncoder.matches(currentPassword, employee.getPasswordHash())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Текущий пароль введен неверно");
            return "redirect:/employee/profile/changePassword";
        }

        // Проверка, что новый пароль и подтверждение совпадают
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Новый пароль и подтверждение не совпадают");
            return "redirect:/employee/profile/changePassword";
        }

        // Проверка сложности пароля (минимум 8 символов)
        if (newPassword.length() < 8) {
            redirectAttributes.addFlashAttribute("errorMessage", "Пароль должен содержать минимум 8 символов");
            return "redirect:/employee/profile/changePassword";
        }

        // Обновление пароля
        employee.setPasswordHash(passwordEncoder.encode(newPassword));
        employeeService.saveEmp(employee);

        redirectAttributes.addFlashAttribute("successMessage", "Пароль успешно изменен");
        return "redirect:/employee/profile";
    }

    // AJAX-метод для проверки текущего пароля
    @PostMapping("/profile/checkCurrentPassword")
    @ResponseBody
    public ResponseEntity<?> checkCurrentPassword(
            @RequestParam String currentPassword,
            Principal principal) {

        String username = principal.getName();
        Employee employee = employeeService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        boolean isValid = passwordEncoder.matches(currentPassword, employee.getPasswordHash());

        return ResponseEntity.ok().body(Collections.singletonMap("isValid", isValid));
    }
}
