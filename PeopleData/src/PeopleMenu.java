import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PeopleMenu extends JFrame {
    // Variabel komponen form sesuai pola latihan
    public JPanel mainPanel;
    private JLabel titleLabel;
    private JTextField idField;
    private JTextField namaField;
    private JTextField tahunLahirField;
    private JComboBox<String> kategoriComboBox;
    private JRadioButton rbLaki;
    private JRadioButton rbPerempuan;
    private ButtonGroup bgGender;
    private JTable personTable;
    private JButton addUpdateButton;
    private JButton deleteButton;
    private JButton cancelButton;

    // Atribut pendukung logika CRUD
    private int selectedIndex = -1;
    private Database database;

    // Constructor
    public PeopleMenu() {
        // Inisialisasi database
        database = new Database();

        // Isi tabel person (Pastikan personTable tidak null)
        if (personTable != null) {
            personTable.setModel(setTable());
        }

        // Ubah styling title jika label tersedia
        if (titleLabel != null) {
            titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));
        }

        // Atur isi combo box kategori
        String[] kategoriData = { "Mahasiswa", "Dosen", "Staf Administrasi", "Alumni", "Peneliti" };
        kategoriComboBox.setModel(new DefaultComboBoxModel<>(kategoriData));

        // Konfigurasi ButtonGroup untuk JRadioButton
        bgGender = new ButtonGroup();
        bgGender.add(rbLaki);
        bgGender.add(rbPerempuan);
        rbLaki.setSelected(true);

        // Sembunyikan button delete saat awal dijalankan
        deleteButton.setVisible(false);

        // Saat tombol add/update ditekan
        addUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedIndex == -1) {
                    insertData();
                } else {
                    updateData();
                }
            }
        });

        // Saat tombol delete ditekan (dilengkapi konfirmasi prompt)
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                        null,
                        "Apakah Anda yakin ingin menghapus data ini?",
                        "Konfirmasi Hapus",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    deleteData();
                }
            }
        });

        // Saat tombol cancel ditekan
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });

        // Saat salah satu baris tabel ditekan (Diperbaiki di dalam constructor & mencakup 5 kolom + RadioButton)
        personTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                selectedIndex = personTable.getSelectedRow();

                String curId = personTable.getModel().getValueAt(selectedIndex, 0).toString();
                String curNama = personTable.getModel().getValueAt(selectedIndex, 1).toString();
                String curTahun = personTable.getModel().getValueAt(selectedIndex, 2).toString();
                String curKategori = personTable.getModel().getValueAt(selectedIndex, 3).toString();
                String curGender = personTable.getModel().getValueAt(selectedIndex, 4).toString();

                idField.setText(curId);
                idField.setEnabled(false); // ID tidak boleh diedit saat update
                namaField.setText(curNama);
                tahunLahirField.setText(curTahun);
                kategoriComboBox.setSelectedItem(curKategori);

                // Set RadioButton sesuai data jenis kelamin di tabel
                if (curGender.equalsIgnoreCase("Laki-laki")) {
                    rbLaki.setSelected(true);
                } else {
                    rbPerempuan.setSelected(true);
                }

                addUpdateButton.setText("Update");
                deleteButton.setVisible(true);
            }
        });
    }

    // Method setTable() untuk membaca data dari Database
    public final DefaultTableModel setTable() {
        Object[] cols = { "ID", "Nama", "Tahun Lahir", "Kategori", "Jenis Kelamin" };
        DefaultTableModel tmp = new DefaultTableModel(null, cols);

        try {
            ResultSet resultSet = database.selectQuery("SELECT * FROM person");
            while (resultSet.next()) {
                Object[] row = new Object[5];
                row[0] = resultSet.getString("id");
                row[1] = resultSet.getString("nama");
                row[2] = resultSet.getString("thn_lahir");
                row[3] = resultSet.getString("kategori");
                row[4] = resultSet.getString("jenis_kelamin");
                tmp.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tmp;
    }

    // Method insertData()
    public void insertData() {
        String id = idField.getText().trim();
        String nama = namaField.getText().trim();
        String tahunStr = tahunLahirField.getText().trim();
        String kategori = kategoriComboBox.getSelectedItem().toString();
        String jenisKelamin = rbLaki.isSelected() ? "Laki-laki" : "Perempuan";

        if (id.isEmpty() || nama.isEmpty() || tahunStr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Semua kolom input harus diisi!", "Error Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int tahun = Integer.parseInt(tahunStr);

            // Cek ID duplikat
            ResultSet checkRs = database.selectQuery("SELECT * FROM person WHERE id = '" + id + "'");
            if (checkRs.next()) {
                JOptionPane.showMessageDialog(null, "ID sudah ada! Gunakan ID lain.", "Error ID Duplikat", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Query INSERT
            String sqlQuery = "INSERT INTO person VALUES('" + id + "', '" + nama + "', '"
                    + tahun + "', '" + kategori + "', '" + jenisKelamin + "')";
            database.insertUpdateDeleteQuery(sqlQuery);

            personTable.setModel(setTable());
            clearForm();
            JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Tahun Lahir harus berupa angka!", "Error Input", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Method updateData()
    public void updateData() {
        String id = idField.getText().trim();
        String nama = namaField.getText().trim();
        String tahunStr = tahunLahirField.getText().trim();
        String kategori = kategoriComboBox.getSelectedItem().toString();
        String jenisKelamin = rbLaki.isSelected() ? "Laki-laki" : "Perempuan";

        if (id.isEmpty() || nama.isEmpty() || tahunStr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Semua kolom input harus diisi!", "Error Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int tahunLahir = Integer.parseInt(tahunStr);

            // Query UPDATE
            String sqlQuery = "UPDATE person SET nama = '" + nama + "', thn_lahir = '" + tahunLahir
                    + "', kategori = '" + kategori + "', jenis_kelamin = '" + jenisKelamin + "' WHERE id = '" + id + "'";
            database.insertUpdateDeleteQuery(sqlQuery);

            personTable.setModel(setTable());
            clearForm();
            JOptionPane.showMessageDialog(null, "Data berhasil diubah!");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Tahun lahir harus berupa angka!", "Error Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method deleteData()
    public void deleteData() {
        if (selectedIndex >= 0) {
            String id = personTable.getModel().getValueAt(selectedIndex, 0).toString();

            String sqlQuery = "DELETE FROM person WHERE id = '" + id + "'";
            database.insertUpdateDeleteQuery(sqlQuery);

            personTable.setModel(setTable());
            clearForm();

            JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
        }
    }

    // Method clearForm()
    public void clearForm() {
        idField.setText("");
        idField.setEnabled(true);
        namaField.setText("");
        tahunLahirField.setText("");
        kategoriComboBox.setSelectedIndex(0);
        rbLaki.setSelected(true);

        addUpdateButton.setText("Add");
        deleteButton.setVisible(false);

        selectedIndex = -1;
        personTable.clearSelection();
    }

    // Main Method
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            PeopleMenu menu = new PeopleMenu();
            menu.setSize(750, 600);
            menu.setLocationRelativeTo(null);
            menu.setContentPane(menu.mainPanel);
            menu.getContentPane().setBackground(Color.WHITE);
            menu.setVisible(true);
            menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        });
    }
}