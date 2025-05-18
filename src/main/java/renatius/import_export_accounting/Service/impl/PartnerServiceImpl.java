package renatius.import_export_accounting.Service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import renatius.import_export_accounting.Entity.Partner;
import renatius.import_export_accounting.Repositories.PartnerRepository;
import renatius.import_export_accounting.Service.PartnerService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PartnerServiceImpl implements PartnerService {

    private final PartnerRepository partnerRepository;

    @Override
    public Optional<Partner> findByEmail(String email) {
        return partnerRepository.findByEmail(email);
    }

    @Override
    public Partner findById(long id) {
        return null;
    }

    @Override
    public void savePartner(Partner partner) {
        partnerRepository.save(partner);
    }
}
