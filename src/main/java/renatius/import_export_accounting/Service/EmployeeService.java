package renatius.import_export_accounting.Service;

import renatius.import_export_accounting.Entity.Employee;

import java.util.Optional;

public interface EmployeeService {
     Optional <Employee> findByUsername(String username);

     Employee findById(long id);

     void saveEmp(Employee employee);
}
