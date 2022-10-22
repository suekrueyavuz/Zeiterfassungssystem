package fhcampuswien.zeiterfassungssystem.controller;

import fhcampuswien.zeiterfassungssystem.service.AuftraggeberFirmaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
    public void exportReport(HttpServletResponse response) {
        try {
            response.setContentType("application/octet-stream");
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=report.xlsx";
            response.setHeader(headerKey, headerValue);

            auftraggeberFirmaService.generateReport(response);
        } catch (IOException e) {
            e.getMessage();
        }
    }

}
