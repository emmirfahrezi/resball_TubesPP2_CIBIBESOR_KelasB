package controller;

import model.Lapangan;
import view.viewLapangan;   
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;

public class controllerLapangan {
    private Lapangan model;
    private viewLapangan view;

    public controllerLapangan(viewLapangan view) {
        this.model = new Lapangan();
        this.view = view;

        tampilkanData();
    }

    public void tampilkanData() {
        // Ambil model tabel dari View
        DefaultTableModel tbl = (DefaultTableModel) view.getTableLapangan().getModel();
        tbl.setRowCount(0); // Bersihkan tabel sebelum memuat data baru

        try {
            ResultSet rs = model.getAll();
            int no = 1;
            while (rs.next()) {
                tbl.addRow(new Object[] {
                        no++, // Kolom No
                        rs.getString("id_lapangan"),
                        rs.getString("nama_lapangan"),
                        rs.getString("jenis"),
                        rs.getString("harga_per_jam"),
                        rs.getString("status")
                });
            }
        } catch (Exception e) {
            System.out.println("Error tampil data: " + e.getMessage());
        }
    }
}
