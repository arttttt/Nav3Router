package com.arttttt.nav3router.sample.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DialogScreen(
    onClick: () -> Unit,
) {
    DialogScreenContent(
        onClick = onClick,
    )
}

@Composable
private fun DialogScreenContent(
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = "this is dialog",
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Button(
                onClick = onClick,
            ) {
                Text(
                    text = "Ok"
                )
            }

            Button(
                onClick = onClick,
            ) {
                Text(
                    text = "Cancel"
                )
            }
        }
    }
}

@Preview
@Composable
private fun DialogScreenContentPreview() {
    DialogScreenContent(
        onClick = {},
    )
}