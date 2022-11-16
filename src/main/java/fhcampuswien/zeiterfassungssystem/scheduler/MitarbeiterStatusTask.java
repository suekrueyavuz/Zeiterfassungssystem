package fhcampuswien.zeiterfassungssystem.scheduler;

import fhcampuswien.zeiterfassungssystem.entity.AusgelieheneMitarbeiter;
import fhcampuswien.zeiterfassungssystem.entity.Mitarbeiter;
import fhcampuswien.zeiterfassungssystem.service.AusgelieheneMitarbeiterService;
import fhcampuswien.zeiterfassungssystem.service.MitarbeiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Component
@Transactional
public class MitarbeiterStatusTask {
    @Autowired
    private MitarbeiterService mitarbeiterService;
    @Autowired
    private AusgelieheneMitarbeiterService ausgelieheneMitarbeiterService;

    @Scheduled(fixedRate = 3600000)
    public void updateAusgeliehenStatus() {
        List<AusgelieheneMitarbeiter> ausgelieheneMitarbeiter = ausgelieheneMitarbeiterService.findAllByTag(LocalDate.now());
        for (AusgelieheneMitarbeiter mitarbeiter : ausgelieheneMitarbeiter) {
            if (mitarbeiter.getEndZeit() != null) {
                setMitarbeiterVerfuegbar(mitarbeiter.getMitarbeiter().getId());
            }
        }
    }

    public void setMitarbeiterVerfuegbar(Long mitarbeiterId) {
        Mitarbeiter mitarbeiter = mitarbeiterService.getMitarbeiter(mitarbeiterId);
        mitarbeiter.setId(mitarbeiterId);
    }
}
