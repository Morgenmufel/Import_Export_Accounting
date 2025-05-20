package renatius.import_export_accounting.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import renatius.import_export_accounting.Configurations.security.SecurityUtil;
import renatius.import_export_accounting.Entity.Employee;
import renatius.import_export_accounting.Entity.Warehouse;
import renatius.import_export_accounting.Service.EmployeeService;
import renatius.import_export_accounting.Service.WarehouseService;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/employee/warehouse")
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
        int count = warehouseList.size();
        model.addAttribute("totalWarehouses", count);
        return "WarehouseManagerPage";
    }

    @GetMapping("/addPage")
    public String addNewWarehousePage(Model model) {
        String username = SecurityUtil.getSessionUser();
        Employee employee = employeeService.findByUsername(username).get();
        model.addAttribute("employee", employee);
        Warehouse warehouse = new Warehouse();
        model.addAttribute("warehouse", warehouse);
        return "AddWarehousePage";
    }

    @PostMapping("/add")
    public String addNewWarehouse(
            @ModelAttribute("warehouse") Warehouse warehouse,
            BindingResult bindingResult, Model model) {

        Warehouse warehouse1 = null;
        Optional <Warehouse> excitingWarehouse = warehouseService.findWarehouseByName(warehouse.getName());
        if (excitingWarehouse.isPresent()) {
            warehouse1 = excitingWarehouse.get();
        }
        if(warehouse1 != null && warehouse1.getName()!= null && !warehouse1.getName().isEmpty()) {
            model.addAttribute("alertMessage", "Данное название уже используется. Выберите другое.");
            model.addAttribute("warehouse", warehouse);
            return "AddWarehousePage";
        }
        if(warehouse1 != null && warehouse1.getAddress()!= null && !warehouse1.getAddress().isEmpty()) {
            model.addAttribute("alertMessage", "Данный адрес уже используется. Выберите другое.");
            model.addAttribute("warehouse", warehouse);
            return "AddWarehousePage";
        }
        if(bindingResult.hasErrors()){
            model.addAttribute("alertMessage", "Возникли ошибки в форме");
            model.addAttribute("warehouse", warehouse);
            return "AddWarehousePage";
        }
        else{
            warehouseService.saveWarehouse(warehouse);
        }
        return "redirect:/employee/warehouse/home";

    }

    @PostMapping("/delete/{id}")
    public String deleteWarehouse(@PathVariable long id, Model model){
        Warehouse excitingWarehouse = warehouseService.findWarehouseById(id).get();
        if(excitingWarehouse == null) {
            model.addAttribute("alertMessage", "Такого склада не существует");
            return "WarehouseManagerPage";
        }
        else{
            warehouseService.deleteWarehouseById(id);
            return "redirect:/employee/warehouse/home";
        }
    }

    @GetMapping("/editPage/{id}")
    public String editWarehousePage(@PathVariable long id,Model model) {
        Warehouse warehouse = warehouseService.findWarehouseById(id).get();
        if (warehouse == null) {
            model.addAttribute("alertMessage", "Такого склада не существует");
            return "WarehouseManagerPage";
        }
        else {
            model.addAttribute("warehouse", warehouse);
            return "EditWarehousePage";
        }
    }


    @GetMapping("/{id}")
    public String oneWarehousePage(@PathVariable long id, Model model) {
        Warehouse warehouse1 = null;
        model.addAttribute("employee", employeeService.findByUsername(SecurityUtil.getSessionUser()).get());
        Optional<Warehouse> warehouse = warehouseService.findWarehouseById(id);
        if (warehouse.isPresent()){
            warehouse1 = warehouse.get();
        }
        model.addAttribute("warehouse", warehouse1);
        return "ListOfProductsOnWarehousePage";
    }

    //TODO допилить изменения
    @PostMapping("/edit")
    public String editWarehouse(@ModelAttribute("warehouse") Warehouse updatedWarehouse,
                                BindingResult bindingResult,
                                Model model, @RequestParam ("originalId") Long originalId) {

        Warehouse existingWarehouse = warehouseService.findWarehouseById(originalId)
                .orElseThrow(() -> new IllegalArgumentException("Склад не найден"));

        if (!existingWarehouse.getName().equals(updatedWarehouse.getName())) {
            if (warehouseService.excitingWarehouseByName(updatedWarehouse.getName(), originalId)) {
                model.addAttribute("alertMessage", "Склад с таким названием уже существует");
                model.addAttribute("warehouse", existingWarehouse);
                return "EditWarehousePage";
            }
        }

        if (!existingWarehouse.getAddress().equals(updatedWarehouse.getAddress())) {
            if (warehouseService.excitingWarehouseByAddress(updatedWarehouse.getAddress(), originalId)) {
                model.addAttribute("alertMessage", "Склад с таким адресом уже существует");
                model.addAttribute("warehouse", existingWarehouse); // Возвращаем оригинальные данные
                return "EditWarehousePage";
            }
        }

        if (bindingResult.hasErrors()){
            model.addAttribute("alertMessage", "В форме возникли ошибки");
            return "redirect:/employee/warehouse/editPage/" + originalId;
        }


        existingWarehouse.setName(updatedWarehouse.getName());
        existingWarehouse.setAddress(updatedWarehouse.getAddress());
        existingWarehouse.setManager(updatedWarehouse.getManager());

        warehouseService.updateWarehouse(existingWarehouse);

        return "redirect:/employee/warehouse/home";
    }
}
