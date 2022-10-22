package fhcampuswien.zeiterfassungssystem.service;

import fhcampuswien.zeiterfassungssystem.Enum.AusgeliehenStatus;
import fhcampuswien.zeiterfassungssystem.Enum.ZeitStatus;
import fhcampuswien.zeiterfassungssystem.entity.AusgelieheneMitarbeiter;
import fhcampuswien.zeiterfassungssystem.entity.Mitarbeiter;
import fhcampuswien.zeiterfassungssystem.repository.MitarbeiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;

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

    public Mitarbeiter save(Mitarbeiter mitarbeiter) {
        mitarbeiter.setAusgeliehenStatus(AusgeliehenStatus.VERFUEGBAR);
        return mitarbeiterRepository.save(mitarbeiter);
    }

    public Mitarbeiter getMitarbeiter(Long id) {
        return mitarbeiterRepository.findById(id).get();
    }

    public Mitarbeiter getMitarbeiterByUsername(String username) {
        return mitarbeiterRepository.findByUsername(username);
    }

    @Transactional
    public void arbeitszeitenEintragen(String startZeit, String endZeit, Long mitarbeiterId, Long firmaId, LocalDate arbeitstag) {
        if(!checkIfZeitenIsCorrect(startZeit, endZeit)) {
            throw new IllegalArgumentException("Start- und Endzeit-Angaben stimmen nicht.");
        }
        AusgelieheneMitarbeiter mitarbeiter = ausgelieheneMitarbeiterService.getAusgeliehenenMitarbeiterVonFirma(mitarbeiterId, firmaId, arbeitstag);
        mitarbeiter.setStartZeit(parseArbeitszeiten(startZeit));
        mitarbeiter.setEndZeit(parseArbeitszeiten(endZeit));
        mitarbeiter.setZeitStatus(ZeitStatus.INBEARBEITUNG);
    }

    @Transactional
    public void arbeitzeitenStatusBearbeiten(Long schichtleiterId, Long mitarbeiterId, Long firmaId, LocalDate arbeitstag, ZeitStatus zeitStatus) {
        AusgelieheneMitarbeiter mitarbeiter = ausgelieheneMitarbeiterService.getAusgeliehenenMitarbeiterVonFirma(mitarbeiterId, firmaId, arbeitstag);
        mitarbeiter.setZeitStatus(zeitStatus);
    }

    private LocalTime parseArbeitszeiten(String arbeitszeit) {
        return LocalTime.parse(arbeitszeit);
    }

    private boolean checkIfZeitenIsCorrect(String startzeit, String endzeit) {
        LocalTime start = LocalTime.parse(startzeit);
        LocalTime ende = LocalTime.parse(endzeit);
        int value = start.compareTo(ende);
        return value < 0;
    }
}
