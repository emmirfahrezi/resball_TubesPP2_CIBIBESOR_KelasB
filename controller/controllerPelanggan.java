package controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Pelanggan;
import view.viewPelanggan;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.FileOutputStream;


public class controllerPelanggan {
    private Pelanggan model;
    private viewPelanggan view;

    public controllerPelanggan(viewPelanggan view) {
        this.model = new Pelanggan();
        this.view = view;

        // event
        this.view.getBtnSimpan().addActionListener(e -> simpanData());
        this.view.getBtnClear().addActionListener(e -> clearForm());
        this.view.getBtnHapus().addActionListener(e -> hapusData());
        this.view.getBtnEdit().addActionListener(e -> ubahData());
        this.view.getBtnClear().addActionListener(e -> clearForm());
        this.view.getBtnPdf().addActionListener(e -> cetakPdf());


        this.view.getTablePelanggan().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                pilihBaris(); // Panggil fungsi pilihBaris di bawah
            }
        });

        tampilkanData();
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
                tbl.addRow(new Object[] {
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

    private void pilihBaris() {
        int row = view.getTablePelanggan().getSelectedRow();
        
        if (row != -1) {
            // Ambil data dari tabel, masukin ke textfield
            view.getTxtId().setText(view.getTablePelanggan().getValueAt(row, 1).toString());
            view.getTxtNama().setText(view.getTablePelanggan().getValueAt(row, 2).toString());
            view.getTxtNoHp().setText(view.getTablePelanggan().getValueAt(row, 3).toString());
            view.getTxtTim().setText(view.getTablePelanggan().getValueAt(row, 4).toString());
        }
    }
    

    // Mengambil input dari View dan mengirim ke Model
    public void simpanData() {
         // Validasi: Cek apakah nama, no_hp, dan tim kosong
         if (view.getTxtNama().getText().isEmpty() || 
             view.getTxtNoHp().getText().isEmpty() || 
             view.getTxtTim().getText().isEmpty()) {
                JOptionPane.showMessageDialog(view, "Semua field harus diisi!");
             return;
        }

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

    // ================= LOGIC UBAH / UPDATE =================
    public void ubahData() {
        // Validasi: Cek apakah ID kosong (artinya belum klik tabel)
        if (view.getTxtId().getText().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Pilih data di tabel dulu yang mau diedit!");
            return;
        }

        // Validasi: Cek apakah nama, no_hp, dan tim kosong
         if (view.getTxtNama().getText().isEmpty() || 
            view.getTxtNoHp().getText().isEmpty() || 
            view.getTxtTim().getText().isEmpty()) {
            JOptionPane.showMessageDialog(view, "Semua field harus diisi!");
            return;
      }

        try {
            // Ambil ID dari textfield yang hidden/disable
            int id = Integer.parseInt(view.getTxtId().getText());

            // Panggil Model Update
            model.update(
                id,
                view.getTxtNama().getText(),
                view.getTxtNoHp().getText(),
                view.getTxtTim().getText()
            );

            tampilkanData(); // Refresh tabel
            clearForm();     // Bersihkan form
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Gagal Ubah: " + e.getMessage());
        }
    }

    private void hapusData() {
        int row = view.getTablePelanggan().getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(view, "Pilih data dulu!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
            view,
            "Yakin hapus data?",
            "Konfirmasi",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(
                view.getTablePelanggan().getValueAt(row, 1).toString()
            );

            try {
                model.deleteById(id);
                tampilkanData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Gagal hapus data");
            }
        }
    }

    public void clearForm() {
        view.getTxtId().setText("");
        view.getTxtNama().setText("");
        view.getTxtNoHp().setText("");
        view.getTxtTim().setText("");
    }

    private void cetakPdf() {
        try {
            Document document = new Document();
            FileOutputStream fos = new FileOutputStream("Laporan_Pelanggan.pdf");
            PdfWriter.getInstance(document, fos);
            document.open();

            // Add title
            Paragraph title = new Paragraph("Laporan Data Pelanggan");
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            // Create table
            PdfPTable table = new PdfPTable(5);
            table.addCell("No");
            table.addCell("ID Pelanggan");
            table.addCell("Nama");
            table.addCell("No HP");
            table.addCell("Nama Tim");

            // Add data from table
            DefaultTableModel tbl = (DefaultTableModel) view.getTablePelanggan().getModel();
            for (int i = 0; i < tbl.getRowCount(); i++) {
                table.addCell(tbl.getValueAt(i, 0).toString());
                table.addCell(tbl.getValueAt(i, 1).toString());
                table.addCell(tbl.getValueAt(i, 2).toString());
                table.addCell(tbl.getValueAt(i, 3).toString());
                table.addCell(tbl.getValueAt(i, 4).toString());
            }

            document.add(table);
            document.close();
            fos.close();

            JOptionPane.showMessageDialog(view, "PDF berhasil dibuat: Laporan_Pelanggan.pdf");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "Gagal membuat PDF: " + e.getMessage());
        }
    }

    private void addCellHeader(PdfPTable table, String text) {
    Font font = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
    PdfPCell cell = new PdfPCell(new Phrase(text, font));
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
    table.addCell(cell);
}


    
}