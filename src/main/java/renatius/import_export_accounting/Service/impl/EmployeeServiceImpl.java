package renatius.import_export_accounting.Service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import renatius.import_export_accounting.Entity.Employee;
import renatius.import_export_accounting.Repositories.EmployeeRepository;
import renatius.import_export_accounting.Service.EmployeeService;

import java.util.Optional;


@RequiredArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public Optional<Employee> findByUsername(String username) {
        return employeeRepository.findByUsername(username);
    }

    @Override
    public Employee findById(long id) {
        return employeeRepository.findById(id);
    }

    @Override
    public void saveEmp(Employee employee) {
        employeeRepository.save(employee);
    }
}
