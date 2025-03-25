import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Menu extends JFrame {
    private Database database;
    private ArrayList<Mahasiswa> listMahasiswa;
    private int selectedIndex;
    private JTextField nimField;
    private JTextField namaField;
    private JComboBox jenisKelaminComboBox;
    private JComboBox jurusanComboBox;
    private JTable mahasiswaTable;
    private JButton addButton;
    private JButton cancelButton;
    private JButton addUpdateButton;
    private JButton deleteButton;
    private JPanel mainPanel;
    private JLabel titleLabel;
    private JLabel nimLabel;
    private JLabel namaLabel;
    private JLabel jenisKelaminLabel;
    private JLabel jurusanLabel;



    // Pastel Blue Color
    private static final Color PASTEL_BLUE = new Color(173, 216, 230); // Light Blue

    public Menu() {
        // Initialize database
        database = new Database();

        // Initialize list
        listMahasiswa = new ArrayList<>();

        // Setup frame
        setTitle("Data Mahasiswa");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        getContentPane().setBackground(PASTEL_BLUE);
        setLayout(null);

        // NIM Label and Field
        JLabel nimLabel = new JLabel("NIM");
        nimLabel.setBounds(20, 20, 100, 25);
        add(nimLabel);

        nimField = new JTextField();
        nimField.setBounds(120, 20, 250, 25);
        add(nimField);

        // Nama Label and Field
        JLabel namaLabel = new JLabel("Nama");
        namaLabel.setBounds(20, 50, 100, 25);
        add(namaLabel);

        namaField = new JTextField();
        namaField.setBounds(120, 50, 250, 25);
        add(namaField);

        // Jenis Kelamin Label and ComboBox
        JLabel jenisKelaminLabel = new JLabel("Jenis Kelamin");
        jenisKelaminLabel.setBounds(20, 80, 100, 25);
        add(jenisKelaminLabel);

        String[] jenisKelaminOptions = {"", "Laki-laki", "Perempuan"};
        jenisKelaminComboBox = new JComboBox<>(jenisKelaminOptions);
        jenisKelaminComboBox.setBounds(120, 80, 250, 25);
        add(jenisKelaminComboBox);

        // Jurusan Label and ComboBox
        JLabel jurusanLabel = new JLabel("Jurusan");
        jurusanLabel.setBounds(20, 110, 100, 25);
        add(jurusanLabel);

        String[] jurusanOptions = {"", "Teknik Informatika", "Sistem Informasi", "Ilmu Komputer", "Teknik Elektro", "Teknik Sipil"};
        jurusanComboBox = new JComboBox<>(jurusanOptions);
        jurusanComboBox.setBounds(120, 110, 250, 25);
        add(jurusanComboBox);

        // Buttons
        addButton = new JButton("Add");
        addButton.setBounds(120, 150, 100, 25);
        add(addButton);

        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(230, 150, 100, 25);
        add(cancelButton);

        deleteButton = new JButton("Delete");
        deleteButton.setBounds(340, 150, 100, 25);
        add(deleteButton);

        // Table
        String[] columnNames = {"No", "NIM", "Nama", "Jenis Kelamin", "Jurusan"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        mahasiswaTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(mahasiswaTable);
        scrollPane.setBounds(20, 200, 450, 250);
        add(scrollPane);

        // Populate table from database
        populateListFromDatabase();

        // Table click listener
        mahasiswaTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = mahasiswaTable.getSelectedRow();
                if (row != -1) {
                    selectedIndex = row;
                    Mahasiswa selectedMahasiswa = listMahasiswa.get(row);
                    nimField.setText(selectedMahasiswa.getNim());
                    namaField.setText(selectedMahasiswa.getNama());
                    jenisKelaminComboBox.setSelectedItem(selectedMahasiswa.getJenisKelamin());
                    jurusanComboBox.setSelectedItem(selectedMahasiswa.getJurusan());
                    addButton.setText("Update");
                }
            }
        });

        // Add button listener
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (addButton.getText().equals("Add")) {
                    insertData();
                } else {
                    updateData();
                }
            }
        });

        // Delete button listener
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteData();
            }
        });

        // Cancel button listener
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });
    }

    private void populateListFromDatabase() {
        // Clear existing list
        listMahasiswa.clear();
        DefaultTableModel model = (DefaultTableModel) mahasiswaTable.getModel();
        model.setRowCount(0);

        DefaultTableModel databaseModel = database.getDataTable();

        for (int i = 0; i < databaseModel.getRowCount(); i++) {
            String nim = (String) databaseModel.getValueAt(i, 0);
            String nama = (String) databaseModel.getValueAt(i, 1);
            String jenisKelamin = (String) databaseModel.getValueAt(i, 2);
            String jurusan = (String) databaseModel.getValueAt(i, 3);

            model.addRow(new Object[]{i + 1, nim, nama, jenisKelamin, jurusan});
            listMahasiswa.add(new Mahasiswa(nim, nama, jenisKelamin, jurusan));
        }
    }

    private void insertData() {
        String nim = nimField.getText();
        String nama = namaField.getText();
        String jenisKelamin = jenisKelaminComboBox.getSelectedItem().toString();
        String jurusan = jurusanComboBox.getSelectedItem().toString();

        int result = database.insertData(nim, nama, jenisKelamin, jurusan);

        switch (result) {
            case -1:
                JOptionPane.showMessageDialog(
                        null,
                        "Semua kolom wajib diisi!",
                        "Peringatan",
                        JOptionPane.WARNING_MESSAGE
                );
                break;
            case -2:
                JOptionPane.showMessageDialog(
                        null,
                        "NIM sudah ada!",
                        "Kesalahan",
                        JOptionPane.ERROR_MESSAGE
                );
                break;
            case 0:
                JOptionPane.showMessageDialog(
                        null,
                        "Gagal menambahkan data",
                        "Kesalahan",
                        JOptionPane.ERROR_MESSAGE
                );
                break;
            case 1:
                populateListFromDatabase();
                clearForm();
                JOptionPane.showMessageDialog(
                        null,
                        "Data berhasil ditambahkan",
                        "Sukses",
                        JOptionPane.INFORMATION_MESSAGE
                );
                break;
        }
    }

    private void updateData() {
        String nimLama = listMahasiswa.get(selectedIndex).getNim();
        String nimBaru = nimField.getText();
        String nama = namaField.getText();
        String jenisKelamin = jenisKelaminComboBox.getSelectedItem().toString();
        String jurusan = jurusanComboBox.getSelectedItem().toString();

        int result = database.updateData(nimLama, nimBaru, nama, jenisKelamin, jurusan);

        switch (result) {
            case -1:
                JOptionPane.showMessageDialog(
                        null,
                        "Semua kolom wajib diisi!",
                        "Peringatan",
                        JOptionPane.WARNING_MESSAGE
                );
                break;
            case -2:
                JOptionPane.showMessageDialog(
                        null,
                        "NIM sudah ada!",
                        "Kesalahan",
                        JOptionPane.ERROR_MESSAGE
                );
                break;
            case 0:
                JOptionPane.showMessageDialog(
                        null,
                        "Gagal memperbarui data",
                        "Kesalahan",
                        JOptionPane.ERROR_MESSAGE
                );
                break;
            case 1:
                populateListFromDatabase();
                clearForm();
                JOptionPane.showMessageDialog(
                        null,
                        "Data berhasil diperbarui",
                        "Sukses",
                        JOptionPane.INFORMATION_MESSAGE
                );
                break;
        }
    }

    private void deleteData() {
        // Check if a row is selected
        if (selectedIndex < 0 || selectedIndex >= listMahasiswa.size()) {
            JOptionPane.showMessageDialog(
                    null,
                    "Pilih data mahasiswa yang akan dihapus",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Confirm deletion
        int confirm = JOptionPane.showConfirmDialog(
                null,
                "Apakah Anda yakin ingin menghapus data ini?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            // Get NIM of the selected mahasiswa
            String nim = listMahasiswa.get(selectedIndex).getNim();

            // Try to delete from database
            boolean result = database.deleteData(nim);

            if (result) {
                // Refresh the list and clear form
                populateListFromDatabase();
                clearForm();
                JOptionPane.showMessageDialog(
                        null,
                        "Data berhasil dihapus",
                        "Sukses",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "Gagal menghapus data",
                        "Kesalahan",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }



    private void clearForm() {
        nimField.setText("");
        namaField.setText("");
        jenisKelaminComboBox.setSelectedIndex(0);
        jurusanComboBox.setSelectedIndex(0);
        addButton.setText("Add");
    }

    @Override
    public void dispose() {
        if (database != null) {
            database.closeConnection();
        }
        super.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Menu().setVisible(true);
            }
        });
    }
}