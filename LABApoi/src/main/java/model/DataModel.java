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
    private List<List<List<Double>>> sheetsData;
    private List<List<String>> sheetsColumnNames;
    private List<String> sheetNames;
    private int currentSheetIndex;

    public DataModel() {
        sheetsData = new ArrayList<>();
        sheetsColumnNames = new ArrayList<>();
        sheetNames = new ArrayList<>();
        currentSheetIndex = 0;
    }

    public void loadDataFromExcel(File file) throws IOException {
    sheetsData.clear();
    sheetsColumnNames.clear();
    sheetNames.clear();

    try (FileInputStream fis = new FileInputStream(file);
         Workbook workbook = new XSSFWorkbook(fis)) {

        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();

        for (int sheetIndex = 0; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            sheetNames.add(sheet.getSheetName());

            List<List<Double>> sheetData = new ArrayList<>();
            List<String> columnNames = new ArrayList<>();

            Row headerRow = sheet.getRow(0);
            if (headerRow == null) continue;

            for (Cell cell : headerRow) {
                columnNames.add(cell.getStringCellValue());
                sheetData.add(new ArrayList<>());
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    for (int j = 0; j < columnNames.size(); j++) {
                        Cell cell = row.getCell(j, Row.MissingCellPolicy.RETURN_NULL_AND_BLANK);
                        if (cell != null) {
                            switch (cell.getCellType()) {
                                case NUMERIC:
                                    sheetData.get(j).add(cell.getNumericCellValue());
                                    break;
                                case FORMULA:
                                    CellValue cellValue = evaluator.evaluate(cell);
                                    if (cellValue.getCellType() == CellType.NUMERIC) {
                                        sheetData.get(j).add(cellValue.getNumberValue());
                                    } else {
                                        sheetData.get(j).add(0.0);
                                    }
                                    break;
                                default:
                                    sheetData.get(j).add(0.0);
                            }
                        }
                    }
                }
            }

            sheetsData.add(sheetData);
            sheetsColumnNames.add(columnNames);
        }
        currentSheetIndex = 0; // первый лист по умолчанию
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

            List<String> columnNames = getColumnNames();
            for (int i = 0; i < columnNames.size(); i++) {
                headerCell = headerRow.createCell(i + 1);
                headerCell.setCellValue(columnNames.get(i));
                headerCell.setCellStyle(headerStyle);
            }

            for (int i = 0; i < statsNames.size(); i++) {
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(statsNames.get(i));
                double[] values = statsResults.get(i);
                for (int j = 0; j < values.length; j++) {
                    Cell cell = row.createCell(j + 1);
                    cell.setCellValue(values[j]);

                    CellStyle numberStyle = workbook.createCellStyle();
                    numberStyle.setDataFormat(workbook.createDataFormat().getFormat("0.0000"));
                    cell.setCellStyle(numberStyle);
                }
            }

            for (int i = 0; i <= columnNames.size(); i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fos = new FileOutputStream(file)) {
                workbook.write(fos);
            }
        }
    }

    public void setCurrentSheet(int index) {
        if (index >= 0 && index < sheetsData.size()) {
            currentSheetIndex = index;
        }
    }

    public List<List<Double>> getDataColumns() {
        return sheetsData.get(currentSheetIndex);
    }

    public List<String> getColumnNames() {
        return sheetsColumnNames.get(currentSheetIndex);
    }

    public List<String> getSheetNames() {
        return sheetNames;
    }

    public int getSheetsCount() {
        return sheetsData.size();
    }
}
