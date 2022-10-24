package fhcampuswien.zeiterfassungssystem.report;

import fhcampuswien.zeiterfassungssystem.Enum.Schicht;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Report {
    private String mitarbeiterName;
    private double ersteSchichtFirma;
    private double zweiteSchichtFirma;
    private double dritteSchichtFirma;
    private double ersteSchichtMitarbeiter;
    private double zweiteSchichtMitarbeiter;
    private double dritteSchichtMitarbeiter;
    private double uStunden;
    private double uStundenSoFt;
    private double betrag;
}
