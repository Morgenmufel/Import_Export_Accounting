package renatius.import_export_accounting.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import renatius.import_export_accounting.Configurations.security.SecurityUtil;
import renatius.import_export_accounting.Entity.Employee;
import renatius.import_export_accounting.Entity.Warehouse;
import renatius.import_export_accounting.Service.EmployeeService;
import renatius.import_export_accounting.Service.WarehouseService;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/employee/warehouse_manager")
public class WarehouseManagerController {

    private final EmployeeService employeeService;

    private final WarehouseService warehouseService;

    @GetMapping("/home")
    public String homePage(Model model) {
        String username = SecurityUtil.getSessionUser();
        Employee employee = employeeService.findByUsername(username).get();
        model.addAttribute("employee", employee);
        List<Warehouse> warehouseList = warehouseService.findAllWarehouses();
        model.addAttribute("warehouseList", warehouseList);
        return "WarehouseManagerPage";
    }
}
