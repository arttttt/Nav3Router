package com.arttttt.nav3router.sample.shared.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SimpleScreen(
    index: Int,
    pickedColor: Color? = null,
    onPickColor: (() -> Unit)? = null,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
    ) {
        Text(
            text = index.toString(),
        )

        if (pickedColor != null) {
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(pickedColor),
            )
        }

        if (onPickColor != null) {
            Button(
                modifier = Modifier.padding(top = 4.dp),
                onClick = onPickColor,
            ) {
                Text(text = "Pick color (for result)")
            }
        }
    }
}

@Preview
@Composable
private fun SimpleScreenPreview() {
    SimpleScreen(
        index = 0,
    )
}
