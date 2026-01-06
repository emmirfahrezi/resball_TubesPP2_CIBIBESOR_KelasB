package koneksiDB;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class koneksiDB {
    private static Connection mysqlconfig;
    public static Connection configDB() throws SQLException {
        try {
            String url = "jdbc:mysql://localhost:3306/resball"; // sesuaikan URL database Anda
            String user = "root";
            String pass = ""; // sesuaikan password MySQL Anda

            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());

            mysqlconfig = DriverManager.getConnection(url, user, pass);
            

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Koneksi ke database gagal: " + e.getMessage());
        }
        return mysqlconfig;
    }
}
