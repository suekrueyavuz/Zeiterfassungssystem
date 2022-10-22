package fhcampuswien.zeiterfassungssystem.service;

import fhcampuswien.zeiterfassungssystem.Enum.AusgeliehenStatus;
import fhcampuswien.zeiterfassungssystem.entity.Mitarbeiter;
import fhcampuswien.zeiterfassungssystem.entity.AuftraggeberFirma;
import fhcampuswien.zeiterfassungssystem.report.Report;
import fhcampuswien.zeiterfassungssystem.repository.AuftraggeberFirmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;

@Service
public class AuftraggeberFirmaService {
    private AuftraggeberFirmaRepository auftraggeberFirmaRepository;
    private MitarbeiterService mitarbeiterService;

    @Autowired
    public AuftraggeberFirmaService(AuftraggeberFirmaRepository auftraggeberFirmaRepository,
                                    MitarbeiterService mitarbeiterService) {
        this.auftraggeberFirmaRepository = auftraggeberFirmaRepository;
        this.mitarbeiterService = mitarbeiterService;
    }

    public AuftraggeberFirma save(AuftraggeberFirma firma) {
        return auftraggeberFirmaRepository.save(firma);
    }

    public AuftraggeberFirma getFirmaByUsername(String username) {
        return auftraggeberFirmaRepository.findByUsername(username);
    }

    public void generateReport(HttpServletResponse response) throws IOException {
        Report report = new Report();
        report.setErsteSchicht("1");
        report.setZweiteSchicht("2");
        report.generateReport(response);
    }

    @Transactional
    public void addMitarbeiterToCompany(Long mitarbeiterId, Long firmaId) {
        Mitarbeiter mitarbeiter = mitarbeiterService.getMitarbeiter(mitarbeiterId);
        mitarbeiter.setAusgeliehenStatus(AusgeliehenStatus.AUSGELIEHEN);
        AuftraggeberFirma company = auftraggeberFirmaRepository.findById(firmaId).get();
        company.addMitarbeiter(mitarbeiter);
    }
}
