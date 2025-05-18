package renatius.import_export_accounting.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import renatius.import_export_accounting.Entity.Request;


@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
}
