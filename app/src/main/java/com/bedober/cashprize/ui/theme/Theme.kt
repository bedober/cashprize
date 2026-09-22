package com.bedober.cashprize.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CashPrizeColors = lightColorScheme(primary = Color(0xFF6750A4), secondary = Color(0xFF625B71))

@Composable
fun CashPrizeTheme(content: @Composable () -> Unit) { MaterialTheme(colorScheme = CashPrizeColors, content = content) }
