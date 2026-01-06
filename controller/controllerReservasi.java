package controller;

import model.Reservasi;
import model.Pelanggan;
import model.Lapangan;
import view.viewReservasi;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class controllerReservasi {

    private Reservasi modelReservasi;
    private Pelanggan modelPelanggan;
    private Lapangan modelLapangan;
    private viewReservasi view;

    public controllerReservasi(viewReservasi view) {
        this.modelReservasi = new Reservasi();
        this.modelPelanggan = new Pelanggan();
        this.modelLapangan = new Lapangan();
        this.view = view;

        // ================= [TETAP] INIT DATA =================
        isiComboPelanggan();
        isiComboLapangan();
        tampilkanDataTabel();

        // ================= [TETAP] BUTTON =================
        this.view.getBtnSimpan().addActionListener(e -> simpanData());
        this.view.getBtnClear().addActionListener(e -> clearForm());

        // ================= [DIUBAH] HITUNG OTOMATIS =================
        this.view.getTxtJamSelesai().addFocusListener(
                new java.awt.event.FocusAdapter() {
                    @Override
                    public void focusLost(java.awt.event.FocusEvent evt) {
                        hitungTotalOtomatis();
                    }
                });

        this.view.getCbLapangan().addActionListener(
                e -> hitungTotalOtomatis());
    }

    // =====================================================
    // [DIUBAH] ISI COMBO PELANGGAN → ID - NAMA
    // =====================================================
    private void isiComboPelanggan() {
        try {
            view.getCbPelanggan().removeAllItems();
            ResultSet rs = modelPelanggan.getAll();

            while (rs.next()) {
                view.getCbPelanggan().addItem(
                        rs.getInt("id_pelanggan") + " - " +
                                rs.getString("nama"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =====================================================
    // [TETAP] ISI COMBO LAPANGAN
    // =====================================================
    private void isiComboLapangan() {
        try {
            view.getCbLapangan().removeAllItems();
            ResultSet rs = modelLapangan.getAll();
            while (rs.next()) {
                view.getCbLapangan().addItem(
                        rs.getInt("id_lapangan") + " - " +
                                rs.getString("nama_lapangan"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =====================================================
    // [DIAMANKAN] HITUNG TOTAL OTOMATIS
    // =====================================================
    private void hitungTotalOtomatis() {
        try {
            if (view.getCbLapangan().getSelectedItem() == null)
                return;
            if (view.getTxtJamMulai().getText().equals("HH:mm"))
                return;
            if (view.getTxtJamSelesai().getText().equals("HH:mm"))
                return;

            String selectedLap = view.getCbLapangan().getSelectedItem().toString();
            int idLap = Integer.parseInt(selectedLap.split(" - ")[0]);

            ResultSet rs = modelLapangan.getById(idLap);

            if (rs.next()) {
                int hargaPerJam = rs.getInt("harga_per_jam");

                int jamMulai = Integer.parseInt(view.getTxtJamMulai().getText().split(":")[0]);
                int jamSelesai = Integer.parseInt(view.getTxtJamSelesai().getText().split(":")[0]);

                int durasi = jamSelesai - jamMulai;

                if (durasi > 0) {
                    view.getTxtTotalBayar()
                            .setText(String.valueOf(durasi * hargaPerJam));
                } else {
                    view.getTxtTotalBayar().setText("0");
                }
            }
        } catch (Exception e) {
            // sengaja diem
        }
    }

    // =====================================================
    // [TETAP] TAMPILKAN DATA
    // =====================================================
    public void tampilkanDataTabel() {
        DefaultTableModel tbl = (DefaultTableModel) view.getTableReservasi().getModel();
        tbl.setRowCount(0);

        try {
            ResultSet rs = modelReservasi.getAll();
            while (rs.next()) {
                tbl.addRow(new Object[] {
                        rs.getInt("id_booking"), // 🔥 BUKAN id_reservasi
                        rs.getString("nama_pelanggan"), // 🔥 ALIAS
                        rs.getString("nama_lapangan"),
                        rs.getDate("tanggal"),
                        rs.getTime("jam_mulai"),
                        rs.getTime("jam_selesai"),
                        rs.getInt("total_bayar")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =====================================================
    // [DIUBAH] SIMPAN DATA → AMAN & KONSISTEN
    // =====================================================
    private void simpanData() {
        try {
            String pel = view.getCbPelanggan().getSelectedItem().toString();
            String lap = view.getCbLapangan().getSelectedItem().toString();

            int idPel = Integer.parseInt(pel.split(" - ")[0]);
            int idLap = Integer.parseInt(lap.split(" - ")[0]);

            String tgl = view.getTxtTanggal().getText();
            String mulai = view.getTxtJamMulai().getText();
            String selesai = view.getTxtJamSelesai().getText();
            int total = Integer.parseInt(view.getTxtTotalBayar().getText());

            if (modelReservasi.simpan(
                    idPel, idLap, tgl, mulai, selesai, total)) {

                JOptionPane.showMessageDialog(
                        view, "Reservasi Berhasil!");

                tampilkanDataTabel();
                clearForm();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    view, "Gagal Simpan: " + e.getMessage());
        }
    }

    // =====================================================
    // [TETAP] CLEAR FORM
    // =====================================================
    private void clearForm() {
        view.getTxtTanggal().setText("YYYY-MM-DD");
        view.getTxtJamMulai().setText("HH:mm");
        view.getTxtJamSelesai().setText("HH:mm");
        view.getTxtTotalBayar().setText("");
    }
}
