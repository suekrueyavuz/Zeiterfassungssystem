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
public class ExcelGenerator {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<Report> reports;

    public ExcelGenerator(List<Report> reports) {
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

        createCell(row, 0, "Name", style);
        createCell(row, 1, "Schicht", style);
        createCell(row, 2, "Preis/Stunde", style);
        createCell(row, 3, "KW34", style);
        createCell(row, 4, "1. Schicht", style);
        createCell(row, 5, "2. Schicht", style);
        createCell(row, 6, "3. Schicht", style);
        createCell(row, 7, "U-Std", style);
        createCell(row, 8, "U-Std (So+Ft)", style);
        createCell(row, 9, "Betrag", style);
    }

    private void writeData() {
        CellStyle style = workbook.createCellStyle();
        int rowNum = 0;

        for(Report report: reports) {
            writeHeader(rowNum++);

            Row row1 = sheet.createRow(rowNum++);
            Row row2 = sheet.createRow(rowNum++);
            Row row3 = sheet.createRow(rowNum++);
            Row row4 = sheet.createRow(rowNum++);
            Row row5 = sheet.createRow(rowNum++);

            createCell(row1, 0, report.getMitarbeiterName(), style);
            createCell(row1, 1, "1. Schicht", style);
            createCell(row1, 2, report.getErsteSchichtFirma(), style);
            createCell(row1, 4, report.getErsteSchichtMitarbeiter(), style);

            createCell(row2, 1, "2. Schicht", style);
            createCell(row2, 2, report.getZweiteSchichtFirma(), style);
            createCell(row2, 5, report.getZweiteSchichtMitarbeiter(), style);

            createCell(row3, 1, "3. Schicht", style);
            createCell(row3, 6, report.getDritteSchichtMitarbeiter(), style);

            createCell(row3, 2, report.getDritteSchichtFirma(), style);

            createCell(row4, 1, "U-Std", style);

            createCell(row5, 1, "U-Std (So+Ft)", style);
        }

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

    public void generateReport(HttpServletResponse response) throws IOException {
        writeData();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
