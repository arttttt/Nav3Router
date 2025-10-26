package com.arttttt.nav3router

import androidx.compose.runtime.Composable

@Composable
actual fun BackInterceptionProvider(
    interceptionEnabled: Boolean,
    content: @Composable (() -> Unit),
) {
    content()
}