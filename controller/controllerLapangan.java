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

// validasi
import javax.swing.text.AbstractDocument;
import javax.swing.text.DocumentFilter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

public class controllerLapangan {
    private Lapangan model;
    private viewLapangan view;

    public controllerLapangan(viewLapangan view) {
        this.model = new Lapangan();
        this.view = view;
        this.view.getBtnEdit().addActionListener(e -> ubahData());
        this.view.getBtnClear().addActionListener(e -> clearForm());

        this.view.getTableLapangan().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                pilihBaris();
            }
        });

        setupInputValidation();

        isiFormSaatKlikTabel();

        tampilkanData();

        view.getBtnSimpan().addActionListener(e -> {

            // validasi sebelum simpan
            if (!validateBeforeSave()) return; // Hentikan proses simpan jika validasi gagal
            
            try {
                String nama = view.getTxtNamaLapangan().getText();
                String jenis = view.getTxtJenisLapangan().getText();
                int harga = Integer.parseInt(view.getTxtHargaSewa().getText());

                //validasi sttus harus di isi
                Object statusObj = view.getCbStatus().getSelectedItem();
                if (statusObj == null) {
                    JOptionPane.showMessageDialog(view, "Status harus dipilih.");
                    return;
                }

                Lapangan.Status status = Lapangan.Status.valueOf(view.getCbStatus().getSelectedItem().toString());

                model.insert(nama, jenis, harga, status);

                tampilkanData();
                clearForm();
            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(view, "Harga harus berupa angka yang valid.");
                view.getTxtHargaSewa().requestFocus();
            } catch (IllegalArgumentException iae) {
                JOptionPane.showMessageDialog(view, "Status tidak valid.");
                view.getCbStatus().requestFocus();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Gagal Simpan: " + ex.getMessage());
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

        // Validasi sebelum ubah
        if (!validateBeforeUpdate()) return; // Hentikan proses ubah jika validasi gagal

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
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(view, "ID dan Harga harus berupa angka yang valid.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Gagal Ubah: " + e.getMessage());
        }
    }

    public void clearForm() {
        view.getTxtId().setText("");
        view.getTxtNamaLapangan().setText("");
        view.getTxtJenisLapangan().setText("");
        view.getTxtHargaSewa().setText("");
        view.getCbStatus().setSelectedIndex(0);
        view.getTableLapangan().clearSelection();
    }

    private boolean validateCommonFields() {
        String nama = view.getTxtNamaLapangan().getText().trim();
        String jenis = view.getTxtJenisLapangan().getText().trim();
        String hargaTxt = view.getTxtHargaSewa().getText().trim();
        Object statusObj = view.getCbStatus().getSelectedItem();

        if (nama.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Nama Lapangan tidak boleh kosong.");
            view.getTxtNamaLapangan().requestFocus();
            return false;
        }
        if (nama.length() < 3 || nama.length() > 50) {
            JOptionPane.showMessageDialog(view, "Nama Lapangan 3-50 karakter.");
            view.getTxtNamaLapangan().requestFocus();
            return false;
        }

        if (jenis.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Jenis Lapangan tidak boleh kosong.");
            view.getTxtJenisLapangan().requestFocus();
            return false;
        }
        if (jenis.length() < 3 || jenis.length() > 40) {
            JOptionPane.showMessageDialog(view, "Jenis Lapangan 3-40 karakter.");
            view.getTxtJenisLapangan().requestFocus();
            return false;
        }

        if (hargaTxt.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Harga Sewa tidak boleh kosong.");
            view.getTxtHargaSewa().requestFocus();
            return false;
        }
        try {
            int harga = Integer.parseInt(hargaTxt);
            if (harga <= 0) {
                JOptionPane.showMessageDialog(view, "Harga Sewa harus lebih dari 0.");
                view.getTxtHargaSewa().requestFocus();
                return false;
            }
            if (harga > 100000000) {
                JOptionPane.showMessageDialog(view, "Harga Sewa terlalu besar.");
                view.getTxtHargaSewa().requestFocus();
                return false;
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(view, "Harga Sewa harus berupa angka.");
            view.getTxtHargaSewa().requestFocus();
            return false;
        }

        if (statusObj == null) {
            JOptionPane.showMessageDialog(view, "Status harus dipilih.");
            view.getCbStatus().requestFocus();
            return false;
        }
        // Validasi enum
        try {
            Lapangan.Status.valueOf(statusObj.toString());
        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(view, "Status tidak valid.");
            view.getCbStatus().requestFocus();
            return false;
        }

        return true;
    }

    private boolean validateBeforeSave() {
        return validateCommonFields();
    }

    private boolean validateBeforeUpdate() {
        String idTxt = view.getTxtId().getText().trim();
        if (idTxt.isEmpty()) {
            JOptionPane.showMessageDialog(view, "ID tidak boleh kosong saat update.");
            view.getTxtId().requestFocus();
            return false;
        }
        try {
            int id = Integer.parseInt(idTxt);
            if (id <= 0) {
                JOptionPane.showMessageDialog(view, "ID harus angka positif.");
                view.getTxtId().requestFocus();
                return false;
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(view, "ID harus berupa angka.");
            view.getTxtId().requestFocus();
            return false;
        }
        return validateCommonFields();
    }

    private void setupInputValidation() {
        // Batasi Harga Sewa hanya angka dengan panjang maksimal 9 digit
        try {
            AbstractDocument docHarga = (AbstractDocument) view.getTxtHargaSewa().getDocument();
            docHarga.setDocumentFilter(new NumericFilter(9));
        } catch (Exception ignored) {}
    }

    // Filter dokumen untuk angka saja
    private static class NumericFilter extends DocumentFilter {
        private final int maxLen;
        NumericFilter(int maxLen) { this.maxLen = maxLen; }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {
            if (string == null) return;
            String filtered = string.replaceAll("\\D", "");
            if (filtered.isEmpty()) return;

            int newLen = fb.getDocument().getLength() + filtered.length();
            if (newLen <= maxLen) {
                super.insertString(fb, offset, filtered, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            if (text == null) return;
            String filtered = text.replaceAll("\\D", "");
            int currentLen = fb.getDocument().getLength();
            int newLen = currentLen - length + filtered.length();
            if (filtered.isEmpty() && length > 0) {
                super.replace(fb, offset, length, "", attrs);
            } else if (newLen <= maxLen) {
                super.replace(fb, offset, length, filtered, attrs);
            }
        }
    }

}
