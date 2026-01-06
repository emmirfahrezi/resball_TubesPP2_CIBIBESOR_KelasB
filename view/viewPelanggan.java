package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import controller.controllerPelanggan;

public class viewPelanggan extends JFrame {
    
    // ================= KOMPONEN =================
    private JTextField txtId, txtNama, txtNoHp, txtTim, txtCari;
    private JButton btnSimpan, btnEdit, btnHapus, btnClear, btnCari;
    private JTable tablePelanggan;
    private DefaultTableModel tableModel;

    public viewPelanggan() {
        setTitle("Data Pelanggan - Aplikasi Sewa Lapangan");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ================= PANEL FORM =================
        JPanel panelForm = new JPanel(new GridLayout(4, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createTitledBorder("Form Pelanggan"));

        txtId = new JTextField();
        txtId.setEditable(false);

        txtNama = new JTextField();
        txtNoHp = new JTextField();
        txtTim = new JTextField();

        panelForm.add(new JLabel("ID Pelanggan"));
        panelForm.add(txtId);
        panelForm.add(new JLabel("Nama"));
        panelForm.add(txtNama);
        panelForm.add(new JLabel("No HP"));
        panelForm.add(txtNoHp);
        panelForm.add(new JLabel("Nama Tim"));
        panelForm.add(txtTim);

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
        panelCari.setBorder(BorderFactory.createTitledBorder("Cari Pelanggan"));

        txtCari = new JTextField(20);
        btnCari = new JButton("Cari");

        panelCari.add(new JLabel("Nama"));
        panelCari.add(txtCari);
        panelCari.add(btnCari);

        // ================= PANEL ATAS =================
        JPanel panelAtas = new JPanel(new BorderLayout());
        panelAtas.add(panelForm, BorderLayout.NORTH);
        panelAtas.add(panelTombol, BorderLayout.CENTER);
        panelAtas.add(panelCari, BorderLayout.SOUTH);

        add(panelAtas, BorderLayout.NORTH);

        // ================= TABEL =================
        tableModel = new DefaultTableModel(
            new Object[]{"No", "ID", "Nama", "No HP", "Nama Tim"}, 0
        );

        tablePelanggan = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tablePelanggan);

        add(scrollPane, BorderLayout.CENTER);
    }

    // ================= GETTER =================
    public JTextField getTxtId() {
        return txtId;
    }

    public JTextField getTxtNama() {
        return txtNama;
    }

    public JTextField getTxtNoHp() {
        return txtNoHp;
    }

    public JTextField getTxtTim() {
        return txtTim;
    }

    public JTextField getTxtCari() {
        return txtCari;
    }

    public JButton getBtnSimpan() {
        return btnSimpan;
    }

    public JButton getBtnEdit() {
        return btnEdit;
    }

    public JButton getBtnHapus() {
        return btnHapus;
    }

    public JButton getBtnClear() {
        return btnClear;
    }

    public JButton getBtnCari() {
        return btnCari;
    }

    public JTable getTablePelanggan() {
        return tablePelanggan;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }
}
