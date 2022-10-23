package fhcampuswien.zeiterfassungssystem.report;

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
    private double ersteSchicht;
    private double zweiteSchicht;
    private double dritteSchicht;
    private double uStunden;
    private double uStundenSoFt;
    private double betrag;
}
