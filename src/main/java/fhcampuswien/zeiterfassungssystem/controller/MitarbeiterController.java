package fhcampuswien.zeiterfassungssystem.controller;

import fhcampuswien.zeiterfassungssystem.entity.Mitarbeiter;
import fhcampuswien.zeiterfassungssystem.service.MitarbeiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MitarbeiterController {
    private MitarbeiterService mitarbeiterService;

    @Autowired
    public MitarbeiterController (MitarbeiterService mitarbeiterService) {
        this.mitarbeiterService = mitarbeiterService;
    }

    @PostMapping
    public ResponseEntity<Mitarbeiter> createNewMitarbeiter(@RequestBody Mitarbeiter mitarbeiter) {
        Mitarbeiter newMitarbeiter = mitarbeiterService.createNewMitarbeiter(mitarbeiter);
        return new ResponseEntity<>(newMitarbeiter, HttpStatus.OK);
    }
}
