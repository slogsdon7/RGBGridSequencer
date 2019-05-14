import controller.Controller;
import launchpad.Device;
import model.DB;
import views.View;

import javax.swing.*;

public class Main {
    public static void main(String[] args){
        DB.init();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Device.setupReceiver();
                Controller controller = new Controller();
            }
        });
    }
}
