import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class Database {
    private Connection connection;
    private final String url = "jdbc:mysql://localhost:3306/db_mahasiswa";
    private final String username = "root";
    private final String password = "";

    public Database() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database connection successful!");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }
    }

    // Metode untuk mengecek apakah NIM sudah ada
    public boolean isNIMExists(String nim) {
        try {
            PreparedStatement checkNIM = connection.prepareStatement("SELECT * FROM mahasiswa WHERE nim = ?");
            checkNIM.setString(1, nim);
            ResultSet rs = checkNIM.executeQuery();
            return rs.next(); // Mengembalikan true jika NIM sudah ada
        } catch (SQLException e) {
            System.out.println("Error checking NIM: " + e.getMessage());
            return false;
        }
    }

    public boolean validateInput(String nim, String nama, String jenisKelamin, String jurusan) {
        return !(nim.trim().isEmpty() ||
                nama.trim().isEmpty() ||
                jenisKelamin.trim().isEmpty() ||
                jenisKelamin.equals("") ||
                jurusan.trim().isEmpty() ||
                jurusan.equals(""));
    }

    public DefaultTableModel getDataTable() {
        DefaultTableModel model = new DefaultTableModel(new String[]{"NIM", "Nama", "Jenis Kelamin", "Jurusan"}, 0);

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM mahasiswa");

            while (resultSet.next()) {
                String nim = resultSet.getString("nim");
                String nama = resultSet.getString("nama");
                String jenisKelamin = resultSet.getString("jenis_kelamin");
                String jurusan = resultSet.getString("jurusan");

                model.addRow(new Object[]{nim, nama, jenisKelamin, jurusan});
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving data: " + e.getMessage());
        }

        return model;
    }

    public int insertData(String nim, String nama, String jenisKelamin, String jurusan) {
        // Validasi input kosong
        if (!validateInput(nim, nama, jenisKelamin, jurusan)) {
            return -1; // Input kosong
        }

        // Cek apakah NIM sudah ada
        if (isNIMExists(nim)) {
            return -2; // NIM sudah ada
        }

        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO mahasiswa (nim, nama, jenis_kelamin, jurusan) VALUES (?, ?, ?, ?)"
            );
            statement.setString(1, nim);
            statement.setString(2, nama);
            statement.setString(3, jenisKelamin);
            statement.setString(4, jurusan);

            statement.executeUpdate();
            return 1; // Berhasil insert
        } catch (SQLException e) {
            System.out.println("Error inserting data: " + e.getMessage());
            return 0; // Gagal insert
        }
    }

    public int updateData(String nimLama, String nimBaru, String nama, String jenisKelamin, String jurusan) {
        // Validasi input kosong
        if (!validateInput(nimBaru, nama, jenisKelamin, jurusan)) {
            return -1; // Input kosong
        }

        try {
            // Jika NIM baru berbeda dengan NIM lama, cek apakah NIM baru sudah ada
            if (!nimLama.equals(nimBaru) && isNIMExists(nimBaru)) {
                return -2; // NIM sudah ada
            }

            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE mahasiswa SET nim = ?, nama = ?, jenis_kelamin = ?, jurusan = ? WHERE nim = ?"
            );
            statement.setString(1, nimBaru);
            statement.setString(2, nama);
            statement.setString(3, jenisKelamin);
            statement.setString(4, jurusan);
            statement.setString(5, nimLama);

            int rowsAffected = statement.executeUpdate();

            // Cek apakah data berhasil diupdate
            return (rowsAffected > 0) ? 1 : 0;
        } catch (SQLException e) {
            System.out.println("Error updating data: " + e.getMessage());
            return 0;
        }
    }

    // Method untuk menghapus data mahasiswa
    public boolean deleteData(String nim) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "DELETE FROM mahasiswa WHERE nim = ?"
            );
            statement.setString(1, nim);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting data: " + e.getMessage());
            return false;
        }
    }

    // Method untuk menutup koneksi database
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }
}