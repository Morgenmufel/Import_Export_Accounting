package renatius.import_export_accounting.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import renatius.import_export_accounting.Entity.Employee;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Employee findById(long employeeId);

    Optional<Employee> findByUsername(String username);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END " +
            "FROM Employee e WHERE e.username = :username AND e.id <> :id")
    boolean excitingByUsernameNotId(@Param("username") String username, @Param("id") Long id);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END " +
            "FROM Employee e WHERE e.email = :email AND e.id <> :id")
    boolean excitingByEmailNotId(@Param("email") String email, @Param("id") Long id);
}
