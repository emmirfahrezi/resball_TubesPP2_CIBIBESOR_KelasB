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
        this.modelReservasi = new Reservasi();
        this.modelPelanggan = new Pelanggan();
        this.modelLapangan = new Lapangan();
        this.view.getBtnEdit().addActionListener(e -> ubahData());
        this.view = view;

        // 1. Inisialisasi Data awal
        isiComboPelanggan();
        isiComboLapangan();
        tampilkanDataTabel();

        // 2. Listener untuk Tombol
        this.view.getBtnSimpan().addActionListener(e -> simpanData());
        this.view.getBtnClear().addActionListener(e -> clearForm());

        this.view.getTableReservasi().addMouseListener(new MouseAdapter() {
    @Override
    public void mouseClicked(MouseEvent e) {
        pilihBaris(); // <--- Panggil function di atas
    }
});

        // 3. Listener Perhitungan Otomatis
        // Menghitung saat jam selesai diisi (Focus Lost)
        this.view.getTxtJamSelesai().addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                hitungTotalOtomatis();
            }
        });
        
        // Menghitung ulang jika pilihan lapangan diganti
        this.view.getCbLapangan().addActionListener(e -> hitungTotalOtomatis());
    }

    private void isiComboPelanggan() {
        try {
            view.getCbPelanggan().removeAllItems(); // Bersihkan item lama
            ResultSet rs = modelPelanggan.getAll(); // Ambil data dari database pelanggan
            
            while(rs.next()) {
                // Tambahkan nama pelanggan ke ComboBox
                // Gunakan rs.getString("nama") sesuai kolom di database Anda
                view.getCbPelanggan().addItem(rs.getString("nama")); 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Fungsi isiComboLapangan tetap seperti yang sudah ada

    private void isiComboLapangan() {
        try {
            view.getCbLapangan().removeAllItems();
            ResultSet rs = modelLapangan.getAll(); // Mengambil dari Model Lapangan
            while(rs.next()) {
                view.getCbLapangan().addItem(rs.getInt("id_lapangan") + " - " + rs.getString("nama_lapangan"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hitungTotalOtomatis() {
        try {
            // Ambil ID dari ComboBox Lapangan
            String selectedLap = view.getCbLapangan().getSelectedItem().toString();
            int idLap = Integer.parseInt(selectedLap.split(" - ")[0]);

            // Ambil data harga dari Model Lapangan menggunakan getById
            ResultSet rs = modelLapangan.getById(idLap);
            
            if (rs.next()) {
                int hargaPerJam = rs.getInt("harga_per_jam");

                // Ambil nilai jam (Asumsi format HH:mm, contoh 08:00)
                int jamMulai = Integer.parseInt(view.getTxtJamMulai().getText().split(":")[0]);
                int jamSelesai = Integer.parseInt(view.getTxtJamSelesai().getText().split(":")[0]);
                
                int durasi = jamSelesai - jamMulai;
                
                if (durasi > 0) {
                    int total = durasi * hargaPerJam;
                    view.getTxtTotalBayar().setText(String.valueOf(total));
                } else {
                    view.getTxtTotalBayar().setText("0");
                }
            }
        } catch (Exception e) {
            // Error diabaikan jika form belum lengkap diisi
        }
    }

    public void tampilkanDataTabel() {
        DefaultTableModel tbl = (DefaultTableModel) view.getTableReservasi().getModel();
        tbl.setRowCount(0);
        try {
            ResultSet rs = modelReservasi.getAll(); // JOIN Pelanggan & Lapangan
            while (rs.next()) {
                tbl.addRow(new Object[]{
                    rs.getInt("id_reservasi"),
                    rs.getString("nama_pelanggan"),
                    rs.getString("nama_lapangan"),
                    rs.getString("tanggal"),
                    rs.getString("jam_mulai"),
                    rs.getString("jam_selesai"),
                    rs.getInt("total_bayar")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void simpanData() {
        try {
            int idPel = Integer.parseInt(view.getCbPelanggan().getSelectedItem().toString().split(" - ")[0]);
            int idLap = Integer.parseInt(view.getCbLapangan().getSelectedItem().toString().split(" - ")[0]);
            String tgl = view.getTxtTanggal().getText();
            String mulai = view.getTxtJamMulai().getText();
            String selesai = view.getTxtJamSelesai().getText();
            int total = Integer.parseInt(view.getTxtTotalBayar().getText());

            if (modelReservasi.simpan(idPel, idLap, tgl, mulai, selesai, total)) {
                JOptionPane.showMessageDialog(view, "Reservasi Berhasil!");
                tampilkanDataTabel();
                clearForm();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Gagal Simpan: " + e.getMessage());
        }
    }

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
                clearForm();          // Bersihkan form
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

    private void clearForm() {
        view.getTxtTanggal().setText("YYYY-MM-DD");
        view.getTxtJamMulai().setText("HH:mm");
        view.getTxtJamSelesai().setText("HH:mm");
        view.getTxtTotalBayar().setText("");
    }
}