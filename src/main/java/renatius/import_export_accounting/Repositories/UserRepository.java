package renatius.import_export_accounting.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import renatius.import_export_accounting.Entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
