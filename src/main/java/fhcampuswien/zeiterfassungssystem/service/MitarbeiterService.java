package fhcampuswien.zeiterfassungssystem.service;

import fhcampuswien.zeiterfassungssystem.Enum.AusgeliehenStatus;
import fhcampuswien.zeiterfassungssystem.Enum.Role;
import fhcampuswien.zeiterfassungssystem.Enum.ZeitStatus;
import fhcampuswien.zeiterfassungssystem.entity.AusgelieheneMitarbeiter;
import fhcampuswien.zeiterfassungssystem.entity.Mitarbeiter;
import fhcampuswien.zeiterfassungssystem.repository.MitarbeiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MitarbeiterService {
    private MitarbeiterRepository mitarbeiterRepository;
    private AusgelieheneMitarbeiterService ausgelieheneMitarbeiterService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public MitarbeiterService (MitarbeiterRepository mitarbeiterRepository,
                               AusgelieheneMitarbeiterService ausgelieheneMitarbeiterService,
                               PasswordEncoder passwordEncoder) {
        this.mitarbeiterRepository = mitarbeiterRepository;
        this.ausgelieheneMitarbeiterService = ausgelieheneMitarbeiterService;
        this.passwordEncoder = passwordEncoder;
    }

    public Mitarbeiter save(Mitarbeiter mitarbeiter) {
        mitarbeiter.setAusgeliehenStatus(AusgeliehenStatus.VERFUEGBAR);
        mitarbeiter.setPassword(mitarbeiter.getPassword());
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

    public void editAusgeliehenStatus(Long mitarbeiterId, AusgeliehenStatus status) {
        Mitarbeiter mitarbeiter = mitarbeiterRepository.findById(mitarbeiterId).get();
        mitarbeiter.setAusgeliehenStatus(status);
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
        AusgelieheneMitarbeiter mitarbeiter = ausgelieheneMitarbeiterService.getAusgeliehenenMitarbeiterVonFirma(mitarbeiterId, firmaId, arbeitstag);
        mitarbeiter.setStartZeit(parseArbeitszeiten(startZeit));
        mitarbeiter.setEndZeit(parseArbeitszeiten(endZeit));
        mitarbeiter.setZeitStatus(ZeitStatus.INBEARBEITUNG);
        editAusgeliehenStatus(mitarbeiterId, AusgeliehenStatus.VERFUEGBAR);
    }

    public List<AusgelieheneMitarbeiter> getAusleihungen(Long mitarbeiterId) {
        return ausgelieheneMitarbeiterService.getAusleihungenVonMitarbeiter(mitarbeiterId);
    }

    public void arbeitzeitenStatusBearbeiten(Long mitarbeiterId, Long firmaId, LocalDate arbeitstag, ZeitStatus zeitStatus) {
        AusgelieheneMitarbeiter mitarbeiter = ausgelieheneMitarbeiterService.getAusgeliehenenMitarbeiterVonFirma(mitarbeiterId, firmaId, arbeitstag);
        mitarbeiter.setZeitStatus(zeitStatus);
    }

    public void markiereAlsFeiertag(Long mitarbeiterId, Long firmaId, LocalDate tag, boolean isFeiertag) {
        AusgelieheneMitarbeiter mitarbeiter = ausgelieheneMitarbeiterService.getAusgeliehenenMitarbeiterVonFirma(mitarbeiterId, firmaId, tag);
        mitarbeiter.setFeiertag(isFeiertag);
    }

    public void markiereAlsUeberstunde(Long mitarbeiterId, Long firmaId, LocalDate tag) {
        AusgelieheneMitarbeiter mitarbeiter = ausgelieheneMitarbeiterService.getAusgeliehenenMitarbeiterVonFirma(mitarbeiterId, firmaId, tag);
        mitarbeiter.setUeberStunde(getDifferenceBetweenTimes(mitarbeiter.getStartZeit(), mitarbeiter.getEndZeit()));
    }

    private double getDifferenceBetweenTimes(LocalTime start, LocalTime end) {
        if(start.isBefore(end)) {
            return (double) Duration.between(start, end).toMinutes() / 60;
        } else {
            return (double) Duration.ofHours(24).minus(Duration.between(end, start)).toMinutes() / 60;
        }
    }

    private LocalTime parseArbeitszeiten(String arbeitszeit) {
        return LocalTime.parse(arbeitszeit);
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
