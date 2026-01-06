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

        isiFormSaatKlikTabel();

        tampilkanData();

        view.getBtnSimpan().addActionListener(e -> {
            try {
                String nama = view.getTxtNamaLapangan().getText();
                String jenis = view.getTxtJenisLapangan().getText();
                int harga = Integer.parseInt(view.getTxtHargaSewa().getText());

                Lapangan.Status status = Lapangan.Status.valueOf(view.getCbStatus().getSelectedItem().toString());

                model.insert(nama, jenis, harga, status);

                tampilkanData();
                resetForm();
            } catch (Exception ex) {
                // isi disini mir untuk validasi
            }
        });
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

    private void isiFormSaatKlikTabel() {
        view.getTableLapangan().getSelectionModel()
                .addListSelectionListener(e -> {

                    if (e.getValueIsAdjusting())
                        return; // 🔥 PENTING

                    int row = view.getTableLapangan().getSelectedRow();
                    if (row != -1) {

                        view.getTxtId().setText(
                                view.getTableLapangan().getValueAt(row, 1).toString());
                        view.getTxtNamaLapangan().setText(
                                view.getTableLapangan().getValueAt(row, 2).toString());
                        view.getTxtJenisLapangan().setText(
                                view.getTableLapangan().getValueAt(row, 3).toString());
                        view.getTxtHargaSewa().setText(
                                view.getTableLapangan().getValueAt(row, 4).toString());

                        String statusDb = view.getTableLapangan()
                                .getValueAt(row, 5)
                                .toString();

                        view.getCbStatus().setSelectedItem(statusDb);
                    }
                });
    }

    private void resetForm() {
        view.getTxtId().setText("");
        view.getTxtNamaLapangan().setText("");
        view.getTxtJenisLapangan().setText("");
        view.getTxtHargaSewa().setText("");
        view.getCbStatus().setSelectedIndex(0); // TERSEDIA
        view.getTableLapangan().clearSelection();
    }

}
