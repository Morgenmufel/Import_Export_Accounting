package renatius.import_export_accounting.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import renatius.import_export_accounting.Entity.Document;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

}
