package fhcampuswien.zeiterfassungssystem.controller;

import fhcampuswien.zeiterfassungssystem.service.AuftraggeberFirmaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/{firmaId}/report")
    public void exportReport(HttpServletResponse response, @PathVariable Long firmaId) {
        try {
            response.setContentType("application/octet-stream");
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=report.xlsx";
            response.setHeader(headerKey, headerValue);

            auftraggeberFirmaService.generateReport(response, firmaId);
        } catch (IOException e) {
            e.getMessage();
        }
    }

}
