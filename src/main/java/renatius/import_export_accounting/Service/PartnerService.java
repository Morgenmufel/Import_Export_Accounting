package renatius.import_export_accounting.Service;

import renatius.import_export_accounting.Entity.Employee;
import renatius.import_export_accounting.Entity.Partner;

import java.util.Optional;


public interface PartnerService {

    Optional<Partner> findByEmail(String email);

    Partner findById(long id);

    void savePartner(Partner partner);

    boolean existsByEmail(String email);

    boolean existsByName(String name);

    boolean existsByNameOfCompany(String nameOfCompany);
}
