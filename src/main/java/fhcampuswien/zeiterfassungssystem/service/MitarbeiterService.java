package fhcampuswien.zeiterfassungssystem.service;

import fhcampuswien.zeiterfassungssystem.Enum.AusgeliehenStatus;
import fhcampuswien.zeiterfassungssystem.Enum.Role;
import fhcampuswien.zeiterfassungssystem.Enum.ZeitStatus;
import fhcampuswien.zeiterfassungssystem.entity.AusgelieheneMitarbeiter;
import fhcampuswien.zeiterfassungssystem.entity.Mitarbeiter;
import fhcampuswien.zeiterfassungssystem.repository.MitarbeiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
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

    public void removeMitarbeiter(Long mitarbeiterId) {
        Mitarbeiter mitarbeiter = getMitarbeiter(mitarbeiterId);
        mitarbeiterRepository.delete(mitarbeiter);
    }

    public void editMitarbeiter(Mitarbeiter mitarbeiter) {
        Mitarbeiter editedMitarbeiter = getMitarbeiter(mitarbeiter.getId());
        editedMitarbeiter.setForename(mitarbeiter.getForename());
        editedMitarbeiter.setSurname(mitarbeiter.getSurname());
        editedMitarbeiter.setUsername(mitarbeiter.getUsername());
        editedMitarbeiter.setRole(mitarbeiter.getRole());
        mitarbeiterRepository.save(editedMitarbeiter);
    }

    public Mitarbeiter getMitarbeiter(Long id) {
        return mitarbeiterRepository.findById(id).get();
    }

    public Mitarbeiter getMitarbeiterByUsername(String username) {
        return mitarbeiterRepository.findByUsername(username);
    }

    public List<Mitarbeiter> getAllMitarbeiterByStatus(AusgeliehenStatus status) {
        return mitarbeiterRepository.findAllByAusgeliehenStatusAndRole(status, Role.ROLE_MITARBEITER);
    }

    public List<Mitarbeiter> getAllByRole(Role role) {
        return mitarbeiterRepository.findAllByRole(role);
    }

    public List<Mitarbeiter> getAll() {
        return mitarbeiterRepository.findAll();
    }

    public void arbeitszeitenEintragen(String startZeit, String endZeit, Long mitarbeiterId, Long firmaId, LocalDate arbeitstag) {
        if(!checkIfZeitenIsCorrect(startZeit, endZeit)) {
            throw new IllegalArgumentException("Start- und Endzeit-Angaben stimmen nicht.");
        }
        AusgelieheneMitarbeiter mitarbeiter = ausgelieheneMitarbeiterService.getAusgeliehenenMitarbeiterVonFirma(mitarbeiterId, firmaId, arbeitstag);
        mitarbeiter.setStartZeit(parseArbeitszeiten(startZeit));
        mitarbeiter.setEndZeit(parseArbeitszeiten(endZeit));
        mitarbeiter.setZeitStatus(ZeitStatus.INBEARBEITUNG);
    }

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

    public String resetPassword(Long mitarbeiterId, String newpass) {
        Optional<Mitarbeiter> mitarbeiter1 = mitarbeiterRepository.findById(mitarbeiterId);
        if(mitarbeiter1.isPresent()){
            mitarbeiter1.get().setPassword(newpass);
            mitarbeiterRepository.save(mitarbeiter1.get());
            return "Password resetted";
        }
        return "Employee not found";
    }
}
