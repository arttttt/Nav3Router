package com.arttttt.nav3router

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.navigationevent.NavigationEventDispatcher
import androidx.navigationevent.NavigationEventDispatcherOwner
import androidx.navigationevent.compose.LocalNavigationEventDispatcherOwner

@Composable
actual fun BackInterceptionProvider(
    interceptionEnabled: Boolean,
    content: @Composable () -> Unit
) {
    val parentOwner = LocalNavigationEventDispatcherOwner.current

    if (interceptionEnabled && parentOwner != null) {
        val interceptingOwner = remember(parentOwner) {
            object : NavigationEventDispatcherOwner {

                override val navigationEventDispatcher: NavigationEventDispatcher = NavigationEventDispatcher(
                    parent = parentOwner.navigationEventDispatcher,
                ).apply {
                    isEnabled = false
                }
            }
        }

        CompositionLocalProvider(LocalNavigationEventDispatcherOwner provides interceptingOwner) {
            content()
        }
    } else {
        content()
    }
}