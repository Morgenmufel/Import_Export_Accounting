package renatius.import_export_accounting.Configurations.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import renatius.import_export_accounting.Entity.Partner;
import renatius.import_export_accounting.Repositories.PartnerRepository;


@Service
@RequiredArgsConstructor
public class PartnerDetailsService implements UserDetailsService {

    private final PartnerRepository partnerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Partner partner = partnerRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Partner not found"));
        return new PartnerDetails(partner);
    }
}
