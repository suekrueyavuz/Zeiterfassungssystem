package fhcampuswien.zeiterfassungssystem.controller;

import fhcampuswien.zeiterfassungssystem.entity.AusgelieheneMitarbeiter;
import fhcampuswien.zeiterfassungssystem.entity.Mitarbeiter;
import fhcampuswien.zeiterfassungssystem.requestDTO.ArbeitszeitEintragenDTO;
import fhcampuswien.zeiterfassungssystem.service.MitarbeiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("mitarbeiter")
public class MitarbeiterController {
    private MitarbeiterService mitarbeiterService;

    @Autowired
    public MitarbeiterController (MitarbeiterService mitarbeiterService) {
        this.mitarbeiterService = mitarbeiterService;
    }

    @PostMapping("/{mitarbeiterId}/firma/{firmaId}")
    public void arbeitszeitenEintragen(@PathVariable Long mitarbeiterId,
                                      @PathVariable Long firmaId,
                                      @RequestBody ArbeitszeitEintragenDTO dto) {
        mitarbeiterService.arbeitszeitenEintragen(dto.getStartZeit(), dto.getEndZeit(),
                mitarbeiterId, firmaId, dto.getArbeitstag());
    }

    @GetMapping("/{mitarbeiterId}")
    public ResponseEntity<List<AusgelieheneMitarbeiter>> getAusleihungen(@PathVariable Long mitarbeiterId) {
        return new ResponseEntity<>(mitarbeiterService.getAusleihungen(mitarbeiterId), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<Mitarbeiter> getMitarbeiter(@RequestParam String username) {
        return new ResponseEntity<>(mitarbeiterService.getMitarbeiterByUsername(username), HttpStatus.OK);
    }

    @PutMapping("/{mitarbeiterId}/firma/{firmaId}")
    public void markiereAlsFeiertag(@PathVariable Long mitarbeiterId,
                                    @PathVariable Long firmaId,
                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate tag,
                                    @RequestParam boolean isFeiertag) {
        mitarbeiterService.markiereAlsFeiertag(mitarbeiterId, firmaId, tag, isFeiertag);
    }

}
