package view;

import javax.swing.*;

public class ErrorDialog {
    public static void showError(String message) {
        JOptionPane.showMessageDialog(null, message, "Ошибка", JOptionPane.ERROR_MESSAGE);
    }
}
