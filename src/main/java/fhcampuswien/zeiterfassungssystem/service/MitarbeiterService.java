package fhcampuswien.zeiterfassungssystem.service;

import fhcampuswien.zeiterfassungssystem.entity.AusgelieheneMitarbeiter;
import fhcampuswien.zeiterfassungssystem.entity.Mitarbeiter;
import fhcampuswien.zeiterfassungssystem.repository.MitarbeiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;

@Service
public class MitarbeiterService {
    private MitarbeiterRepository mitarbeiterRepository;
    private AusgelieheneMitarbeiterService ausgelieheneMitarbeiterService;

    @Autowired
    public MitarbeiterService (MitarbeiterRepository mitarbeiterRepository,
                               AusgelieheneMitarbeiterService ausgelieheneMitarbeiterService) {
        this.mitarbeiterRepository = mitarbeiterRepository;
        this.ausgelieheneMitarbeiterService = ausgelieheneMitarbeiterService;
    }

    public Mitarbeiter createNewMitarbeiter(Mitarbeiter mitarbeiter) {
        return mitarbeiterRepository.save(mitarbeiter);
    }

    public Mitarbeiter getMitarbeiter(Long id) {
        return mitarbeiterRepository.findById(id).get();
    }

    @Transactional
    public void startZeitEintragen(String startZeit, Long mitarbeiterId, Long firmaId, LocalDate arbeitstag) {
        AusgelieheneMitarbeiter mitarbeiter = ausgelieheneMitarbeiterService.getAusgeliehenenMitarbeiterVonFirma(mitarbeiterId, firmaId, arbeitstag);
        mitarbeiter.setStartZeit(startZeit);
    }
}
