package controller;

import model.DataModel;
import model.StatisticsCalculator;
import view.MainView;
import view.ErrorDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.apache.commons.math3.stat.StatUtils;

public class MainController {
    private MainView view;
    private DataModel model;

    public MainController(MainView view, DataModel model) {
        this.view = view;
        this.model = model;
        
        view.setImportButtonListener(new ImportButtonListener());
        view.setExportButtonListener(new ExportButtonListener());
        view.setExitButtonListener(new ExitButtonListener());
    }

    class ImportButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                File file = view.showFileOpenDialog();
                if (file != null) {
                    model.loadDataFromExcel(file);
                    calculateAndDisplayStatistics();
                }
            } catch (Exception ex) {
                ErrorDialog.showError("Ошибка при импорте данных");
            }
        }
    }

    class ExportButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (model.getDataColumns().isEmpty()) {
                    ErrorDialog.showError("Нет данных для экспорта");
                    return;
                }
                
                File file = view.showFileSaveDialog();
                if (file != null) {
                    List<String> statsNames = new ArrayList<>();
                    List<double[]> statsResults = new ArrayList<>();
                    
                    prepareStatisticsData(statsNames, statsResults);
                    model.saveResultsToExcel(file, statsNames, statsResults);
                    
                    JOptionPane.showMessageDialog(view, 
                        "Результаты экспортированы в файл: " + file.getPath(),
                        "Экспорт завершен",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                ErrorDialog.showError("Ошибка при экспорте данных ");
            }
        }
    }

    class ExitButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    private void calculateAndDisplayStatistics() {
        List<List<Double>> dataColumns = model.getDataColumns();
        List<String> columnNames = model.getColumnNames();
        
        StringBuilder results = new StringBuilder();
        results.append("Статистические показатели:\n\n");
        
        for (int i = 0; i < dataColumns.size(); i++) {
            results.append("Колонка: ").append(columnNames.get(i)).append("\n");
            List<Double> columnData = dataColumns.get(i);
            
            results.append(String.format("Среднее арифметическое: %.4f\n", 
                StatisticsCalculator.calculateMean(columnData)));
            results.append(String.format("Среднее геометрическое: %.4f\n", 
                StatisticsCalculator.calculateGeometricMean(columnData)));
            results.append(String.format("Стандартное отклонение: %.4f\n", 
                StatisticsCalculator.calculateStandardDeviation(columnData)));
            results.append(String.format("Размах: %.4f\n", 
                StatisticsCalculator.calculateRange(columnData)));
            results.append(String.format("Дисперсия: %.4f\n", 
                StatisticsCalculator.calculateVariance(columnData)));
            results.append(String.format("Коэффициент вариации: %.4f\n", 
                StatisticsCalculator.calculateVariationCoefficient(columnData)));
            
            double[] confInterval = StatisticsCalculator.calculateConfidenceInterval(columnData, 0.95);
            results.append(String.format("95%% доверительный интервал: [%.4f, %.4f]\n", 
                confInterval[0], confInterval[1]));
            
            results.append(String.format("Минимум: %.4f\n", 
                StatUtils.min(columnData.stream().mapToDouble(Double::doubleValue).toArray())));
            results.append(String.format("Максимум: %.4f\n", 
                StatUtils.max(columnData.stream().mapToDouble(Double::doubleValue).toArray())));
            results.append(String.format("Количество элементов: %d\n\n", columnData.size()));
        }
       
        if (dataColumns.size() > 1) {
            results.append("Коэффициенты ковариации:\n");
            for (int i = 0; i < dataColumns.size(); i++) {
                for (int j = i + 1; j < dataColumns.size(); j++) {
                    double cov = StatisticsCalculator.calculateCovariance(dataColumns.get(i), dataColumns.get(j));
                    results.append(String.format("  %s и %s: %.4f\n", 
                        columnNames.get(i), columnNames.get(j), cov));
                }
            }
        }
        
        view.displayResults(results.toString());
    }

    private void prepareStatisticsData(List<String> statsNames, List<double[]> statsResults) {
        List<List<Double>> dataColumns = model.getDataColumns();
        
        statsNames.add("Среднее арифметическое");
        statsNames.add("Среднее геометрическое");
        statsNames.add("Стандартное отклонение");
        statsNames.add("Размах");
        statsNames.add("Дисперсия");
        statsNames.add("Коэффициент вариации");
        statsNames.add("Доверительный интервал (нижняя граница)");
        statsNames.add("Доверительный интервал (верхняя граница)");
        statsNames.add("Минимум");
        statsNames.add("Максимум");
        statsNames.add("Количество элементов");
        
        double[] means = new double[dataColumns.size()];
        double[] geomMeans = new double[dataColumns.size()];
        double[] stdDevs = new double[dataColumns.size()];
        double[] ranges = new double[dataColumns.size()];
        double[] variances = new double[dataColumns.size()];
        double[] varCoeffs = new double[dataColumns.size()];
        double[] confIntLower = new double[dataColumns.size()];
        double[] confIntUpper = new double[dataColumns.size()];
        double[] mins = new double[dataColumns.size()];
        double[] maxs = new double[dataColumns.size()];
        double[] counts = new double[dataColumns.size()];
        
        for (int i = 0; i < dataColumns.size(); i++) {
            List<Double> columnData = dataColumns.get(i);
            
            means[i] = StatisticsCalculator.calculateMean(columnData);
            geomMeans[i] = StatisticsCalculator.calculateGeometricMean(columnData);
            stdDevs[i] = StatisticsCalculator.calculateStandardDeviation(columnData);
            ranges[i] = StatisticsCalculator.calculateRange(columnData);
            variances[i] = StatisticsCalculator.calculateVariance(columnData);
            varCoeffs[i] = StatisticsCalculator.calculateVariationCoefficient(columnData);
            
            double[] confInterval = StatisticsCalculator.calculateConfidenceInterval(columnData, 0.95);
            confIntLower[i] = confInterval[0];
            confIntUpper[i] = confInterval[1];
            
            mins[i] = StatUtils.min(columnData.stream().mapToDouble(Double::doubleValue).toArray());
            maxs[i] = StatUtils.max(columnData.stream().mapToDouble(Double::doubleValue).toArray());
            counts[i] = columnData.size();
        }
        
        statsResults.add(means);
        statsResults.add(geomMeans);
        statsResults.add(stdDevs);
        statsResults.add(ranges);
        statsResults.add(variances);
        statsResults.add(varCoeffs);
        statsResults.add(confIntLower);
        statsResults.add(confIntUpper);
        statsResults.add(mins);
        statsResults.add(maxs);
        statsResults.add(counts);
    }
}
