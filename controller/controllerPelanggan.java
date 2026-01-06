package controller;

import model.Pelanggan;
import view.viewPelanggan;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;

public class controllerPelanggan {
    private Pelanggan model;
    private viewPelanggan view;

    public controllerPelanggan(viewPelanggan view) {
        this.model = new Pelanggan();
        this.view = view;
    }

    // Menampilkan data dari Model ke Tabel di View
    public void tampilkanData() {
        // Ambil model tabel dari View
        DefaultTableModel tbl = (DefaultTableModel) view.getTablePelanggan().getModel();
        tbl.setRowCount(0); // Bersihkan tabel sebelum memuat data baru
        
        try {
            ResultSet rs = model.getAll();
            int no = 1;
            while (rs.next()) {
                tbl.addRow(new Object[]{
                    no++, // Kolom No
                    rs.getString("id_pelanggan"),
                    rs.getString("nama"),
                    rs.getString("no_hp"),
                    rs.getString("nama_tim")
                });
            }
        } catch (Exception e) {
            System.out.println("Error tampil data: " + e.getMessage());
        }
    }

    // Mengambil input dari View dan mengirim ke Model
    public void simpanData() {
        try {
            model.insert(
                view.getTxtNama().getText(),
                view.getTxtNoHp().getText(),
                view.getTxtTim().getText() // Disesuaikan dengan getter di View
            );
            tampilkanData(); // Refresh tabel
            clearForm();
        } catch (Exception e) {
            System.out.println("Gagal Simpan: " + e.getMessage());
        }
    }

    public void clearForm() {
        view.getTxtId().setText("");
        view.getTxtNama().setText("");
        view.getTxtNoHp().setText("");
        view.getTxtTim().setText("");
    }
}