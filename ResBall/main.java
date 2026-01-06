package ResBall;

import javax.swing.UIManager;
import javax.swing.text.View;

import controller.controllerLapangan;
import controller.controllerPelanggan;
import view.viewLapangan;
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
            
            // Menampilkan View Lapangan
            viewLapangan vLapangan = new viewLapangan(); // Pastikan nama kelasnya sesuai
            new controllerLapangan(vLapangan);           // Pastikan controllernya juga ada
            vLapangan.setVisible(true);
        });

    }
}
