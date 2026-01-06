package ResBall;

import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.UIManager;

import koneksiDB.koneksiDB;
import view.viewPelanggan;


public class main {
    public static void main(String[] args) throws SQLException {
       // Mengatur tampilan agar terlihat modern
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}

        // Jalankan View
        java.awt.EventQueue.invokeLater(() -> {
            new viewPelanggan().setVisible(true);
        });
    }
}
