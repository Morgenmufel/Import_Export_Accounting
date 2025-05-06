package renatius.import_export_accounting.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import renatius.import_export_accounting.Entity.Partner;

public interface PartnerRepository extends JpaRepository<Partner, Long> {
}
