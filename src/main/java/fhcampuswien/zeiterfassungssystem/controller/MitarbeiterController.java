package fhcampuswien.zeiterfassungssystem.controller;

import fhcampuswien.zeiterfassungssystem.entity.Mitarbeiter;
import fhcampuswien.zeiterfassungssystem.requestDTO.ArbeitszeitBearbeitenDTO;
import fhcampuswien.zeiterfassungssystem.requestDTO.ArbeitszeitEintragenDTO;
import fhcampuswien.zeiterfassungssystem.service.MitarbeiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MitarbeiterController {
    private MitarbeiterService mitarbeiterService;

    @Autowired
    public MitarbeiterController (MitarbeiterService mitarbeiterService) {
        this.mitarbeiterService = mitarbeiterService;
    }

    @PostMapping("/mitarbeiter")
    public ResponseEntity<Mitarbeiter> createNewMitarbeiter(@RequestBody Mitarbeiter mitarbeiter) {
        Mitarbeiter newMitarbeiter = mitarbeiterService.createNewMitarbeiter(mitarbeiter);
        return new ResponseEntity<>(newMitarbeiter, HttpStatus.OK);
    }

    @PostMapping("/mitarbeiter/{mitarbeiterId}/firma/{firmaId}")
    public void startzeitEintragen(@PathVariable Long mitarbeiterId,
                                   @PathVariable Long firmaId,
                                   @RequestBody ArbeitszeitEintragenDTO dto) {
        mitarbeiterService.startZeitEintragen(dto.getStartZeit(), dto.getEndZeit(),
                mitarbeiterId, firmaId, dto.getArbeitstag());
    }

    @PostMapping("/schichtleiter/{schichtleiterId}/mitarbeiter/{mitarbeiterId}")
    public void arbeitszeitenStatusBearbeiten(@PathVariable Long schichtleiterId,
                                              @PathVariable Long mitarbeiterId,
                                              @RequestBody ArbeitszeitBearbeitenDTO dto) {
        mitarbeiterService.arbeitzeitenStatusBearbeiten(schichtleiterId, mitarbeiterId, dto.getFirmaId(),
                dto.getArbeitstag(), dto.getStatus());
    }
}
