package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;

public class MainView extends JFrame {
    private JButton importButton;
    private JButton exportButton;
    private JButton exitButton;
    private JTextArea resultArea;

    public MainView() {
        setTitle("Лабораторная работа 1");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        JPanel buttonPanel = new JPanel();
        importButton = new JButton("Импорт данных");
        exportButton = new JButton("Экспорт результатов");
        exitButton = new JButton("Выход");
        
        buttonPanel.add(importButton);
        buttonPanel.add(exportButton);
        buttonPanel.add(exitButton);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        getContentPane().add(buttonPanel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }

    public void setImportButtonListener(ActionListener listener) {
        importButton.addActionListener(listener);
    }

    public void setExportButtonListener(ActionListener listener) {
        exportButton.addActionListener(listener);
    }

    public void setExitButtonListener(ActionListener listener) {
        exitButton.addActionListener(listener);
    }

    public void displayResults(String results) {
        resultArea.setText(results);
    }

    public File showFileOpenDialog() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel Files", "xlsx"));
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }

    public File showFileSaveDialog() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel Files", "xlsx"));
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        }
        return null;
    }
}

