package com.example.spendmoney.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.spendmoney.model.Transaction
import com.example.spendmoney.model.dummyTransactions
import com.example.spendmoney.ui.components.SummaryCard
import com.example.spendmoney.ui.components.TransactionCard
import com.example.spendmoney.ui.theme.SpendMoneyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    transactions: List<Transaction>,
    onAddClick: () -> Unit,
    onTransactionClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedBottomItem by remember { mutableIntStateOf(0) }
    val totalExpense = transactions.sumOf { it.amount }

    val topBarTitle = when (selectedBottomItem) {
        0 -> "SpendMoney"
        1 -> "Daftar Transaksi"
        2 -> "Akun Saya"
        else -> "SpendMoney"
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
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
        },
        floatingActionButton = {
            if (selectedBottomItem != 2) {
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
                }

                1 -> {
                    // Tab Transaksi (Lengkap dengan fitur Pencarian & Filter Kategori)
                    TransactionsContent(
                        transactions = transactions,
                        onTransactionClick = onTransactionClick
                    )
                }

                2 -> {
                    // Tab Akun
                    AccountContent(
                        totalTransactionCount = transactions.size
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    SpendMoneyTheme {
        HomeScreen(
            transactions = dummyTransactions,
            onAddClick = {},
            onTransactionClick = {}
        )
    }
}
