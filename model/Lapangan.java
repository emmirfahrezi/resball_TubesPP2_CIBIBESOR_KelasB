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

    public void update(int id, String nama, String jenis, int harga, String status) throws Exception {
        // Query SQL buat update data
        String sql = "UPDATE lapangan SET nama_lapangan=?, jenis=?, harga_per_jam=?, status=? WHERE id_lapangan=?";

        Connection conn = koneksiDB.configDB();
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, nama);
        ps.setString(2, jenis);
        ps.setInt(3, harga); // Harga tipe int
        ps.setString(4, status);
        ps.setInt(5, id); // ID buat kondisi WHERE

        ps.executeUpdate();
    }

    // <-- DELETE DATA LAPANGAN -->
    public void deleteById(int id) throws Exception {
        String sql = "DELETE FROM lapangan WHERE id_lapangan = ?";
        Connection conn = koneksiDB.configDB();
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    // cari
    public ResultSet cariByNama(String keyword) throws Exception {
        Connection conn = koneksiDB.configDB();
        String sql = "SELECT * FROM lapangan WHERE nama_lapangan LIKE ? ORDER BY id_lapangan ASC";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, "%" + keyword + "%");
        return ps.executeQuery();
    }

}
