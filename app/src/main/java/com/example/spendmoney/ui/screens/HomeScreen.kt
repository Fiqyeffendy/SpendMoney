package com.example.spendmoney.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.example.spendmoney.model.Transaction
import com.example.spendmoney.model.dummyTransactions
import com.example.spendmoney.ui.components.SummaryCard
import com.example.spendmoney.ui.components.TransactionCard
import com.example.spendmoney.ui.theme.SpendMoneyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    windowSizeClass: WindowSizeClass,
    transactions: List<Transaction>,
    onAddClick: () -> Unit,
    onTransactionClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedBottomItem by remember { mutableIntStateOf(0) }
    val totalExpense = transactions.sumOf { it.amount }

    val isCompactWidth = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

    val topBarTitle = when (selectedBottomItem) {
        0 -> "SpendMoney"
        1 -> "Daftar Transaksi"
        2 -> "Akun Saya"
        else -> "SpendMoney"
    }

    Row(modifier = modifier.fillMaxSize()) {
        // Adaptif: Pada layar sedang/lebar (Medium/Expanded), gunakan Navigation Rail di kiri
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
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Tambah Transaksi"
                        )
                    }
                }
            ) {
                Spacer(modifier = Modifier.weight(1f))
                NavigationRailItem(
                    selected = selectedBottomItem == 0,
                    onClick = { selectedBottomItem = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Beranda") },
                    label = { Text("Beranda") }
                )
                NavigationRailItem(
                    selected = selectedBottomItem == 1,
                    onClick = { selectedBottomItem = 1 },
                    icon = { Icon(Icons.Default.Info, contentDescription = "Transaksi") },
                    label = { Text("Transaksi") }
                )
                NavigationRailItem(
                    selected = selectedBottomItem == 2,
                    onClick = { selectedBottomItem = 2 },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Akun") },
                    label = { Text("Akun") }
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }

        Scaffold(
            modifier = Modifier.weight(1f),
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = topBarTitle,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
            bottomBar = {
                // Adaptif: Pada layar HP portrait (Compact), gunakan Bottom Navigation Bar
                if (isCompactWidth) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 8.dp
                    ) {
                        NavigationBarItem(
                            selected = selectedBottomItem == 0,
                            onClick = { selectedBottomItem = 0 },
                            icon = { Icon(Icons.Default.Home, contentDescription = "Beranda") },
                            label = { Text("Beranda") }
                        )
                        NavigationBarItem(
                            selected = selectedBottomItem == 1,
                            onClick = { selectedBottomItem = 1 },
                            icon = { Icon(Icons.Default.Info, contentDescription = "Transaksi") },
                            label = { Text("Transaksi") }
                        )
                        NavigationBarItem(
                            selected = selectedBottomItem == 2,
                            onClick = { selectedBottomItem = 2 },
                            icon = { Icon(Icons.Default.Person, contentDescription = "Akun") },
                            label = { Text("Akun") }
                        )
                    }
                }
            },
            floatingActionButton = {
                // FAB di pojok kanan bawah hanya ditampilkan pada layar HP Compact
                if (isCompactWidth && selectedBottomItem != 2) {
                    FloatingActionButton(
                        onClick = onAddClick,
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Tambah Transaksi"
                        )
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (selectedBottomItem) {
                    0 -> {
                        // Tab Beranda
                        if (isCompactWidth) {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                item {
                                    SummaryCard(
                                        totalExpense = totalExpense,
                                        periodMonth = "Oktober 2023"
                                    )
                                }

                                item {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Riwayat Transaksi",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                }

                                if (transactions.isEmpty()) {
                                    item {
                                        Text(
                                            text = "Belum ada transaksi",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.padding(vertical = 24.dp)
                                        )
                                    }
                                } else {
                                    items(
                                        items = transactions,
                                        key = { it.id }
                                    ) { transaction ->
                                        TransactionCard(
                                            transaction = transaction,
                                            onClick = { onTransactionClick(transaction.id) }
                                        )
                                    }
                                }
                            }
                        } else {
                            // Adaptif: Pada Layar Lebar (Tablet / Landscape), gunakan Grid 2 Kolom untuk transaksi
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(20.dp)
                            ) {
                                SummaryCard(
                                    totalExpense = totalExpense,
                                    periodMonth = "Oktober 2023",
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = "Riwayat Transaksi",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = MaterialTheme.colorScheme.onBackground
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(2),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp),
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    items(
                                        items = transactions,
                                        key = { it.id }
                                    ) { transaction ->
                                        TransactionCard(
                                            transaction = transaction,
                                            onClick = { onTransactionClick(transaction.id) }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    1 -> {
                        // Tab Transaksi (Adaptif List-Detail Two Pane di Layar Lebar)
                        TransactionsContent(
                            windowSizeClass = windowSizeClass,
                            transactions = transactions,
                            onTransactionClick = onTransactionClick
                        )
                    }

                    2 -> {
                        // Tab Akun
                        AccountContent(
                            windowSizeClass = windowSizeClass,
                            totalTransactionCount = transactions.size
                        )
                    }
                }
            }
        }
    }
}

// Preview 1: HP Portrait (Compact)
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(name = "Phone - Portrait", device = Devices.PHONE, showBackground = true)
@Composable
fun HomeScreenPhonePreview() {
    SpendMoneyTheme {
        HomeScreen(
            windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(360.dp, 800.dp)),
            transactions = dummyTransactions,
            onAddClick = {},
            onTransactionClick = {}
        )
    }
}

// Preview 2: Tablet (Expanded)
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(name = "Tablet - Expanded", device = Devices.TABLET, showBackground = true)
@Composable
fun HomeScreenTabletPreview() {
    SpendMoneyTheme {
        HomeScreen(
            windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(1280.dp, 800.dp)),
            transactions = dummyTransactions,
            onAddClick = {},
            onTransactionClick = {}
        )
    }
}
