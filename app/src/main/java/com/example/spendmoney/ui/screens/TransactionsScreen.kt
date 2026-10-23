package com.example.spendmoney.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.example.spendmoney.model.Transaction
import com.example.spendmoney.model.dummyTransactions
import com.example.spendmoney.model.formatRupiah
import com.example.spendmoney.ui.components.TransactionCard
import com.example.spendmoney.ui.theme.SpendMoneyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsContent(
    windowSizeClass: WindowSizeClass,
    transactions: List<Transaction>,
    onTransactionClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Semua") }
    var selectedTransactionIdForPane by remember { mutableStateOf<String?>(null) }

    val isCompactWidth = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

    val categories = remember {
        listOf(
            "Semua",
            "Makanan & Minuman",
            "Tagihan",
            "Hiburan",
            "Transportasi",
            "Belanja",
            "Lainnya"
        )
    }

    val filteredTransactions = remember(searchQuery, selectedCategory, transactions) {
        transactions.filter { transaction ->
            val matchesCategory = selectedCategory == "Semua" ||
                    transaction.category.equals(selectedCategory, ignoreCase = true)

            val matchesSearch = searchQuery.isBlank() ||
                    transaction.title.contains(searchQuery, ignoreCase = true) ||
                    transaction.notes.contains(searchQuery, ignoreCase = true)

            matchesCategory && matchesSearch
        }
    }

    val totalFilteredAmount = remember(filteredTransactions) {
        filteredTransactions.sumOf { it.amount }
    }

    if (isCompactWidth) {
        // Layar HP Compact: Single Pane Mode
        TransactionsListPane(
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            selectedCategory = selectedCategory,
            onCategorySelect = { selectedCategory = it },
            categories = categories,
            filteredTransactions = filteredTransactions,
            totalFilteredAmount = totalFilteredAmount,
            onTransactionClick = onTransactionClick,
            modifier = modifier
        )
    } else {
        // Layar Tablet/Landscape (Medium & Expanded): Two-Pane Mode (Daftar di Kiri, Rincian di Kanan)
        Row(
            modifier = modifier
                .fillMaxSize()
                .padding(top = 12.dp)
        ) {
            Box(modifier = Modifier.weight(0.48f)) {
                TransactionsListPane(
                    searchQuery = searchQuery,
                    onSearchQueryChange = { searchQuery = it },
                    selectedCategory = selectedCategory,
                    onCategorySelect = { selectedCategory = it },
                    categories = categories,
                    filteredTransactions = filteredTransactions,
                    totalFilteredAmount = totalFilteredAmount,
                    onTransactionClick = { id ->
                        selectedTransactionIdForPane = id
                    }
                )
            }

            Box(
                modifier = Modifier
                    .weight(0.52f)
                    .fillMaxHeight()
                    .padding(16.dp)
            ) {
                val selectedTransaction = transactions.find { it.id == selectedTransactionIdForPane }
                    ?: filteredTransactions.firstOrNull()

                if (selectedTransaction != null) {
                    DetailTransactionCardView(
                        transaction = selectedTransaction,
                        onDeleteClick = {}
                    )
                } else {
                    Card(
                        modifier = Modifier.fillMaxSize(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Pilih transaksi di sebelah kiri\nuntuk melihat rincian",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsListPane(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedCategory: String,
    onCategorySelect: (String) -> Unit,
    categories: List<String>,
    filteredTransactions: List<Transaction>,
    totalFilteredAmount: Double,
    onTransactionClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 12.dp)
    ) {
        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = { Text("Cari transaksi...") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Cari"
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchQueryChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Hapus"
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Filter Chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories) { category ->
                val isSelected = selectedCategory == category
                FilterChip(
                    selected = isSelected,
                    onClick = { onCategorySelect(category) },
                    label = { Text(category) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Summary Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${filteredTransactions.size} Transaksi",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Total: ${formatRupiah(totalFilteredAmount)}",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // List
        if (filteredTransactions.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Tidak ada transaksi",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(
                    items = filteredTransactions,
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

// Preview 1: Phone Portrait (Compact)
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(name = "Phone - Portrait", device = Devices.PHONE, showBackground = true)
@Composable
fun TransactionsPhonePreview() {
    SpendMoneyTheme {
        TransactionsContent(
            windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(360.dp, 800.dp)),
            transactions = dummyTransactions,
            onTransactionClick = {}
        )
    }
}

// Preview 2: Tablet (Expanded Two-Pane)
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(name = "Tablet - Expanded Two Pane", device = Devices.TABLET, showBackground = true)
@Composable
fun TransactionsTabletPreview() {
    SpendMoneyTheme {
        TransactionsContent(
            windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(1280.dp, 800.dp)),
            transactions = dummyTransactions,
            onTransactionClick = {}
        )
    }
}
