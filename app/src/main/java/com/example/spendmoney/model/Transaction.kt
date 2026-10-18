package com.example.spendmoney.model

import java.text.NumberFormat
import java.util.Locale

data class Transaction(
    val id: String,
    val title: String,
    val category: String,
    val amount: Double,
    val date: String,
    val notes: String
)

fun formatRupiah(amount: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID"))
    formatter.maximumFractionDigits = 0
    return formatter.format(amount).replace("Rp", "Rp ")
}

val dummyTransactions = listOf(
    Transaction(
        id = "1",
        title = "Kopi Kenangan",
        category = "Makanan & Minuman",
        amount = 28000.0,
        date = "24 Okt 2023",
        notes = "Kopi kenangan mantan regular"
    ),
    Transaction(
        id = "2",
        title = "Tagihan Listrik",
        category = "Tagihan",
        amount = 350000.0,
        date = "20 Okt 2023",
        notes = "Token PLN rumah bulanan"
    ),
    Transaction(
        id = "3",
        title = "Langganan Spotify",
        category = "Hiburan",
        amount = 54990.0,
        date = "15 Okt 2023",
        notes = "Paket premium individual"
    ),
    Transaction(
        id = "4",
        title = "Bensin Pertamax",
        category = "Transportasi",
        amount = 100000.0,
        date = "12 Okt 2023",
        notes = "Pengisian bensin motor"
    ),
    Transaction(
        id = "5",
        title = "Belanja Supermarket",
        category = "Belanja",
        amount = 425000.0,
        date = "08 Okt 2023",
        notes = "Kebutuhan pokok mingguan"
    )
)
