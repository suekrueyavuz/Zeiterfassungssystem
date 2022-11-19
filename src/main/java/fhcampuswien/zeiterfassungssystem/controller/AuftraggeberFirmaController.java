package fhcampuswien.zeiterfassungssystem.controller;

import fhcampuswien.zeiterfassungssystem.entity.AuftraggeberFirma;
import fhcampuswien.zeiterfassungssystem.requestDTO.PasswortAendernDTO;
import fhcampuswien.zeiterfassungssystem.service.AuftraggeberFirmaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

            auftraggeberFirmaService.generateReport(response, firmaId);
        } catch (IOException e) {
            e.getMessage();
        }
    }

}
