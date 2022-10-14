package fhcampuswien.zeiterfassungssystem.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
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

    @OneToMany(mappedBy = "auftraggeberFirma", cascade = CascadeType.ALL)
    private List<AusgelieheneMitarbeiter> ausgelieheneMitarbeiterList = new ArrayList<>();

    @Column
    private double ersteSchicht;

    @Column
    private double zweiteSchicht;

    @Column
    private double dritteSchicht;

    public void addMitarbeiter(Mitarbeiter mitarbeiter) {
        AusgelieheneMitarbeiter ausgelieheneMitarbeiter = new AusgelieheneMitarbeiter();
        ausgelieheneMitarbeiter.setMitarbeiter(mitarbeiter);
        ausgelieheneMitarbeiter.setAuftraggeberFirma(this);
        ausgelieheneMitarbeiter.setTag(LocalDate.now());
        ausgelieheneMitarbeiterList.add(ausgelieheneMitarbeiter);
    }
}
