package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class viewReservasi extends JFrame {

    // ================= KOMPONEN =================
    private JTable tableReservasi;
    private JTextField txtIdReservasi, txtIdPelanggan, txtIdLapangan, txtTanggal, txtJamMulai, txtJamSelesai, txtTotalBayar, txtCari;

    private JComboBox<String> cbPelanggan, cbLapangan;
    private JButton btnSimpan, btnEdit, btnHapus, btnClear, btnCari, btnPdf;
    private DefaultTableModel tableModel;
    
    public viewReservasi() {
        setTitle("Data Reservasi - Aplikasi Sewa Lapangan");
        setSize(800, 650); // Ukuran sedikit diperbesar agar tabel lebih lega
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Gunakan DISPOSE agar tidak menutup seluruh app jika ada jendela lain
        setLayout(new BorderLayout());

        // ================= PANEL FORM =================
        JPanel panelForm = new JPanel(new GridLayout(6, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createTitledBorder("Form Reservasi"));

        txtIdReservasi = new JTextField();
        txtIdReservasi.setEditable(false); // ID biasanya auto-increment, jadi tidak perlu diisi manual
        
        // Komponen yang Anda buat tetap ada
        txtIdPelanggan = new JTextField();
        txtIdLapangan = new JTextField();

        cbPelanggan = new JComboBox<>();
        cbLapangan = new JComboBox<>();
        txtTanggal = new JTextField("YYYY-MM-DD");
        txtJamMulai = new JTextField("HH:mm");
        txtJamSelesai = new JTextField("HH:mm");
        txtTotalBayar = new JTextField();

        panelForm.add(new JLabel("Pelanggan:")); panelForm.add(cbPelanggan);
        panelForm.add(new JLabel("Lapangan:")); panelForm.add(cbLapangan);
        panelForm.add(new JLabel("Tanggal:")); panelForm.add(txtTanggal);
        panelForm.add(new JLabel("Jam Mulai:")); panelForm.add(txtJamMulai);
        panelForm.add(new JLabel("Jam Selesai:")); panelForm.add(txtJamSelesai);
        panelForm.add(new JLabel("Total Bayar:")); panelForm.add(txtTotalBayar);

        // ================= PANEL TOMBOL =================
        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnSimpan = new JButton("Simpan");
        btnEdit = new JButton("Edit");
        btnHapus = new JButton("Hapus");
        btnClear = new JButton("Clear");
        btnPdf = new JButton("Cetak PDF");

        panelTombol.add(btnSimpan);
        panelTombol.add(btnEdit);
        panelTombol.add(btnHapus);
        panelTombol.add(btnClear);
        panelTombol.add(btnPdf);

        // ================= PANEL CARI =================
        JPanel panelCari = new JPanel(new FlowLayout(FlowLayout.LEFT)); 
        panelCari.setBorder(BorderFactory.createTitledBorder("Cari Reservasi"));
        
        txtCari = new JTextField(15); // Menambahkan field cari agar tombol cari berguna
        btnCari = new JButton("Cari");  
        
        panelCari.add(new JLabel("Id Reservasi:"));
        panelCari.add(txtCari); // Menambahkan komponen input cari
        panelCari.add(btnCari);
        
        // ================= PANEL ATAS =================
        JPanel panelAtas = new JPanel(new BorderLayout());
        panelAtas.add(panelForm, BorderLayout.NORTH);
        panelAtas.add(panelTombol, BorderLayout.CENTER);
        panelAtas.add(panelCari, BorderLayout.SOUTH);

        add(panelAtas, BorderLayout.NORTH);

        // ================= TABEL =================
        // Menyesuaikan kolom dengan data JOIN (menambahkan nama agar user tidak bingung)
        String[] columnNames = {"Id Res", "Pelanggan", "Lapangan", "Tanggal", "Mulai", "Selesai", "Total"};
        tableModel = new DefaultTableModel(columnNames, 0);
        tableReservasi = new JTable(tableModel);    
        JScrollPane scrollPane = new JScrollPane(tableReservasi);
        add(scrollPane, BorderLayout.CENTER);
    }

    // ================= GETTER LENGKAP =================
    public JTable getTableReservasi() { return tableReservasi; }
    public JTextField getTxtIdReservasi() { return txtIdReservasi; }
    public JTextField getTxtIdPelanggan() { return txtIdPelanggan; }
    public JTextField getTxtIdLapangan() { return txtIdLapangan; }
    public JTextField getTxtTanggal() { return txtTanggal; }
    public JTextField getTxtJamMulai() { return txtJamMulai; }
    public JTextField getTxtJamSelesai() { return txtJamSelesai; }
    public JTextField getTxtTotalBayar() { return txtTotalBayar; }
    public JTextField getTxtCari() { return txtCari; } // Tambahan getter cari

    public JComboBox<String> getCbPelanggan() { return cbPelanggan; }
    public JComboBox<String> getCbLapangan() { return cbLapangan; }
    
    public JButton getBtnSimpan() { return btnSimpan; }
    public JButton getBtnEdit() { return btnEdit; }
    public JButton getBtnHapus() { return btnHapus; }
    public JButton getBtnClear() { return btnClear; }
    public JButton getBtnCari() { return btnCari; }
    public JButton getBtnPdf() { return btnPdf; }
}