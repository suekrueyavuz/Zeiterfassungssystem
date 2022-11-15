package fhcampuswien.zeiterfassungssystem.controller;

import fhcampuswien.zeiterfassungssystem.Enum.Schicht;
import fhcampuswien.zeiterfassungssystem.entity.AuftraggeberFirma;
import fhcampuswien.zeiterfassungssystem.entity.Mitarbeiter;
import fhcampuswien.zeiterfassungssystem.service.AuftraggeberFirmaService;
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

    private PasswordEncoder passwordEncoder;
    private AuftraggeberFirmaService auftraggeberFirmaService;

    @Autowired
    public AdminController(MitarbeiterService mitarbeiterService,
                           AuftraggeberFirmaService auftraggeberFirmaService, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.mitarbeiterService = mitarbeiterService;
        this.auftraggeberFirmaService = auftraggeberFirmaService;
    }

    @PostMapping("/mitarbeiter")
    public ResponseEntity<Mitarbeiter> createNewMitarbeiter(@RequestBody Mitarbeiter mitarbeiter) {
        Mitarbeiter newMitarbeiter = mitarbeiterService.save(mitarbeiter);
        return new ResponseEntity<>(newMitarbeiter, HttpStatus.OK);
    }

    @GetMapping("/mitarbeiter")
    public ResponseEntity<List<Mitarbeiter>> getAllMitarbeiter() {
        List<Mitarbeiter> mitarbeiterList = mitarbeiterService.getAll();
        return new ResponseEntity<>(mitarbeiterList, HttpStatus.OK);
    }

    @GetMapping("/firma")
    public ResponseEntity<List<AuftraggeberFirma>> getAllFirmen() {
        List<AuftraggeberFirma> firmenList = auftraggeberFirmaService.getAll();
        return new ResponseEntity<>(firmenList, HttpStatus.OK);
    }

    @PutMapping("/mitarbeiter/{mitarbeiterId}")
    public ResponseEntity<String> resetEmployeePass(@PathVariable final Long mitarbeiterId){
        String newpass = UUID.randomUUID().toString().substring(10);
        newpass = passwordEncoder.encode(newpass);
        return ResponseEntity.ok(mitarbeiterService.resetPassword(mitarbeiterId, newpass));
    }

    @PutMapping("/firma/{firmaId}")
    public ResponseEntity<String> resetCompanyPass(@PathVariable final Long firmaId){
        String newpass = UUID.randomUUID().toString().substring(10);
        newpass = passwordEncoder.encode(newpass);
        return ResponseEntity.ok(auftraggeberFirmaService.resetPassword(firmaId, newpass));
    }
    @DeleteMapping("/mitarbeiter/{mitarbeiterId}")
    public void removeMitarbeiter(@PathVariable final Long mitarbeiterId) {
        mitarbeiterService.removeMitarbeiter(mitarbeiterId);
    }

    @PutMapping("/mitarbeiter")
    public void editMitarbeiter(@RequestBody Mitarbeiter mitarbeiter) {
        mitarbeiterService.editMitarbeiter(mitarbeiter);
    }

    @DeleteMapping("/firma/{firmaId}")
    public void deleteCompany(@PathVariable final Long firmaId){auftraggeberFirmaService.remove(firmaId);}
    @PostMapping("/firma")
    public void createNewCompany(@RequestBody AuftraggeberFirma firma) {
        auftraggeberFirmaService.save(firma);
    }

    @PostMapping("/firma/{firmaId}/mitarbeiter/{mitarbeiterId}")
    public void addMitarbeiterToCompany(@PathVariable final Long firmaId,
                                        @PathVariable final Long mitarbeiterId,
                                        @RequestParam Schicht schicht) {
        auftraggeberFirmaService.addMitarbeiterToCompany(mitarbeiterId, firmaId, schicht);
    }
}
