package fhcampuswien.zeiterfassungssystem.service;

import fhcampuswien.zeiterfassungssystem.Enum.AusgeliehenStatus;
import fhcampuswien.zeiterfassungssystem.entity.AusgelieheneMitarbeiter;
import fhcampuswien.zeiterfassungssystem.entity.Mitarbeiter;
import fhcampuswien.zeiterfassungssystem.entity.AuftraggeberFirma;
import fhcampuswien.zeiterfassungssystem.report.ExcelGenerator;
import fhcampuswien.zeiterfassungssystem.report.Report;
import fhcampuswien.zeiterfassungssystem.repository.AuftraggeberFirmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuftraggeberFirmaService {
    private AuftraggeberFirmaRepository auftraggeberFirmaRepository;
    private MitarbeiterService mitarbeiterService;
    private AusgelieheneMitarbeiterService ausgelieheneMitarbeiterService;

    @Autowired
    public AuftraggeberFirmaService(AuftraggeberFirmaRepository auftraggeberFirmaRepository,
                                    MitarbeiterService mitarbeiterService,
                                    AusgelieheneMitarbeiterService ausgelieheneMitarbeiterService) {
        this.auftraggeberFirmaRepository = auftraggeberFirmaRepository;
        this.mitarbeiterService = mitarbeiterService;
        this.ausgelieheneMitarbeiterService = ausgelieheneMitarbeiterService;
    }

    public AuftraggeberFirma save(AuftraggeberFirma firma) {
        return auftraggeberFirmaRepository.save(firma);
    }

    public AuftraggeberFirma getFirmaByUsername(String username) {
        return auftraggeberFirmaRepository.findByUsername(username);
    }

    @Transactional
    public void addMitarbeiterToCompany(Long mitarbeiterId, Long firmaId) {
        Mitarbeiter mitarbeiter = mitarbeiterService.getMitarbeiter(mitarbeiterId);
        mitarbeiter.setAusgeliehenStatus(AusgeliehenStatus.AUSGELIEHEN);
        AuftraggeberFirma company = auftraggeberFirmaRepository.findById(firmaId).get();
        company.addMitarbeiter(mitarbeiter);
    }

    public void generateReport(HttpServletResponse response, Long firmaId) throws IOException {
        List<AusgelieheneMitarbeiter> ausgelieheneMitarbeiterList = ausgelieheneMitarbeiterService.getAllByFirmaId(firmaId);

        List<Long> mitarbeiterIds = new ArrayList<>();
        for (AusgelieheneMitarbeiter ausgelieheneMitarbeiter : ausgelieheneMitarbeiterList) {
            mitarbeiterIds.add(ausgelieheneMitarbeiter.getMitarbeiter().getId());
        }

        List<Mitarbeiter> mitarbeiterList = new ArrayList<>();
        for (Long mitarbeiterId : mitarbeiterIds) {
            Mitarbeiter mitarbeiter = mitarbeiterService.getMitarbeiter(mitarbeiterId);
            mitarbeiterList.add(mitarbeiter);
        }

        AuftraggeberFirma firma = auftraggeberFirmaRepository.findById(firmaId).get();

        List<Report> reports = new ArrayList<>();

        for (Mitarbeiter mitarbeiter : mitarbeiterList) {
            Report report = new Report();
            report.setMitarbeiterName(mitarbeiter.getForename() + " " + mitarbeiter.getSurname());
            report.setErsteSchicht(firma.getErsteSchicht());
            report.setZweiteSchicht(firma.getZweiteSchicht());
            report.setDritteSchicht(firma.getDritteSchicht());
            reports.add(report);
        }

        ExcelGenerator excelGenerator = new ExcelGenerator(reports);
        excelGenerator.generateReport(response);
    }
}
