package renatius.import_export_accounting.Service.impl;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import renatius.import_export_accounting.Entity.Employee;
import renatius.import_export_accounting.Repositories.EmployeeRepository;
import renatius.import_export_accounting.Service.EmployeeService;

import java.util.Optional;


@AllArgsConstructor
@Service
@Transactional(rollbackFor = Exception.class)
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

    @Override
    public boolean excitingByUsernameNotId(String username, Long id) {
        return employeeRepository.excitingByUsernameNotId(username, id);
    }

    @Override
    public boolean excitingByEmailNotId(String email, Long id) {
        return employeeRepository.excitingByEmailNotId(email, id);
    }
}
