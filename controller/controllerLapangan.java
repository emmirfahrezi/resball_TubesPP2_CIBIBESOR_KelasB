package controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
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
        this.view.getBtnEdit().addActionListener(e -> ubahData());

        this.view.getTableLapangan().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                pilihBaris();
            }
        });

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
                clearForm();
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

    // ================= LOGIC PILIH BARIS =================
    private void pilihBaris() {
        int row = view.getTableLapangan().getSelectedRow();

        if (row != -1) {
            // 1. Ambil data dari tabel
            String id = view.getTableLapangan().getValueAt(row, 0).toString();
            String nama = view.getTableLapangan().getValueAt(row, 1).toString();
            String jenis = view.getTableLapangan().getValueAt(row, 2).toString();
            String harga = view.getTableLapangan().getValueAt(row, 3).toString();
            String status = view.getTableLapangan().getValueAt(row, 4).toString();

            // 2. Masukin ke Form (Pake Getter yang BENAR sesuai View lu)
            view.getTxtId().setText(id);
            view.getTxtNamaLapangan().setText(nama);
            view.getTxtJenisLapangan().setText(jenis);
            view.getTxtHargaSewa().setText(harga);

            // Khusus ComboBox Status (String diubah balik jadi Enum biar terpilih)
            try {
                view.getCbStatus().setSelectedItem(Lapangan.Status.valueOf(status));
            } catch (Exception e) {
                // Kalau status di DB beda tulisan, default aja
                view.getCbStatus().setSelectedIndex(0);
            }
        }
    }

    public void ubahData() {
        if (view.getTxtId().getText().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Pilih data dulu di tabel!");
            return;
        }

        try {
            int id = Integer.parseInt(view.getTxtId().getText());
            String nama = view.getTxtNamaLapangan().getText();
            String jenis = view.getTxtJenisLapangan().getText();
            int harga = Integer.parseInt(view.getTxtHargaSewa().getText());
            String status = view.getCbStatus().getSelectedItem().toString();

            model.update(id, nama, jenis, harga, status);

            tampilkanData();
            clearForm();
            JOptionPane.showMessageDialog(view, "Data Berhasil Diubah!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Gagal Ubah: " + e.getMessage());
        }
    }

    public void clearForm() {
        view.getTxtId().setText("");
        view.getTxtNamaLapangan().setText("");
        view.getTxtJenisLapangan().setText("");
        view.getTxtHargaSewa().setText("");
        view.getCbStatus().setSelectedIndex(0); // TERSEDIA
        view.getTableLapangan().clearSelection();
    }

}
