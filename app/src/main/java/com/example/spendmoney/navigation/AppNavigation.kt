package com.example.spendmoney.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.spendmoney.model.dummyTransactions
import com.example.spendmoney.ui.screens.AddTransactionScreen
import com.example.spendmoney.ui.screens.DetailTransactionScreen
import com.example.spendmoney.ui.screens.HomeScreen

object Screen {
    const val Home = "home"
    const val AddTransaction = "add_transaction"
    const val DetailTransaction = "detail_transaction/{transactionId}"
    fun createDetailRoute(transactionId: String) = "detail_transaction/$transactionId"
}

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val transactions = remember {
        mutableStateListOf(*dummyTransactions.toTypedArray())
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Home,
        modifier = modifier
    ) {
        composable(Screen.Home) {
            HomeScreen(
                transactions = transactions,
                onAddClick = {
                    navController.navigate(Screen.AddTransaction)
                },
                onTransactionClick = { id ->
                    navController.navigate(Screen.createDetailRoute(id))
                }
            )
        }

        composable(Screen.AddTransaction) {
            AddTransactionScreen(
                onSaveTransaction = { newTransaction ->
                    transactions.add(0, newTransaction)
                    navController.popBackStack()
                },
                onCancel = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.DetailTransaction,
            arguments = listOf(
                navArgument("transactionId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val transactionId = backStackEntry.arguments?.getString("transactionId")
            val transaction = transactions.find { it.id == transactionId }

            DetailTransactionScreen(
                transaction = transaction,
                onBackClick = {
                    navController.popBackStack()
                },
                onDeleteClick = { id ->
                    transactions.removeAll { it.id == id }
                    navController.popBackStack()
                }
            )
        }
    }
}
