package ResBall;

import javax.swing.UIManager;
import javax.swing.text.View;

import controller.controllerLapangan;
import controller.controllerPelanggan;
import controller.controllerReservasi;
import view.viewLapangan;
import view.viewPelanggan;
import view.viewReservasi;

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

            // menampilkan View Reservasi
            viewReservasi vReservasi = new viewReservasi();
            new controllerReservasi(vReservasi); 
            vReservasi.setVisible(true);
            
        });

    }
}
