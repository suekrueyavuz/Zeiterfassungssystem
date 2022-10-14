package fhcampuswien.zeiterfassungssystem.controller;

import fhcampuswien.zeiterfassungssystem.entity.AuftraggeberFirma;
import fhcampuswien.zeiterfassungssystem.service.AuftraggeberFirmaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuftraggeberFirmaController {
    private AuftraggeberFirmaService auftraggeberFirmaService;

    @Autowired
    public AuftraggeberFirmaController(AuftraggeberFirmaService auftraggeberFirmaService) {
        this.auftraggeberFirmaService = auftraggeberFirmaService;
    }

    @PostMapping("/firma")
    public void createNewCompany(@RequestBody AuftraggeberFirma firma) {
        auftraggeberFirmaService.createNewCompany(firma);
    }

    @PostMapping("/firma/{firmaId}/mitarbeiter/{mitarbeiterId}")
    public void addMitarbeiterToCompany(@PathVariable final Long firmaId, @PathVariable final Long mitarbeiterId) {
        auftraggeberFirmaService.addMitarbeiterToCompany(mitarbeiterId, firmaId);
    }
}
