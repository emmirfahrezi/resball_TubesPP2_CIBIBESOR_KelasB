package controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Lapangan;
import model.Pelanggan;
import model.Reservasi;
import view.viewReservasi;

public class controllerReservasi {

    private Reservasi modelReservasi;
    private Pelanggan modelPelanggan;
    private Lapangan modelLapangan;
    private viewReservasi view;

    public controllerReservasi(viewReservasi view) {
        this.view = view;
        this.modelReservasi = new Reservasi();
        this.modelPelanggan = new Pelanggan();
        this.modelLapangan = new Lapangan();
        this.view.getBtnEdit().addActionListener(e -> ubahData());

        // ================= [TETAP] INIT DATA =================
        isiComboPelanggan();
        isiComboLapangan();
        tampilkanDataTabel();

        // ================= [TETAP] BUTTON =================
        this.view.getBtnSimpan().addActionListener(e -> simpanData());
        this.view.getBtnClear().addActionListener(e -> clearForm());

        // Acttion Listener Hapus
        this.view.getBtnHapus().addActionListener(e -> hapusData());

        this.view.getTableReservasi().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                pilihBaris(); // <--- Panggil function di atas
            }
        });

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
                        rs.getInt("id_booking"),
                        rs.getString("nama_pelanggan"), 
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
    // ================= LOGIC TOMBOL EDIT =================
    private void ubahData() {
        try {
            // 1. Ambil ID Reservasi (Primary Key buat WHERE)
            int idRes = Integer.parseInt(view.getTxtIdReservasi().getText());

            // 2. Ambil ID Pelanggan & Lapangan dari ComboBox (Split "1 - Nama")
            int idPel = Integer.parseInt(view.getCbPelanggan().getSelectedItem().toString().split(" - ")[0]);
            int idLap = Integer.parseInt(view.getCbLapangan().getSelectedItem().toString().split(" - ")[0]);

            // 3. Ambil data teks lainnya
            String tgl = view.getTxtTanggal().getText();
            String mulai = view.getTxtJamMulai().getText();
            String selesai = view.getTxtJamSelesai().getText();
            int total = Integer.parseInt(view.getTxtTotalBayar().getText());

            // 4. Panggil Model Update
            if (modelReservasi.update(idRes, idPel, idLap, tgl, mulai, selesai, total)) {
                javax.swing.JOptionPane.showMessageDialog(view, "Data Berhasil Diupdate!");
                tampilkanDataTabel(); // Refresh tabel
                clearForm(); // Bersihkan form
            }
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(view, "Gagal Edit: " + e.getMessage());
        }
    }

    // ================= LOGIC PILIH BARIS (DARI TABEL KE FORM) =================
    private void pilihBaris() {
        int row = view.getTableReservasi().getSelectedRow();

        if (row != -1) {
            // 1. Ambil data mentah dari tabel
            String idRes = view.getTableReservasi().getValueAt(row, 0).toString();
            String namaPel = view.getTableReservasi().getValueAt(row, 1).toString(); // Cuma Nama
            String namaLap = view.getTableReservasi().getValueAt(row, 2).toString(); // Cuma Nama
            String tgl = view.getTableReservasi().getValueAt(row, 3).toString();
            String mulai = view.getTableReservasi().getValueAt(row, 4).toString();
            String selesai = view.getTableReservasi().getValueAt(row, 5).toString();
            String total = view.getTableReservasi().getValueAt(row, 6).toString();

            // 2. Masukin ke Textfield biasa
            view.getTxtIdReservasi().setText(idRes);
            view.getTxtTanggal().setText(tgl);
            view.getTxtJamMulai().setText(mulai);
            view.getTxtJamSelesai().setText(selesai);
            view.getTxtTotalBayar().setText(total);

            // 3. Logic Milih ComboBox Pelanggan
            // Kita cari item di ComboBox yang mengandung nama dari tabel
            for (int i = 0; i < view.getCbPelanggan().getItemCount(); i++) {
                String item = view.getCbPelanggan().getItemAt(i).toString();
                if (item.contains(namaPel)) {
                    view.getCbPelanggan().setSelectedIndex(i);
                    break;
                }
            }

            // 4. Logic Milih ComboBox Lapangan
            for (int i = 0; i < view.getCbLapangan().getItemCount(); i++) {
                String item = view.getCbLapangan().getItemAt(i).toString();
                if (item.contains(namaLap)) {
                    view.getCbLapangan().setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    // LOGIC HAPUS DATA RESERVASI
    private void hapusData() {
        int row = view.getTableReservasi().getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(view, "Pilih data reservasi dulu!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                view,
                "Yakin ingin menghapus reservasi?",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int idRes = Integer.parseInt(
                        view.getTableReservasi().getValueAt(row, 0).toString());

                modelReservasi.deleteById(idRes); // <<< INTI FITUR HAPUS
                tampilkanDataTabel();
                clearForm();

                JOptionPane.showMessageDialog(view, "Data berhasil dihapus!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(view, "Gagal hapus data!");
            }
        }
    }

    private void clearForm() {
        view.getTxtTanggal().setText("YYYY-MM-DD");
        view.getTxtJamMulai().setText("HH:mm");
        view.getTxtJamSelesai().setText("HH:mm");
        view.getTxtTotalBayar().setText("");
    }
}
