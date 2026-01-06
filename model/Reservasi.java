package model;

import java.sql.*;
import koneksiDB.koneksiDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Reservasi {

    private Connection conn;

    public ResultSet getAll() throws SQLException {
        conn = koneksiDB.configDB();
        String sql = "SELECT r.*, p.nama_pelanggan, l.nama_lapangan " +
                     "FROM reservasi r " +
                     "JOIN pelanggan p ON r.id_pelanggan = p.id_pelanggan " +
                     "JOIN lapangan l ON r.id_lapangan = l.id_lapangan";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        return pstmt.executeQuery();
    }

    public boolean simpan(int idPel, int idLap, String tgl, String mulai, String selesai, int total) throws SQLException {
        String sql = "INSERT INTO reservasi (id_pelanggan, id_lapangan, tanggal, jam_mulai, jam_selesai, total_bayar) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, idPel);
        pstmt.setInt(2, idLap);
        pstmt.setString(3, tgl);
        pstmt.setString(4, mulai);
        pstmt.setString(5, selesai);
        pstmt.setInt(6, total);
        return pstmt.executeUpdate() > 0;
    }
}