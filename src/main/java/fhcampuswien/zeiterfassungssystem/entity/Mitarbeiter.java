package fhcampuswien.zeiterfassungssystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fhcampuswien.zeiterfassungssystem.Enum.AusgeliehenStatus;
import fhcampuswien.zeiterfassungssystem.Enum.Role;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Mitarbeiter {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private String forename;

    @Column(nullable = false)
    private String surname;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    @Enumerated(EnumType.STRING)
    private AusgeliehenStatus ausgeliehenStatus;

    @JsonIgnore
    @OneToMany(mappedBy = "mitarbeiter", cascade = CascadeType.ALL)
    private List<AusgelieheneMitarbeiter> ausgelieheneMitarbeiter;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        var sga = new SimpleGrantedAuthority(this.role.name());
        authorities.add(sga);
        return authorities;
    }

}
