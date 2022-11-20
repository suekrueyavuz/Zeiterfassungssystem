package fhcampuswien.zeiterfassungssystem.report;

import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;


@Getter
@Setter
public class ReportGenerator {
    private static final int SPALTE_NAME = 0;
    private static final int SPALTE_SCHICHT = 1;
    private static final int SPALTE_PREIS_PRO_STUNDE = 2;
    private static final int SPALTE_KW = 3;
    private static final int SPALTE_ERSTE_SCHICHT = 4;
    private static final int SPALTE_ZWEITE_SCHICHT = 5;
    private static final int SPALTE_DRITTE_SCHICHT = 6;
    private static final int SPALTE_U_STD = 7;
    private static final int SPALTE_U_STD_FT = 8;
    private static final int SPALTE_BETRAG = 9;

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<Report> reports;

    public ReportGenerator(List<Report> reports) {
        this.reports = reports;
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Report");
    }

    private void writeHeader(int rowNum) {
        Row row = sheet.createRow(rowNum);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        createCell(row, SPALTE_NAME, "Name", style);
        createCell(row, SPALTE_SCHICHT, "Schicht", style);
        createCell(row, SPALTE_PREIS_PRO_STUNDE, "Preis/Stunde", style);
        createCell(row, SPALTE_KW, "KW34", style);
        createCell(row, SPALTE_ERSTE_SCHICHT, "1. Schicht", style);
        createCell(row, SPALTE_ZWEITE_SCHICHT, "2. Schicht", style);
        createCell(row, SPALTE_DRITTE_SCHICHT, "3. Schicht", style);
        createCell(row, SPALTE_U_STD, "U-Std", style);
        createCell(row, SPALTE_U_STD_FT, "U-Std (So+Ft)", style);
        createCell(row, SPALTE_BETRAG, "Betrag", style);
    }

    public void writeData(HttpServletResponse response) throws IOException {
        CellStyle style = workbook.createCellStyle();
        int rowNum = 0;

        for(Report report: reports) {
            writeHeader(rowNum++);
            double betragErsteSchicht = report.getErsteSchichtFirma() * report.getErsteSchichtMitarbeiter();
            double betragZweiteSchicht = report.getZweiteSchichtFirma() * report.getZweiteSchichtMitarbeiter();
            double betragDritteSchicht = report.getDritteSchichtFirma() * report.getDritteSchichtMitarbeiter();
            if(report.isFeiertag()) {
                betragErsteSchicht += betragErsteSchicht;
                betragZweiteSchicht += betragZweiteSchicht;
                betragDritteSchicht += betragDritteSchicht;
            }

            Row row1 = sheet.createRow(rowNum++);
            Row row2 = sheet.createRow(rowNum++);
            Row row3 = sheet.createRow(rowNum++);
            Row row4 = sheet.createRow(rowNum++);
            Row row5 = sheet.createRow(rowNum++);

            createCell(row1, SPALTE_NAME, report.getMitarbeiterName(), style);
            createCell(row1, SPALTE_SCHICHT, "1. Schicht", style);
            createCell(row1, SPALTE_KW, report.getErsteSchichtMitarbeiter(), style);
            createCell(row1, SPALTE_PREIS_PRO_STUNDE, report.getErsteSchichtFirma(), style);
            createCell(row1, SPALTE_ERSTE_SCHICHT, report.getErsteSchichtMitarbeiter(), style);
            createCell(row1, SPALTE_BETRAG, betragErsteSchicht, style);

            createCell(row2, SPALTE_SCHICHT, "2. Schicht", style);
            createCell(row2, SPALTE_KW, report.getZweiteSchichtMitarbeiter(), style);
            createCell(row2, SPALTE_PREIS_PRO_STUNDE, report.getZweiteSchichtFirma(), style);
            createCell(row2, SPALTE_ZWEITE_SCHICHT, report.getZweiteSchichtMitarbeiter(), style);
            createCell(row2, SPALTE_BETRAG, betragZweiteSchicht, style);

            createCell(row3, SPALTE_SCHICHT, "3. Schicht", style);
            createCell(row3, SPALTE_KW, report.getDritteSchichtMitarbeiter(), style);
            createCell(row3, SPALTE_DRITTE_SCHICHT, report.getDritteSchichtMitarbeiter(), style);
            createCell(row3, SPALTE_BETRAG, betragDritteSchicht, style);

            createCell(row3, SPALTE_PREIS_PRO_STUNDE, report.getDritteSchichtFirma(), style);

            createCell(row4, SPALTE_SCHICHT, "U-Std", style);

            createCell(row5, SPALTE_SCHICHT, "U-Std (So+Ft)", style);
        }
        generateReport(response);
    }

    private void createCell(Row row, int columnIndex, Object valueOfCell, CellStyle style) {
        Cell cell = row.createCell(columnIndex);
        sheet.autoSizeColumn(columnIndex);
        if (valueOfCell instanceof Integer) {
            cell.setCellValue((Integer) valueOfCell);
        } else if (valueOfCell instanceof Long) {
            cell.setCellValue((Long) valueOfCell);
        } else if (valueOfCell instanceof String) {
            cell.setCellValue((String) valueOfCell);
        } else if (valueOfCell instanceof Double) {
            cell.setCellValue((Double) valueOfCell);
        } else {
            cell.setCellValue((Boolean) valueOfCell);
        }
        cell.setCellStyle(style);
    }

    private void generateReport(HttpServletResponse response) throws IOException {
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
