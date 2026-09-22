package com.bedober.cashprize

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bedober.cashprize.ui.CashPrizeApp
import com.bedober.cashprize.ui.CashPrizeViewModel
import com.bedober.cashprize.ui.theme.CashPrizeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CashPrizeTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    CashPrizeApp(viewModel<CashPrizeViewModel>())
                }
            }
        }
    }
}
