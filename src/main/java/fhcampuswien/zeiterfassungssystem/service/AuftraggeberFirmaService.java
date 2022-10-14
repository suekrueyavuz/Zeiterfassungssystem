package fhcampuswien.zeiterfassungssystem.service;

import fhcampuswien.zeiterfassungssystem.entity.Mitarbeiter;
import fhcampuswien.zeiterfassungssystem.entity.AuftraggeberFirma;
import fhcampuswien.zeiterfassungssystem.repository.AuftraggeberFirmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class AuftraggeberFirmaService {
    private AuftraggeberFirmaRepository auftraggeberFirmaRepository;
    private MitarbeiterService mitarbeiterService;

    @Autowired
    public AuftraggeberFirmaService(AuftraggeberFirmaRepository auftraggeberFirmaRepository, MitarbeiterService mitarbeiterService) {
        this.auftraggeberFirmaRepository = auftraggeberFirmaRepository;
        this.mitarbeiterService = mitarbeiterService;
    }

    public void createNewCompany(AuftraggeberFirma firma) {
        auftraggeberFirmaRepository.save(firma);
    }

    @Transactional
    public void addMitarbeiterToCompany(Long mitarbeiterId, Long firmaId) {
        Mitarbeiter mitarbeiter = mitarbeiterService.getMitarbeiter(mitarbeiterId);
        AuftraggeberFirma company = auftraggeberFirmaRepository.findById(firmaId).get();
        company.addMitarbeiter(mitarbeiter);
    }
}
