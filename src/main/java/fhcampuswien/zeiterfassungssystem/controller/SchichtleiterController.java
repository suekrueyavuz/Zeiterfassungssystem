package fhcampuswien.zeiterfassungssystem.controller;

import fhcampuswien.zeiterfassungssystem.entity.Mitarbeiter;
import fhcampuswien.zeiterfassungssystem.requestDTO.ArbeitszeitBearbeitenDTO;
import fhcampuswien.zeiterfassungssystem.service.AusgelieheneMitarbeiterService;
import fhcampuswien.zeiterfassungssystem.service.MitarbeiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("schichtleiter")
public class SchichtleiterController {
    private MitarbeiterService mitarbeiterService;
    private AusgelieheneMitarbeiterService ausgelieheneMitarbeiterService;

    @Autowired
    public SchichtleiterController(MitarbeiterService mitarbeiterService, AusgelieheneMitarbeiterService ausgelieheneMitarbeiterService) {
        this.mitarbeiterService = mitarbeiterService;
        this.ausgelieheneMitarbeiterService = ausgelieheneMitarbeiterService;
    }

    @GetMapping("/{schichtleiterId}")
    public ResponseEntity<Mitarbeiter> getSchichtleiter(@PathVariable Long schichtleiterId) {
        return new ResponseEntity<>(mitarbeiterService.getMitarbeiter(schichtleiterId), HttpStatus.OK);
    }

    @PostMapping("/mitarbeiter/{mitarbeiterId}")
    public void arbeitszeitenStatusBearbeiten(@PathVariable Long mitarbeiterId,
                                              @RequestBody ArbeitszeitBearbeitenDTO dto) {
        mitarbeiterService.arbeitzeitenStatusBearbeiten(mitarbeiterId, dto.getFirmaId(),
                dto.getArbeitstag(), dto.getZeitStatus());
    }

    @PutMapping("/firma/{firmaId}/mitarbeiter/{mitarbeiterId}")
    public void markiereAlsFeiertag(@PathVariable Long mitarbeiterId,
                                    @PathVariable Long firmaId,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate tag,
                                    @RequestParam boolean isFeiertag) {
        mitarbeiterService.markiereAlsFeiertag(mitarbeiterId, firmaId, tag, isFeiertag);
    }

    @PostMapping("/firma/{firmaId}/mitarbeiter/{mitarbeiterId}")
    public void markiereAlsUeberstunde(@PathVariable Long mitarbeiterId,
                                       @PathVariable Long firmaId,
                                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate tag) {
        mitarbeiterService.markiereAlsUeberstunde(mitarbeiterId, firmaId, tag);
    }
}
