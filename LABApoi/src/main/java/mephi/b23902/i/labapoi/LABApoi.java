
package mephi.b23902.i.labapoi;

import controller.MainController;
import model.DataModel;
import view.MainView;

/**
 *
 * @author tsyga
 */
public class LABApoi {
    public static void main(String[] args) {
        DataModel model = new DataModel();
        MainView view = new MainView();
        MainController controller = new MainController(view, model);
        
        view.setVisible(true);
    }
}