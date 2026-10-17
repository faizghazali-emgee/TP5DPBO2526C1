# TP5 DPBO 25 26 C1

# Janji Setia
Saya <strong>Faiz Muhammad Ghazali</strong> dengan <strong>NIM 2504531</strong> mengerjakan Tugas Praktikum 5 dalam mata kuliah Desain dan Pemrograman Berorientasi Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.

# Desain
- Person
  - id : String
  - nama : String
  - tahunLahir : int
  - kategori : String
  - jenisKelamin : String
- PeopleMenu
  - JTextField idField;
  - JTextField namaField;
  - JTextField tahunLahirField;
  - JComboBox<String> kategoriComboBox;
  - JRadioButton rbLaki;
  - JRadioButton rbPerempuan;
  - ButtonGroup bgGender;
  - JTable personTable;
  - JButton addUpdateButton;
  - JButton deleteButton;
  - JButton cancelButton;
  - JLabel titleLabel;
  - JPanel mainPanel;
- Database
  - Menangani koneksi dan eksekusi query SQL (CRUD) ke database MySQL.

# Struktur File

```

Main
├── PeopleData/
│   └── src/
│       ├── Person.java
│       ├── PeopleMenu.java
│       ├── PeopleMenu.form
│       └── Database.java
│
├── Dokumentasi/
│   └── Remove.gif
│   └── Add.gif
│
└── README.md

```

# Penjelasan File

Program ini menerapkan konsep Pemrograman Berorientasi Objek (**OOP**) dan pengembangan antarmuka grafis menggunakan **Java Swing** yang terintegrasi secara penuh dengan database **MySQL** dalam studi kasus **Aplikasi Pendataan Civitas Akademika (PeopleData)**.

Sistem memiliki beberapa komponen kelas yang saling terhubung:
1. **Person**: *Model class* yang menyimpan atribut dasar individu seperti ID, nama, tahun lahir, kategori, dan jenis kelamin.
2. **Database**: *Utility class* yang bertindak sebagai penghubung dan pengelola komunikasi query SQL dengan database MySQL (`db_people`).
3. **PeopleMenu**: *Controller & GUI class* berbasis JFrame yang menangani antarmuka formulir, tabel data, serta logika CRUD (*Create, Read, Update, Delete*) langsung ke database tanpa menggunakan `ArrayList`.

**Fitur & Ketentuan Utama:**
- Seluruh proses CRUD terhubung langsung ke database MySQL (`INSERT`, `SELECT`, `UPDATE`, `DELETE`).
- Penambahan data person baru melalui form input interaktif (`idField`, `namaField`, `tahunLahirField`, `kategoriComboBox`, dan `rbLaki`/`rbPerempuan`).
- Pembaruan (*update*) data person yang sudah ada dengan memilih baris pada tabel.
- Penghapusan data dengan konfirmasi prompt dialog (*confirmation dialog*).
- Pembersihan form secara otomatis menggunakan fungsi `clearForm()`.

## ❌ ERROR HANDLING & VALIDASI

Program dilengkapi dengan penanganan kesalahan untuk memastikan integritas data input:
- **Validasi Kosong:** Program menampilkan dialog error jika masih ada kolom input (*ID*, *Nama*, *Tahun Lahir*) yang kosong saat *insert* atau *update*.
- **Validasi ID Duplikat:** Program mengecek ke database untuk memastikan tidak ada ID yang sama saat proses penambahan data baru (*insert*).
- **Validasi Angka (*NumberFormatException*):** Program memastikan bahwa input pada field tahun lahir harus berupa angka yang valid menggunakan blok `try-catch`. Jika salah, pesan *error* akan muncul melalui `JOptionPane`.

# Dokumentasi

## Add
![Add](Dokumentasi/Add.gif)

## Update
![Update](Dokumentasi/Update.gif)

## Remove
![Remove](Dokumentasi/Delete.gif)

## Error
![Error](Dokumentasi/Error.gif)

```