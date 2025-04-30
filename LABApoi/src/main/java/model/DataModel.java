package model;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataModel {
    private List<List<Double>> dataColumns;
    private List<String> columnNames;

    public DataModel() {
        dataColumns = new ArrayList<>();
        columnNames = new ArrayList<>();
    }

    public void loadDataFromExcel(File file) throws IOException {
        dataColumns.clear();
        columnNames.clear();

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);

           
            for (Cell cell : headerRow) {
                columnNames.add(cell.getStringCellValue());
                dataColumns.add(new ArrayList<>());
            }

            
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    for (int j = 0; j < columnNames.size(); j++) {
                        Cell cell = row.getCell(j);
                        if (cell != null && cell.getCellType() == CellType.NUMERIC) {
                            dataColumns.get(j).add(cell.getNumericCellValue());
                        }
                    }
                }
            }
        }
    }

    public void saveResultsToExcel(File file, List<String> statsNames, List<double[]> statsResults) throws IOException {
    String filePath = file.getAbsolutePath();
    if (!filePath.toLowerCase().endsWith(".xlsx")) {
        file = new File(filePath + ".xlsx");
    }

    try (Workbook workbook = new XSSFWorkbook()) {
        Sheet sheet = workbook.createSheet("Результаты");

       
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        Row headerRow = sheet.createRow(0);
        Cell headerCell = headerRow.createCell(0);
        headerCell.setCellValue("Вычисленные параметры");
        headerCell.setCellStyle(headerStyle);

        for (int i = 0; i < columnNames.size(); i++) {
            headerCell = headerRow.createCell(i + 1);
            headerCell.setCellValue(columnNames.get(i));
            headerCell.setCellStyle(headerStyle);
        }

        for (int i = 0; i < statsNames.size(); i++) {
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(statsNames.get(i));
            
            for (int j = 0; j < statsResults.get(i).length; j++) {
                Cell cell = row.createCell(j + 1);
                cell.setCellValue(statsResults.get(i)[j]);
                
                CellStyle numberStyle = workbook.createCellStyle();
                numberStyle.setDataFormat(workbook.createDataFormat().getFormat("0.0000"));
                cell.setCellStyle(numberStyle);
            }
        }

        for (int i = 0; i <= columnNames.size(); i++) {
            sheet.autoSizeColumn(i); //хорошая ширина колонок
        }

        // Сохранение файла
        try (FileOutputStream fos = new FileOutputStream(file)) {
            workbook.write(fos);
        }
    }
}

    public List<List<Double>> getDataColumns() {
        return dataColumns;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }
}
