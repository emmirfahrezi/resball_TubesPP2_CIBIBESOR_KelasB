package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import koneksiDB.koneksiDB;

public class Lapangan {

    // Definisi Enum di dalam Class
    public enum Status {
        TERSEDIA,
        TIDAK_TERSEDIA
    }

    // ================= GET ALL =================
    // Method untuk mengambil semua data lapangan
    public ResultSet getAll() throws Exception {
        Connection conn = koneksiDB.configDB();
        String sql = "SELECT * FROM lapangan ORDER BY id_lapangan ASC";
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(sql);
    }

    // simpan data
    public void insert(String nama, String jenis, int harga, Status status) throws Exception {
        Connection conn = koneksiDB.configDB();

        String sql = "INSERT INTO lapangan (nama_lapangan, jenis, harga_per_jam, status) VALUES (?, ?, ?, ?)";

        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, nama);
        pst.setString(2, jenis);
        pst.setInt(3, harga);
        pst.setString(4, status.name()); // ENUM → STRING

        pst.executeUpdate();
    }
}
