package com.arttttt.nav3router.sample.shared.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.arttttt.nav3router.sample.shared.ColorResult

private val palette = listOf(
    ColorResult(argb = 0xFFE53935L, name = "Red"),
    ColorResult(argb = 0xFF43A047L, name = "Green"),
    ColorResult(argb = 0xFF1E88E5L, name = "Blue"),
    ColorResult(argb = 0xFFFDD835L, name = "Yellow"),
)

@Composable
fun ColorPickerScreen(
    onPick: (ColorResult) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
    ) {
        Text(text = "Pick a color")

        palette.forEach { color ->
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onPick(color) },
            ) {
                androidx.compose.foundation.layout.Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(color.argb.toInt())),
                )
                Text(
                    modifier = Modifier.padding(start = 12.dp),
                    text = color.name,
                )
            }
        }
    }
}
