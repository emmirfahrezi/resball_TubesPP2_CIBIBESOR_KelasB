package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import model.Lapangan; // Import class Lapangan agar bisa mengakses Enum Status

public class viewLapangan extends JFrame {
    // ================= KOMPONEN =================
    private JTable tableLapangan;
    private JTextField txtId, txtNamaLapangan, txtJenisLapangan, txtHargaSewa, txtCari;
    
    // 1. UBAH: dari JTextField ke JComboBox dengan tipe Enum Status
    private JComboBox<Lapangan.Status> cbStatus; 
    
    private JButton btnSimpan, btnEdit, btnHapus, btnClear, btnCari;
    private DefaultTableModel tableModel;

    public viewLapangan() {
        setTitle("Data Lapangan - Aplikasi Sewa Lapangan");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ================= PANEL FORM =================
        JPanel panelForm = new JPanel(new GridLayout(4, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createTitledBorder("Form Lapangan"));

        txtId = new JTextField();
        txtId.setEditable(false);

        txtNamaLapangan = new JTextField();
        txtJenisLapangan = new JTextField();
        txtHargaSewa = new JTextField();
        
        // 2. INISIALISASI: JComboBox menggunakan nilai dari Enum Lapangan.Status
        cbStatus = new JComboBox<>(Lapangan.Status.values());

        panelForm.add(new JLabel("Nama Lapangan")); 
        panelForm.add(txtNamaLapangan);
        panelForm.add(new JLabel("Jenis Lapangan"));
        panelForm.add(txtJenisLapangan);
        panelForm.add(new JLabel("Harga Sewa"));
        panelForm.add(txtHargaSewa);
        panelForm.add(new JLabel("Status"));
        
        // 3. UBAH: Masukkan cbStatus ke dalam panel
        panelForm.add(cbStatus); 
        
        add(panelForm, BorderLayout.NORTH);

        // ================= PANEL TOMBOL =================
        JPanel panelTombol = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnSimpan = new JButton("Simpan");
        btnEdit = new JButton("Edit");
        btnHapus = new JButton("Hapus");
        btnClear = new JButton("Clear");

        panelTombol.add(btnSimpan);
        panelTombol.add(btnEdit);
        panelTombol.add(btnHapus);
        panelTombol.add(btnClear);

        // ================= PANEL CARI =================
        JPanel panelCari = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelCari.setBorder(BorderFactory.createTitledBorder("Cari Lapangan"));

        txtCari = new JTextField(20);
        btnCari = new JButton("Cari");

        panelCari.add(new JLabel("Nama Lapangan"));
        panelCari.add(txtCari);
        panelCari.add(btnCari);

        // ================= PANEL ATAS =================
        JPanel panelAtas = new JPanel(new BorderLayout());
        panelAtas.add(panelForm, BorderLayout.NORTH);
        panelAtas.add(panelTombol, BorderLayout.CENTER);
        panelAtas.add(panelCari, BorderLayout.SOUTH);

        add(panelAtas, BorderLayout.NORTH);

        // ================= TABEL =================
        String[] columnNames = {"ID", "Nama Lapangan", "Jenis Lapangan", "Harga Sewa", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        tableLapangan = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tableLapangan);
        add(scrollPane, BorderLayout.CENTER);
    }

    // ================= GETTER =================
    public JTable getTableLapangan() { return tableLapangan; }
    public JTextField getTxtId() { return txtId; }
    public JTextField getTxtNamaLapangan() { return txtNamaLapangan; }
    public JTextField getTxtJenisLapangan() { return txtJenisLapangan; }
    public JTextField getTxtHargaSewa() { return txtHargaSewa; }

    // 4. UBAH: Getter untuk JComboBox Status
    public JComboBox<Lapangan.Status> getCbStatus() {
        return cbStatus;
    }

    public JTextField getTxtCari() { return txtCari; }
    public JButton getBtnSimpan() { return btnSimpan; }
    public JButton getBtnEdit() { return btnEdit; }
    public JButton getBtnHapus() { return btnHapus; }
    public JButton getBtnClear() { return btnClear; }
    public JButton getBtnCari() { return btnCari; }
}