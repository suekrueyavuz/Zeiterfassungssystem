package fhcampuswien.zeiterfassungssystem.service;

import fhcampuswien.zeiterfassungssystem.entity.Mitarbeiter;
import fhcampuswien.zeiterfassungssystem.repository.MitarbeiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class MitarbeiterService {
    private MitarbeiterRepository mitarbeiterRepository;

    @Autowired
    public MitarbeiterService (MitarbeiterRepository mitarbeiterRepository) {
        this.mitarbeiterRepository = mitarbeiterRepository;
    }

    public Mitarbeiter createNewMitarbeiter(Mitarbeiter mitarbeiter) {
        return mitarbeiterRepository.save(mitarbeiter);
    }

    public Mitarbeiter getMitarbeiter(Long id) {
        return mitarbeiterRepository.findById(id).get();
    }

    @Transactional
    public void startZeitEintragen(String startZeit, Long mitarbeiterId) {
        Mitarbeiter mitarbeiter = getMitarbeiter(mitarbeiterId);
        for (int i=0; i<mitarbeiter.getAusgelieheneMitarbeiter().size(); i++) {
            if(mitarbeiter.getId().equals(mitarbeiter.getAusgelieheneMitarbeiter().get(i).getMitarbeiter().getId())) {
                mitarbeiter.getAusgelieheneMitarbeiter().get(i).setStartZeit(startZeit);
            }
        }
    }
}
