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


    // Tambahkan ini di dalam class Lapangan
    public ResultSet getById(int id) throws Exception {
        Connection conn = koneksiDB.configDB();
        // Mengambil data berdasarkan id_lapangan
        String sql = "SELECT * FROM lapangan WHERE id_lapangan = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        return ps.executeQuery();
    }
}
