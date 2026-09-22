package com.bedober.cashprize

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bedober.cashprize.ui.CashPrizeApp
import com.bedober.cashprize.ui.CashPrizeViewModel
import com.bedober.cashprize.ui.theme.CashPrizeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { CashPrizeTheme { CashPrizeApp(viewModel<CashPrizeViewModel>()) } }
    }
    override fun onNewIntent(intent: Intent) { super.onNewIntent(intent); setIntent(intent) }
}
