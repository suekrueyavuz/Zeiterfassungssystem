package fhcampuswien.zeiterfassungssystem.service.jwt;

import fhcampuswien.zeiterfassungssystem.Enum.Role;
import fhcampuswien.zeiterfassungssystem.entity.AuftraggeberFirma;
import fhcampuswien.zeiterfassungssystem.entity.Mitarbeiter;
import fhcampuswien.zeiterfassungssystem.service.AuftraggeberFirmaService;
import fhcampuswien.zeiterfassungssystem.service.MitarbeiterService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {
    private final MitarbeiterService mitarbeiterService;
    private final AuftraggeberFirmaService auftraggeberFirmaService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Mitarbeiter mitarbeiter = mitarbeiterService.getMitarbeiterByUsername(username);
        if(mitarbeiter != null) {
            return new User(mitarbeiter.getUsername(), mitarbeiter.getPassword(), mitarbeiter.getAuthorities());
        } else {
            AuftraggeberFirma firma = auftraggeberFirmaService.getFirmaByUsername(username);
            return new User(firma.getUsername(), firma.getPassword(), getFirmaAuthorities());
        }
    }

    public Collection<? extends GrantedAuthority> getFirmaAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        var sga = new SimpleGrantedAuthority(Role.ROLE_FIRMA.name());
        authorities.add(sga);
        return authorities;
    }
}
