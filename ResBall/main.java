package ResBall;

import javax.swing.UIManager;
import controller.controllerPelanggan;
import view.viewPelanggan;

public class main {
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }

        java.awt.EventQueue.invokeLater(() -> {
            viewPelanggan view = new viewPelanggan();
            new controllerPelanggan(view);
            view.setVisible(true);
        });
    }
}
