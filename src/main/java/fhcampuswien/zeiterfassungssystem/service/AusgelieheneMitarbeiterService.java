package fhcampuswien.zeiterfassungssystem.service;

import fhcampuswien.zeiterfassungssystem.entity.AusgelieheneMitarbeiter;
import fhcampuswien.zeiterfassungssystem.repository.AusgelieheneMitarbeiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
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

}
