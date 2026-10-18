package com.example.spendmoney

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.spendmoney.navigation.AppNavigation
import com.example.spendmoney.ui.theme.SpendMoneyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpendMoneyTheme {
                AppNavigation()
            }
        }
    }
}
