package fhcampuswien.zeiterfassungssystem.controller;

import fhcampuswien.zeiterfassungssystem.Enum.AusgeliehenStatus;
import fhcampuswien.zeiterfassungssystem.Enum.Schicht;
import fhcampuswien.zeiterfassungssystem.entity.AuftraggeberFirma;
import fhcampuswien.zeiterfassungssystem.entity.AusgelieheneMitarbeiter;
import fhcampuswien.zeiterfassungssystem.entity.Mitarbeiter;
import fhcampuswien.zeiterfassungssystem.service.AuftraggeberFirmaService;
import fhcampuswien.zeiterfassungssystem.service.AusgelieheneMitarbeiterService;
import fhcampuswien.zeiterfassungssystem.service.MitarbeiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("admin")
public class AdminController {
    private MitarbeiterService mitarbeiterService;
    private AuftraggeberFirmaService auftraggeberFirmaService;
    private AusgelieheneMitarbeiterService ausgelieheneMitarbeiterService;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(MitarbeiterService mitarbeiterService,
                           AuftraggeberFirmaService auftraggeberFirmaService,
                           PasswordEncoder passwordEncoder,
                           AusgelieheneMitarbeiterService ausgelieheneMitarbeiterService) {
        this.passwordEncoder = passwordEncoder;
        this.mitarbeiterService = mitarbeiterService;
        this.auftraggeberFirmaService = auftraggeberFirmaService;
        this.ausgelieheneMitarbeiterService = ausgelieheneMitarbeiterService;
    }

    @PostMapping("/mitarbeiter")
    public ResponseEntity<?> createNewMitarbeiter(@RequestBody Mitarbeiter mitarbeiter) {
        if(mitarbeiterService.checkIfMitarbeiterExists(mitarbeiter.getUsername())) {
            return new ResponseEntity<>("Username existiert", HttpStatus.CONFLICT);
        }
        mitarbeiter.setPassword(passwordEncoder.encode(mitarbeiter.getPassword()));
        Mitarbeiter newMitarbeiter = mitarbeiterService.save(mitarbeiter);
        return new ResponseEntity<>(newMitarbeiter, HttpStatus.OK);
    }

    @GetMapping("/mitarbeiter")
    public ResponseEntity<List<Mitarbeiter>> getAllMitarbeiter() {
        List<Mitarbeiter> mitarbeiterList = mitarbeiterService.getAll();
        return new ResponseEntity<>(mitarbeiterList, HttpStatus.OK);
    }

    @GetMapping("/mitarbeiter/verfuegbar")
    public ResponseEntity<List<Mitarbeiter>> getVerfuegbareMitarbeiter() {
        List<Mitarbeiter> mitarbeiterList = mitarbeiterService.getAllMitarbeiterByStatus(AusgeliehenStatus.VERFUEGBAR);
        return new ResponseEntity<>(mitarbeiterList, HttpStatus.OK);
    }

    @GetMapping("/mitarbeiter/ausgeliehen")
    public ResponseEntity<List<AusgelieheneMitarbeiter>> getAusgelieheneMitarbeiter() {
        List<AusgelieheneMitarbeiter> mitarbeiterList = ausgelieheneMitarbeiterService.getAusgelieheneMitarbeiter();
        return new ResponseEntity<>(mitarbeiterList, HttpStatus.OK);
    }

    @PutMapping("/mitarbeiter/ausgeliehenerMitarbeiter/{id}")
    public void updateAusgeliehenenMitarbeiter(@PathVariable Long id,
                                               @RequestBody AusgelieheneMitarbeiter ausgelieheneMitarbeiter) {
        ausgelieheneMitarbeiterService.updateAusgeliehenenMitarbeiter(id, ausgelieheneMitarbeiter);
    }

    @GetMapping("/firma")
    public ResponseEntity<List<AuftraggeberFirma>> getAllFirmen() {
        List<AuftraggeberFirma> firmenList = auftraggeberFirmaService.getAll();
        return new ResponseEntity<>(firmenList, HttpStatus.OK);
    }

    @PutMapping("/mitarbeiter/{mitarbeiterId}")
    public void resetEmployeePass(@PathVariable final Long mitarbeiterId){
        String newpass = UUID.randomUUID().toString().substring(10);
        newpass = passwordEncoder.encode(newpass);
        mitarbeiterService.resetPassword(mitarbeiterId, newpass);
    }

    @PutMapping("/firma/{firmaId}")
    public void resetCompanyPass(@PathVariable final Long firmaId){
        String newpass = UUID.randomUUID().toString().substring(10);
        newpass = passwordEncoder.encode(newpass);
        auftraggeberFirmaService.resetPassword(firmaId, newpass);
    }

    @DeleteMapping("/mitarbeiter/{mitarbeiterId}")
    public void removeMitarbeiter(@PathVariable final Long mitarbeiterId) {
        mitarbeiterService.removeMitarbeiter(mitarbeiterId);
    }

    @PutMapping("/mitarbeiter/bearbeiten")
    public void editMitarbeiter(@RequestBody Mitarbeiter mitarbeiter) {
        mitarbeiterService.editMitarbeiter(mitarbeiter);
    }

    @DeleteMapping("/firma/{firmaId}")
    public void deleteCompany(@PathVariable final Long firmaId) {
        auftraggeberFirmaService.remove(firmaId);
    }

    @PostMapping("/firma")
    public void createNewCompany(@RequestBody AuftraggeberFirma firma) {
        firma.setPassword(passwordEncoder.encode(firma.getPassword()));
        auftraggeberFirmaService.save(firma);
    }

    @PostMapping("/firma/{firmaId}/mitarbeiter/{mitarbeiterId}")
    public void addMitarbeiterToCompany(@PathVariable final Long firmaId,
                                        @PathVariable final Long mitarbeiterId,
                                        @RequestParam Schicht schicht) {
        auftraggeberFirmaService.addMitarbeiterToCompany(mitarbeiterId, firmaId, schicht);
    }
}
