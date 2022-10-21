package fhcampuswien.zeiterfassungssystem.controller;

import fhcampuswien.zeiterfassungssystem.requestDTO.ArbeitszeitEintragenDTO;
import fhcampuswien.zeiterfassungssystem.service.MitarbeiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

}
