**Janji**

Saya Armelia Zahrah Mumtaz dengan NIM 2300801 berjanji mengerjakan TP5 DPBO dengan keberkahan-Nya, maka saya tidak akan melakukan kecurangan sesuai yang telah di spesifikasikan, Aamiin

**Desain Program**

1. Database Class

Bertanggung jawab untuk koneksi database MySQL
Mengimplementasikan metode CRUD
Melakukan validasi data sebelum operasi database


2. Menu Class

Antarmuka grafis menggunakan Java Swing
Menampilkan form input data mahasiswa
Mengatur interaksi pengguna
Mengintegrasikan operasi database


3. Mahasiswa Class

Model data untuk objek mahasiswa
Menyimpan informasi dasar mahasiswa

**Alur Penjelasan**


Alur program manajemen data mahasiswa dimulai ketika pengguna membuka aplikasi. Pertama, aplikasi melakukan koneksi ke database MySQL menggunakan JDBC. Antarmuka utama akan menampilkan form input dengan empat field utama: NIM, Nama, Jenis Kelamin (dropdown), dan Jurusan (dropdown).

Ketika pengguna ingin menambah data, mereka mengisi form dan menekan tombol "Add". Sistem akan melakukan validasi input terlebih dahulu, memastikan tidak ada field kosong dan NIM yang dimasukkan belum pernah digunakan sebelumnya. Jika validasi berhasil, data akan disimpan ke database dan secara otomatis memperbarui tabel yang menampilkan daftar mahasiswa.

Untuk mengupdate data, pengguna dapat mengklik baris data di tabel yang ingin diubah. Informasi mahasiswa akan otomatis terisi di form. Setelah melakukan perubahan dan menekan tombol "Update", sistem kembali melakukan validasi sebelum menyimpan perubahan ke database.

Fitur hapus data memungkinkan pengguna menghapus data mahasiswa yang dipilih dari tabel. Setiap operasi - baik tambah, update, atau hapus - dilengkapi dengan pesan konfirmasi dan notifikasi status operasi kepada pengguna.

Saat menutup aplikasi, koneksi database akan ditutup secara otomatis untuk mencegah kebocoran sumber daya.
