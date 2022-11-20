package fhcampuswien.zeiterfassungssystem.controller;

import fhcampuswien.zeiterfassungssystem.entity.AuftraggeberFirma;
import fhcampuswien.zeiterfassungssystem.entity.AusgelieheneMitarbeiter;
import fhcampuswien.zeiterfassungssystem.requestDTO.PasswortAendernDTO;
import fhcampuswien.zeiterfassungssystem.service.AuftraggeberFirmaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("firma")
public class AuftraggeberFirmaController {
    private AuftraggeberFirmaService auftraggeberFirmaService;

    @Autowired
    public AuftraggeberFirmaController(AuftraggeberFirmaService auftraggeberFirmaService) {
        this.auftraggeberFirmaService = auftraggeberFirmaService;
    }

    @PutMapping("/{firmaId}")
    public void passwortAendern(@PathVariable Long firmaId, @RequestBody PasswortAendernDTO dto) {
        auftraggeberFirmaService.passwortAendern(firmaId, dto.getAltesPasswort(), dto.getNeuesPasswort());
    }

    @GetMapping("/{firmaId}/report")
    public void exportReport(HttpServletResponse response, @PathVariable Long firmaId) {
        AuftraggeberFirma firma = auftraggeberFirmaService.getFirmaById(firmaId);
        try {
            response.setContentType("application/octet-stream");
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=Report-" + firma.getName() + ".xlsx";
            response.setHeader(headerKey, headerValue);

            auftraggeberFirmaService.generateReportFuerFirma(response, firmaId);
        } catch (IOException e) {
            e.getMessage();
        }
    }

    @GetMapping("/{firmaId}/export")
    public void exportReportFuerZeitraum(HttpServletResponse response, @PathVariable Long firmaId,
                                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate von,
                                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate bis) {
        AuftraggeberFirma firma = auftraggeberFirmaService.getFirmaById(firmaId);
        try {
            response.setContentType("application/octet-stream");
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=Report-" + firma.getName() + ".xlsx";
            response.setHeader(headerKey, headerValue);

            auftraggeberFirmaService.generateReportFuerZeitraum(response, firmaId, von, bis);
        } catch (IOException e) {
            e.getMessage();
        }
    }

    @GetMapping("/{firmaId}/ausgelieheneMitarbeiter")
    public ResponseEntity<List<AusgelieheneMitarbeiter>> getAusgelieheneMitarbeiter(@PathVariable final Long firmaId) {
        return new ResponseEntity<>(auftraggeberFirmaService.getAusgelieheneMitarbeiter(firmaId), HttpStatus.OK);
    }

}
