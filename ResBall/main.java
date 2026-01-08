package ResBall;

import javax.swing.*;
import view.viewLapangan;
import view.viewPelanggan;
import view.viewReservasi;
import controller.controllerLapangan;
import controller.controllerPelanggan;
import controller.controllerReservasi;

public class main extends JFrame {

    public main(){
        setTitle("Aplikasi Sewa Lapangan");
        setSize(900,700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // TAB
        JTabbedPane tabs = new JTabbedPane();

        // ====== PELANGGAN ======
        viewPelanggan vPelanggan = new viewPelanggan();
        new controllerPelanggan(vPelanggan);
        tabs.add("Pelanggan", vPelanggan.getContentPane());

        // ====== LAPANGAN ======
        viewLapangan vLapangan = new viewLapangan();
        new controllerLapangan(vLapangan);
        tabs.add("Lapangan", vLapangan.getContentPane());

        // ====== RESERVASI ======
        viewReservasi vReservasi = new viewReservasi();
        new controllerReservasi(vReservasi);
        tabs.add("Reservasi", vReservasi.getContentPane());

        add(tabs);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new main().setVisible(true);
        });
    }
}
