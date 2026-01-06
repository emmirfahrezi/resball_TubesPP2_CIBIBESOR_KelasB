package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import koneksiDB.koneksiDB;

public class Pelanggan {
    // ================= GET ALL =================
    // Method untuk mengambil semua data pelanggan
    public ResultSet getAll() throws Exception {
        Connection conn = koneksiDB.configDB();
        String sql = "SELECT * FROM pelanggan ORDER BY id_pelanggan ASC";
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(sql);
    }

    public void insert(String nama, String noHp, String namaTim) throws Exception {
        String sql = "INSERT INTO pelanggan (nama, no_hp, nama_tim) VALUES (?, ?, ?)";
        Connection conn = koneksiDB.configDB();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, nama);
        ps.setString(2, noHp);
        ps.setString(3, namaTim);
        ps.executeUpdate();
    }

    // ================= DELETE =================
    // Method untuk menghapus data pelanggan berdasarkan ID
    public void deleteById(int id) throws Exception {
        String sql = "DELETE FROM pelanggan WHERE id_pelanggan = ?";
        Connection conn = koneksiDB.configDB();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
}
    // ... method update dan delete tetap sama seperti kode sebelumnya

}
