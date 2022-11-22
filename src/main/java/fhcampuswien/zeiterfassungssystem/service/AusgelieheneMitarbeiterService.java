package fhcampuswien.zeiterfassungssystem.service;

import fhcampuswien.zeiterfassungssystem.Enum.ZeitStatus;
import fhcampuswien.zeiterfassungssystem.entity.AusgelieheneMitarbeiter;
import fhcampuswien.zeiterfassungssystem.repository.AusgelieheneMitarbeiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class AusgelieheneMitarbeiterService {
    private AusgelieheneMitarbeiterRepository ausgelieheneMitarbeiterRepository;

    @Autowired
    public AusgelieheneMitarbeiterService(AusgelieheneMitarbeiterRepository ausgelieheneMitarbeiterRepository) {
        this.ausgelieheneMitarbeiterRepository = ausgelieheneMitarbeiterRepository;
    }

    public AusgelieheneMitarbeiter getAusgeliehenenMitarbeiterVonFirma(Long mitarbeiterId, Long firmaId, LocalDate arbeitstag) {
        return ausgelieheneMitarbeiterRepository.getAusgelieheneMitarbeiterVonFirma(mitarbeiterId, firmaId, arbeitstag);
    }

    public List<AusgelieheneMitarbeiter> getAllByFirmaId(Long firmaId) {
        return ausgelieheneMitarbeiterRepository.getAllByFirmaId(firmaId);
    }

    public List<AusgelieheneMitarbeiter> getAllByFirmaIdAndZeitstatus(Long firmaId, ZeitStatus status) {
        return ausgelieheneMitarbeiterRepository.getAllByFirmaIdAndZeitstatus(firmaId, status);
    }

    public List<AusgelieheneMitarbeiter> getAllByFirmaIdFuerZeitraum(Long firmaId, LocalDate von, LocalDate bis) {
        return ausgelieheneMitarbeiterRepository.getAllByFirmaIdAndZeitstatusFuerZeitraum(firmaId, von, bis);
    }

    public List<AusgelieheneMitarbeiter> getAusleihungenVonMitarbeiter(Long mitarbeiterId) {
        return ausgelieheneMitarbeiterRepository.findAllByMitarbeiter_Id(mitarbeiterId);
    }

    public List<AusgelieheneMitarbeiter> getAusgelieheneMitarbeiter() {
        return ausgelieheneMitarbeiterRepository.findAll();
    }

    public void updateAusgeliehenenMitarbeiter(Long mitarbeiterId, AusgelieheneMitarbeiter ausgelieheneMitarbeiter) {
        AusgelieheneMitarbeiter mitarbeiter = getAusgeliehenenMitarbeiterVonFirma(mitarbeiterId,
                ausgelieheneMitarbeiter.getAuftraggeberFirma().getId(), ausgelieheneMitarbeiter.getTag());
        mitarbeiter.setStartZeit(ausgelieheneMitarbeiter.getStartZeit());
        mitarbeiter.setEndZeit(ausgelieheneMitarbeiter.getEndZeit());
        mitarbeiter.setZeitStatus(ausgelieheneMitarbeiter.getZeitStatus());
    }

}
