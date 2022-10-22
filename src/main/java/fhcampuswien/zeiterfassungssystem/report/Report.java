package fhcampuswien.zeiterfassungssystem.report;

import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;


@Getter
@Setter
public class Report {
    private String mitarbeiterName;
    private String ersteSchicht;
    private String zweiteSchicht;

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public Report() {
        workbook = new XSSFWorkbook();
    }

    private void writeHeader() {
        sheet = workbook.createSheet("Report");
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
//        XSSFFont font = workbook.createFont();
//        font.setBold(true);
//        font.setFontHeight(16);
//        style.setFont(font);
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

    private void createCell(Row row, int columnIndex, Object valueOfCell, CellStyle style) {
        Cell cell = row.createCell(columnIndex);
        sheet.autoSizeColumn(columnIndex);
        if (valueOfCell instanceof Integer) {
            cell.setCellValue((Integer) valueOfCell);
        } else if (valueOfCell instanceof Long) {
            cell.setCellValue((Long) valueOfCell);
        } else if (valueOfCell instanceof String) {
            cell.setCellValue((String) valueOfCell);
        } else {
            cell.setCellValue((Boolean) valueOfCell);
        }
        cell.setCellStyle(style);
    }

    public void generateReport(HttpServletResponse response) throws IOException {
        writeHeader();
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
