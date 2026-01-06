package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import koneksiDB.koneksiDB;

public class Pelanggan {
    // ================= GET ALL =================
    // Method di model/Pelanggan.java
    public ResultSet getAll() throws Exception {
        Connection conn = koneksiDB.configDB();
        // Sesuaikan query dengan nama tabel pelanggan Anda
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

    // Tambahkan di model/Pelanggan.java
    public int getIdByNama(String nama) throws Exception {
        Connection conn = koneksiDB.configDB();
        String sql = "SELECT id_pelanggan FROM pelanggan WHERE nama = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, nama);
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            return rs.getInt("id_pelanggan");
        }
        return 0;
    }
}
