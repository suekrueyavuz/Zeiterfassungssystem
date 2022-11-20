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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AuftraggeberFirmaService {
    private AuftraggeberFirmaRepository auftraggeberFirmaRepository;
    private MitarbeiterService mitarbeiterService;
    private AusgelieheneMitarbeiterService ausgelieheneMitarbeiterService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AuftraggeberFirmaService(AuftraggeberFirmaRepository auftraggeberFirmaRepository,
                                    MitarbeiterService mitarbeiterService,
                                    AusgelieheneMitarbeiterService ausgelieheneMitarbeiterService,
                                    PasswordEncoder passwordEncoder) {
        this.auftraggeberFirmaRepository = auftraggeberFirmaRepository;
        this.mitarbeiterService = mitarbeiterService;
        this.ausgelieheneMitarbeiterService = ausgelieheneMitarbeiterService;
        this.passwordEncoder = passwordEncoder;
    }

    public AuftraggeberFirma save(AuftraggeberFirma firma) {
        return auftraggeberFirmaRepository.save(firma);
    }

    public AuftraggeberFirma getFirmaById(Long id){
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

    public List<AuftraggeberFirma> getAll() {
        return auftraggeberFirmaRepository.findAll();
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

    public void passwortAendern(Long firmaId, String altesPasswort, String neuesPasswort) {
        AuftraggeberFirma firma = auftraggeberFirmaRepository.findById(firmaId).get();
        if(passwordEncoder.matches(altesPasswort, firma.getPassword())) {
            firma.setPassword(passwordEncoder.encode(neuesPasswort));
        } else {
            throw new IllegalArgumentException("Altes Passwort ist nicht korrekt");
        }
    }

    public List<AusgelieheneMitarbeiter> getAusgelieheneMitarbeiter(Long firmaId) {
        return ausgelieheneMitarbeiterService.getAllByFirmaId(firmaId);
    }

    public void generateReportFuerFirma(HttpServletResponse response, Long firmaId) throws IOException {
        List<AusgelieheneMitarbeiter> ausgelieheneMitarbeiterList = ausgelieheneMitarbeiterService.getAllByFirmaId(firmaId);
        List<Report> reports = erstelleReports(firmaId, ausgelieheneMitarbeiterList);
        ReportGenerator reportGenerator = new ReportGenerator(reports);
        reportGenerator.writeData(response);
    }

    private List<Report> erstelleReports(Long firmaId, List<AusgelieheneMitarbeiter> ausgelieheneMitarbeiterList) {
        List<Mitarbeiter> mitarbeiterList = new ArrayList<>();
        for (AusgelieheneMitarbeiter ausgelieheneMitarbeiter : ausgelieheneMitarbeiterList) {
            if(!mitarbeiterList.contains(ausgelieheneMitarbeiter.getMitarbeiter())) {
                mitarbeiterList.add(ausgelieheneMitarbeiter.getMitarbeiter());
            }
        }

        AuftraggeberFirma firma = auftraggeberFirmaRepository.findById(firmaId).get();

        List<Report> reports = new ArrayList<>();

        for (Mitarbeiter mitarbeiter : mitarbeiterList) {
            double ersteSchicht = 0;
            double zweiteSchicht = 0;
            double dritteSchicht = 0;

            for (AusgelieheneMitarbeiter ausgelieheneMitarbeiter : ausgelieheneMitarbeiterList) {
                if (mitarbeiter.getId().equals(ausgelieheneMitarbeiter.getMitarbeiter().getId())) {
                    LocalTime startZeit = ausgelieheneMitarbeiter.getStartZeit();
                    LocalTime endZeit = ausgelieheneMitarbeiter.getEndZeit();
                    switch (ausgelieheneMitarbeiter.getSchicht()) {
                        case ERSTE_SCHICHT:
                            ersteSchicht += getDifferenceBetweenTimes(startZeit, endZeit);
                            break;
                        case ZWEITE_SCHICHT:
                            zweiteSchicht += getDifferenceBetweenTimes(startZeit, endZeit);
                            break;
                        case DRITTE_SCHICHT:
                            dritteSchicht += getDifferenceBetweenTimes(startZeit, endZeit);
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
        return reports;
    }

    private double getDifferenceBetweenTimes(LocalTime start, LocalTime end) {
        if(start.isBefore(end)) {
            return (double) Duration.between(start, end).toMinutes();
        } else {
            return (double) Duration.ofHours(24).minus(Duration.between(end, start)).toMinutes();
        }
    }

}
