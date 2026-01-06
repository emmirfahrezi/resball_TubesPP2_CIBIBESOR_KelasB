package ResBall;

import java.sql.Connection;
import java.sql.SQLException;

import koneksiDB.koneksiDB;


public class main {
    public static void main(String[] args) throws SQLException {
        Connection conn = koneksiDB.configDB();
        if (conn != null) {
            System.out.println("KONEKSI DATABASE BERHASIL");
        } else {
            System.out.println("KONEKSI DATABASE GAGAL");
        }
    }
}
