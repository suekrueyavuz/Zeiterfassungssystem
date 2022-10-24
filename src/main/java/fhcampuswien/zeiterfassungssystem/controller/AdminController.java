package fhcampuswien.zeiterfassungssystem.controller;

import fhcampuswien.zeiterfassungssystem.entity.AuftraggeberFirma;
import fhcampuswien.zeiterfassungssystem.entity.Mitarbeiter;
import fhcampuswien.zeiterfassungssystem.service.AuftraggeberFirmaService;
import fhcampuswien.zeiterfassungssystem.service.MitarbeiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin")
public class AdminController {
    private MitarbeiterService mitarbeiterService;
    private AuftraggeberFirmaService auftraggeberFirmaService;

    @Autowired
    public AdminController(MitarbeiterService mitarbeiterService,
                           AuftraggeberFirmaService auftraggeberFirmaService) {
        this.mitarbeiterService = mitarbeiterService;
        this.auftraggeberFirmaService = auftraggeberFirmaService;
    }

    @PostMapping("/mitarbeiter")
    public ResponseEntity<Mitarbeiter> createNewMitarbeiter(@RequestBody Mitarbeiter mitarbeiter) {
        Mitarbeiter newMitarbeiter = mitarbeiterService.save(mitarbeiter);
        return new ResponseEntity<>(newMitarbeiter, HttpStatus.OK);
    }

    @DeleteMapping("/mitarbeiter/{mitarbeiterId}")
    public void removeMitarbeiter(@PathVariable final Long mitarbeiterId) {
        mitarbeiterService.removeMitarbeiter(mitarbeiterId);
    }

    @DeleteMapping("/firma/{firmaId}")
    public void deleteCompany(@PathVariable final Long firmaId){auftraggeberFirmaService.remove(firmaId);}
    @PostMapping("/firma")
    public void createNewCompany(@RequestBody AuftraggeberFirma firma) {
        auftraggeberFirmaService.save(firma);
    }

    @PostMapping("/firma/{firmaId}/mitarbeiter/{mitarbeiterId}")
    public void addMitarbeiterToCompany(@PathVariable final Long firmaId, @PathVariable final Long mitarbeiterId) {
        auftraggeberFirmaService.addMitarbeiterToCompany(mitarbeiterId, firmaId);
    }
}
