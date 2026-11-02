# SpendMoney - Aplikasi Catatan Keuangan Adaptif

Aplikasi Android SpendMoney dibangun menggunakan **Jetpack Compose** dengan desain tata letak adaptif (*Adaptive Layout*) untuk mendukung berbagai ukuran layar (Smartphone & Tablet).

# Nama Anggota Kelompok:

1. Bintang Rangga Saputra - 241080200140
2. Muchammad Fiqy Effendy - 241080200108

---

## 1. Keberadaan Syntax Adaptive Layout (`syntax adaptive berada di mana?`)

Syntax tata letak adaptif (*Adaptive Layout*) pada proyek ini tersebar di beberapa file utama:

1. **[MainActivity.kt](file:///home/odass/AndroidStudioProjects/SpendMoney/app/src/main/java/com/example/spendmoney/MainActivity.kt)**
   - Menggunakan `calculateWindowSizeClass(this)` dari library `androidx.compose.material3.windowsizeclass` untuk menghitung kelas ukuran jendela layar device saat ini (`WindowSizeClass`).
   - `WindowSizeClass` diteruskan ke `AppNavigation(windowSizeClass = windowSizeClass)`.

2. **[AppNavigation.kt](file:///home/odass/AndroidStudioProjects/SpendMoney/app/src/main/java/com/example/spendmoney/navigation/AppNavigation.kt)**
   - Meneruskan parameter `windowSizeClass` ke layar-layar utama seperti `HomeScreen`, `AddTransactionScreen`, `TransactionsContent`, dan `AccountContent`.

3. **[HomeScreen.kt](file:///home/odass/AndroidStudioProjects/SpendMoney/app/src/main/java/com/example/spendmoney/ui/screens/HomeScreen.kt)**
   - Memeriksa kondisi lebar layar:
     ```kotlin
     val isCompactWidth = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact
     ```
   - **Tampilan Smartphone (Compact Width)**: Menampilkan `NavigationBar` (Footer Bar di bawah) dan `FloatingActionButton` (Tombol Plus di kanan bawah) serta daftar transaksi 1 kolom (`LazyColumn`).
   - **Tampilan Tablet/Landscape (Medium & Expanded Width)**: Menampilkan `NavigationRail` (Navigasi samping kiri) dengan `FloatingActionButton` di bagian `header` (kiri atas) serta daftar transaksi 2 kolom grid (`LazyVerticalGrid`).

4. **[TransactionsScreen.kt](file:///home/odass/AndroidStudioProjects/SpendMoney/app/src/main/java/com/example/spendmoney/ui/screens/TransactionsScreen.kt)**
   - **Smartphone**: Menampilkan daftar transaksi 1 layar (Single Pane).
   - **Tablet**: Menampilkan pola **Two-Pane Layout** (Daftar transaksi di panel kiri 48% dan detail rincian transaksi di panel kanan 52%).

5. **[AccountScreen.kt](file:///home/odass/AndroidStudioProjects/SpendMoney/app/src/main/java/com/example/spendmoney/ui/screens/AccountScreen.kt)** & **[AddTransactionScreen.kt](file:///home/odass/AndroidStudioProjects/SpendMoney/app/src/main/java/com/example/spendmoney/ui/screens/AddTransactionScreen.kt)**
   - Mengubah layout dari 1 kolom bertumpuk vertikal di Smartphone menjadi 2 kolom berdampingan (`Row`) di layar Tablet.

---

## 2. Cara Mengganti Warna Menu Footer (`ganti warna menu footer`)

Menu footer (`NavigationBar`) pada smartphone diatur di dalam file **[HomeScreen.kt](file:///home/odass/AndroidStudioProjects/SpendMoney/app/src/main/java/com/example/spendmoney/ui/screens/HomeScreen.kt)**.

Untuk mengganti warna latar belakang (*container*) dan item menu footer:

```kotlin
NavigationBar(
    // 1. Mengganti warna background footer menu
    containerColor = MaterialTheme.colorScheme.primaryContainer, // Atau warna lain seperti Color(0xFFXXXXXX) / MaterialTheme.colorScheme.surfaceVariant
    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
    tonalElevation = 8.dp
) {
    NavigationBarItem(
        selected = selectedBottomItem == 0,
        onClick = { selectedBottomItem = 0 },
        icon = { Icon(Icons.Default.Home, contentDescription = "Beranda") },
        label = { Text("Beranda") },
        // 2. Mengganti warna ikon & teks saat dipilih / tidak dipilih (opsional)
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.primary,
            selectedTextColor = MaterialTheme.colorScheme.primary,
            indicatorColor = MaterialTheme.colorScheme.secondaryContainer,
            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}
```

---

## 3. Cara Menambahkan Satu Menu Footer (`tambahkan satu menu footer`)

Untuk menambahkan menu baru pada footer (`NavigationBar`) dan `NavigationRail`:

1. Buka file **[HomeScreen.kt](file:///home/odass/AndroidStudioProjects/SpendMoney/app/src/main/java/com/example/spendmoney/ui/screens/HomeScreen.kt)**.
2. Tambahkan judul pada `topBarTitle`:
   ```kotlin
   val topBarTitle = when (selectedBottomItem) {
       0 -> "SpendMoney"
       1 -> "Daftar Transaksi"
       2 -> "Akun Saya"
       3 -> "Pengaturan" // Menu Baru (Indeks 3)
       else -> "SpendMoney"
   }
   ```
3. Tambahkan `NavigationBarItem` pada `NavigationBar` (Smartphone):
   ```kotlin
   NavigationBarItem(
       selected = selectedBottomItem == 3,
       onClick = { selectedBottomItem = 3 },
       icon = { Icon(Icons.Default.Settings, contentDescription = "Pengaturan") },
       label = { Text("Pengaturan") }
   )
   ```
4. Tambahkan `NavigationRailItem` pada `NavigationRail` (Tablet):
   ```kotlin
   NavigationRailItem(
       selected = selectedBottomItem == 3,
       onClick = { selectedBottomItem = 3 },
       icon = { Icon(Icons.Default.Settings, contentDescription = "Pengaturan") },
       label = { Text("Pengaturan") }
   )
   ```
5. Tambahkan penanganan halaman pada `when (selectedBottomItem)`:
   ```kotlin
   when (selectedBottomItem) {
       0 -> { /* Beranda */ }
       1 -> { /* Transaksi */ }
       2 -> { /* Akun */ }
       3 -> {
           // Halaman Pengaturan baru
           SettingsContent()
       }
   }
   ```

---

## 4. Penjelasan Posisi Tanda Plus (FAB): Smartphone di Bawah Kanan vs Tablet di Atas Kiri

Penempatan tombol Plus (`FloatingActionButton`) disesuaikan secara adaptif berdasarkan ukuran layar di **[HomeScreen.kt](file:///home/odass/AndroidStudioProjects/SpendMoney/app/src/main/java/com/example/spendmoney/ui/screens/HomeScreen.kt)**:

- **Pada Smartphone (`isCompactWidth == true`)**:
  FAB diletakkan di dalam properti `floatingActionButton` pada `Scaffold`:
  ```kotlin
  floatingActionButton = {
      if (isCompactWidth && selectedBottomItem != 2) {
          FloatingActionButton(
              onClick = onAddClick,
              containerColor = MaterialTheme.colorScheme.primary,
              contentColor = MaterialTheme.colorScheme.onPrimary
          ) {
              Icon(imageVector = Icons.Default.Add, contentDescription = "Tambah Transaksi")
          }
      }
  }
  ```
  Secara bawaan Jetpack Compose `Scaffold`, posisi `floatingActionButton` diletakkan pada **pojok kanan bawah** layar (`FabPosition.End`).

- **Pada Tablet (`isCompactWidth == false`)**:
  FAB diletakkan di dalam komponen `NavigationRail` pada bagian `header`:
  ```kotlin
  if (!isCompactWidth) {
      NavigationRail(
          containerColor = MaterialTheme.colorScheme.surface,
          header = {
              FloatingActionButton(
                  onClick = onAddClick,
                  containerColor = MaterialTheme.colorScheme.primary,
                  contentColor = MaterialTheme.colorScheme.onPrimary,
                  modifier = Modifier.padding(vertical = 12.dp)
              ) {
                  Icon(imageVector = Icons.Default.Add, contentDescription = "Tambah Transaksi")
              }
          }
      ) {
          // Item navigasi samping (Beranda, Transaksi, Akun, Pengaturan)
      }
  }
  ```
  Karena `NavigationRail` terletak di sisi **paling kiri** layar, menempatkan `FloatingActionButton` di bagian `header` `NavigationRail` secara otomatis memosisikannya di **pojok atas kiri** layar tablet.
