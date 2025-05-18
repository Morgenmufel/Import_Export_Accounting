package renatius.import_export_accounting.Configurations.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import renatius.import_export_accounting.Entity.Partner;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class PartnerDetails implements UserDetails {
    private final Partner partner;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_PARTNER"));
    }

    @Override
    public String getPassword() {
        return partner.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return partner.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
