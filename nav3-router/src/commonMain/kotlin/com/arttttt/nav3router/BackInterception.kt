package com.arttttt.nav3router

import androidx.compose.runtime.Composable

/**
 * Provides automatic back interception for nested navigation containers.
 *
 * This prevents nested NavigationBackHandlers from intercepting back events that
 * should be handled by parent navigation containers.
 *
 * @param interceptionEnabled Whether to enable back event interception.
 *                           Should be true for nested containers, false for root.
 * @param content The composable content that will be wrapped with interception
 */
@Composable
expect fun BackInterceptionProvider(
    interceptionEnabled: Boolean,
    content: @Composable () -> Unit
)