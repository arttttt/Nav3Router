package com.arttttt.nav3router.sample.shared.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun BottomSheetScreen() {
    BottomSheetScreenContent()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomSheetScreenContent() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        repeat(10) {
            Text(
                text = "Item $it"
            )
        }
    }
}

@Preview
@Composable
private fun BottomSheetScreenContentPreview() {
    BottomSheetScreenContent()
}