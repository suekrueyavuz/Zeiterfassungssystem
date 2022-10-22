package fhcampuswien.zeiterfassungssystem.entity;

import fhcampuswien.zeiterfassungssystem.Enum.ZeitStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AusgelieheneMitarbeiter {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mitarbeiter_id")
    private Mitarbeiter mitarbeiter;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "auftraggeberFirma_id")
    private AuftraggeberFirma auftraggeberFirma;

    @Column
    private LocalDate tag;

    @Column
    private LocalTime startZeit;

    @Column
    private LocalTime endZeit;

    @Column
    @Enumerated(EnumType.STRING)
    private ZeitStatus zeitStatus;

}
