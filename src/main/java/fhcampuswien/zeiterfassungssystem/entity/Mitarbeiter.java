package fhcampuswien.zeiterfassungssystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fhcampuswien.zeiterfassungssystem.Enum.Role;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
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

    @JsonIgnore
    @OneToMany(mappedBy = "mitarbeiter", cascade = CascadeType.ALL)
    private List<AusgelieheneMitarbeiter> ausgelieheneMitarbeiter;
}
