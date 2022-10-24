package fhcampuswien.zeiterfassungssystem.service;

import fhcampuswien.zeiterfassungssystem.Enum.AusgeliehenStatus;
import fhcampuswien.zeiterfassungssystem.Enum.Schicht;
import fhcampuswien.zeiterfassungssystem.entity.AusgelieheneMitarbeiter;
import fhcampuswien.zeiterfassungssystem.entity.Mitarbeiter;
import fhcampuswien.zeiterfassungssystem.entity.AuftraggeberFirma;
import fhcampuswien.zeiterfassungssystem.report.ReportGenerator;
import fhcampuswien.zeiterfassungssystem.report.Report;
import fhcampuswien.zeiterfassungssystem.repository.AuftraggeberFirmaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
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

    private AuftraggeberFirma getFirmaById(Long id){
        if(auftraggeberFirmaRepository.findById(id).isPresent()){
            return auftraggeberFirmaRepository.findById(id).get();
        }else{
            //TODO Excpetion
            return null;
        }
    }

    public void remove(Long id){
        auftraggeberFirmaRepository.delete(getFirmaById(id));
    }
    public AuftraggeberFirma getFirmaByUsername(String username) {
        return auftraggeberFirmaRepository.findByUsername(username);
    }

    public void addMitarbeiterToCompany(Long mitarbeiterId, Long firmaId, Schicht schicht) {
        Mitarbeiter mitarbeiter = mitarbeiterService.getMitarbeiter(mitarbeiterId);
        mitarbeiter.setAusgeliehenStatus(AusgeliehenStatus.AUSGELIEHEN);
        AuftraggeberFirma company = auftraggeberFirmaRepository.findById(firmaId).get();
        company.addMitarbeiter(mitarbeiter, schicht);
    }

    public String resetPassword(Long firmaId, String newpass) {
        Optional<AuftraggeberFirma> firma1 = auftraggeberFirmaRepository.findById(firmaId);
        if(firma1.isPresent()){
            firma1.get().setPassword(newpass);
            auftraggeberFirmaRepository.save(firma1.get());
            return "Password resetted";
        }
        return "Company not found";
    }

    public void generateReport(HttpServletResponse response, Long firmaId) throws IOException {
        List<AusgelieheneMitarbeiter> ausgelieheneMitarbeiterList = ausgelieheneMitarbeiterService.getAllByFirmaId(firmaId);

        List<Long> mitarbeiterIds = new ArrayList<>();
        for (AusgelieheneMitarbeiter ausgelieheneMitarbeiter : ausgelieheneMitarbeiterList) {
            Long mitarbeiterId = ausgelieheneMitarbeiter.getMitarbeiter().getId();
            if (!mitarbeiterIds.contains(mitarbeiterId)) {
                mitarbeiterIds.add(ausgelieheneMitarbeiter.getMitarbeiter().getId());
            }
        }

        List<Mitarbeiter> mitarbeiterList = new ArrayList<>();
        for (Long mitarbeiterId : mitarbeiterIds) {
            Mitarbeiter mitarbeiter = mitarbeiterService.getMitarbeiter(mitarbeiterId);
            mitarbeiterList.add(mitarbeiter);
        }

        AuftraggeberFirma firma = auftraggeberFirmaRepository.findById(firmaId).get();

        List<Report> reports = new ArrayList<>();

        for (Mitarbeiter mitarbeiter : mitarbeiterList) {
            double ersteSchicht = 0;
            double zweiteSchicht = 0;
            double dritteSchicht = 0;

            for (AusgelieheneMitarbeiter ausgelieheneMitarbeiter : ausgelieheneMitarbeiterList) {
                if (mitarbeiter.getId().equals(ausgelieheneMitarbeiter.getMitarbeiter().getId())) {
                    switch (ausgelieheneMitarbeiter.getSchicht()) {
                        case ERSTE_SCHICHT:
                            ersteSchicht += (double) ausgelieheneMitarbeiter.getStartZeit().until(ausgelieheneMitarbeiter.getEndZeit(), ChronoUnit.MINUTES);
                            break;
                        case ZWEITE_SCHICHT:
                            zweiteSchicht += (double) ausgelieheneMitarbeiter.getStartZeit().until(ausgelieheneMitarbeiter.getEndZeit(), ChronoUnit.MINUTES);
                            break;
                        case DRITTE_SCHICHT:
                            dritteSchicht += (double) ausgelieheneMitarbeiter.getStartZeit().until(ausgelieheneMitarbeiter.getEndZeit(), ChronoUnit.MINUTES);
                            break;
                    }
                }
            }

            Report report = new Report();
            report.setErsteSchichtMitarbeiter(ersteSchicht/60);
            report.setZweiteSchichtMitarbeiter(zweiteSchicht/60);
            report.setDritteSchichtMitarbeiter(dritteSchicht/60);
            report.setMitarbeiterName(mitarbeiter.getForename() + " " + mitarbeiter.getSurname());
            report.setErsteSchichtFirma(firma.getErsteSchicht());
            report.setZweiteSchichtFirma(firma.getZweiteSchicht());
            report.setDritteSchichtFirma(firma.getDritteSchicht());
            reports.add(report);
        }

        ReportGenerator reportGenerator = new ReportGenerator(reports);
        reportGenerator.generateReport(response);
    }
}
