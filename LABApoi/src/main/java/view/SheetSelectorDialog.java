
package view;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;

public class SheetSelectorDialog extends JDialog {
    private JComboBox<String> sheetCombo;
    private int selectedIndex = -1;

    public SheetSelectorDialog(Frame parent, List<String> sheetNames) {
        super(parent, "Выбор листа", true);
        
        sheetCombo = new JComboBox<>();
        for (String name : sheetNames) {
            sheetCombo.addItem(name);
        }
        
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            selectedIndex = sheetCombo.getSelectedIndex();
            dispose();
        });
        
        setLayout(new BorderLayout());
        add(sheetCombo, BorderLayout.CENTER);
        add(okButton, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(parent);
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }
}
