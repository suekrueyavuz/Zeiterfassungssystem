package fhcampuswien.zeiterfassungssystem.controller;

import fhcampuswien.zeiterfassungssystem.requestDTO.ArbeitszeitBearbeitenDTO;
import fhcampuswien.zeiterfassungssystem.service.MitarbeiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("schichtleiter")
public class SchichtleiterController {
    private MitarbeiterService mitarbeiterService;

    @Autowired
    public SchichtleiterController(MitarbeiterService mitarbeiterService) {
        this.mitarbeiterService = mitarbeiterService;
    }

    @PostMapping("/mitarbeiter/{mitarbeiterId}")
    public void arbeitszeitenStatusBearbeiten(@PathVariable Long mitarbeiterId,
                                              @RequestBody ArbeitszeitBearbeitenDTO dto) {
        mitarbeiterService.arbeitzeitenStatusBearbeiten(mitarbeiterId, dto.getFirmaId(),
                dto.getArbeitstag(), dto.getZeitStatus());
    }
}
