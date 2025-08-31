package com.arttttt.nav3router.sample.shared

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun BottomSheetScreen() {
    BottomSheetScreenContent()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomSheetScreenContent() {
    ModalBottomSheet(
        onDismissRequest = {},
    ) {}
}

@Preview
@Composable
private fun BottomSheetScreenContentPreview() {
    BottomSheetScreenContent()
}