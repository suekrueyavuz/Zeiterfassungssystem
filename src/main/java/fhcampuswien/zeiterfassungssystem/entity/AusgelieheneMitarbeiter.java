package fhcampuswien.zeiterfassungssystem.entity;

import fhcampuswien.zeiterfassungssystem.Enum.Status;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

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
    private String startZeit;

    @Column
    private String endZeit;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

}
