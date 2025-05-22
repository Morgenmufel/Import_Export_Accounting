package renatius.import_export_accounting.Controllers;


import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import renatius.import_export_accounting.Configurations.security.SecurityUtil;
import renatius.import_export_accounting.Entity.Employee;
import renatius.import_export_accounting.Entity.Enum.RequestStatus;
import renatius.import_export_accounting.Entity.Request;
import renatius.import_export_accounting.Service.EmployeeService;
import renatius.import_export_accounting.Service.RequestService;
import org.springframework.ui.Model;

@Controller
@AllArgsConstructor
@RequestMapping("/employee/ved")
public class VedRequestController {
    private final RequestService requestService;
    private final EmployeeService employeeService;

    @GetMapping("/requests")
    public String showRequests(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            Model model) {

        Page<Request> requests = requestService.findByStatus(
                RequestStatus.NEW,
                PageRequest.of(page, size, Sort.by("createdAt").descending())
        );

        String username = SecurityUtil.getSessionUser();
        Employee employee = employeeService.findByUsername(username).get();
        model.addAttribute("employee", employee);
        model.addAttribute("requests", requests);
        return "VADMainPage";
    }

    @PostMapping("/{id}/approve")
    public String approveRequest(@PathVariable Long id) {
        requestService.approveRequest(id);
        return "redirect:/partner/ved/requests";
    }

    @PostMapping("/{id}/reject")
    public String rejectRequest(@PathVariable Long id) {
        requestService.rejectRequest(id);
        return "redirect:/partner/ved/requests";
    }

}
