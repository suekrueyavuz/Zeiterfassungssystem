package fhcampuswien.zeiterfassungssystem.service;

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

    public List<AusgelieheneMitarbeiter> getAllByFirmaIdFuerZeitraum(Long firmaId, LocalDate von, LocalDate bis) {
        return ausgelieheneMitarbeiterRepository.getAllByFirmaIdFuerZeitraum(firmaId, von, bis);
    }

    public List<AusgelieheneMitarbeiter> getAusleihungen(Long mitarbeiterId) {
        return ausgelieheneMitarbeiterRepository.findAllByMitarbeiter_Id(mitarbeiterId);
    }

}
