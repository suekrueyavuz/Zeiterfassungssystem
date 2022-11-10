package fhcampuswien.zeiterfassungssystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fhcampuswien.zeiterfassungssystem.Enum.Role;
import fhcampuswien.zeiterfassungssystem.Enum.Schicht;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class AuftraggeberFirma {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "auftraggeberFirma", cascade = CascadeType.ALL)
    private List<AusgelieheneMitarbeiter> ausgelieheneMitarbeiterList = new ArrayList<>();

    @Column
    private double ersteSchicht;

    @Column
    private double zweiteSchicht;

    @Column
    private double dritteSchicht;

    public void addMitarbeiter(Mitarbeiter mitarbeiter, Schicht schicht) {
        AusgelieheneMitarbeiter ausgelieheneMitarbeiter = new AusgelieheneMitarbeiter();
        ausgelieheneMitarbeiter.setMitarbeiter(mitarbeiter);
        ausgelieheneMitarbeiter.setAuftraggeberFirma(this);
        ausgelieheneMitarbeiter.setTag(LocalDate.now());
        ausgelieheneMitarbeiter.setSchicht(schicht);
        ausgelieheneMitarbeiterList.add(ausgelieheneMitarbeiter);
    }
}
