package com.arttttt.nav3router.sample.shared

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import com.arttttt.nav3router.Nav3Host

@Composable
fun RootContent() {
    Nav3Host<Screen>(
        backStack = rememberNavBackStack(Screen.Main)
    ) { backStack, onBack, router ->
        NavDisplay(
            modifier = Modifier,
            backStack = backStack,
            onBack = onBack,
            entryProvider = entryProvider {
                entry<Screen.Main> {

                }
            },
        )
    }
}