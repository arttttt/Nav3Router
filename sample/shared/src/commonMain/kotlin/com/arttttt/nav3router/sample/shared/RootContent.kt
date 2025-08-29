package com.arttttt.nav3router.sample.shared

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import com.arttttt.nav3router.Nav3Host

@Composable
fun RootContent() {
    Nav3Host<Screen>(
        backStack = rememberNavBackStack(Screen.Main(0))
    ) { backStack, onBack, router ->
        NavDisplay(
            modifier = Modifier,
            backStack = backStack,
            onBack = onBack,
            entryProvider = entryProvider {
                entry<Screen.Main> { screen ->
                    MainScreen(
                        index = screen.index,
                        onPop = {
                            router.popUp()
                        },
                        onPush = { index ->
                            router.push(
                                Screen.Main(index + 1)
                            )
                        },
                        onReplace = { index ->
                            router.replaceCurrent(
                                Screen.Main(index + 1)
                            )
                        },
                        onNewChain = { index ->
                            router.replaceStack(
                                Screen.Main(index + 1),
                                Screen.Main(index + 2),
                                Screen.Main(index + 3)
                            )
                        },
                        onNewRoot = { index ->
                            router.replaceStack(
                                Screen.Main(index + 1)
                            )
                        },
                        onFinishChain = {
                            router.clearStack()
                        },
                    )
                }
            },
        )
    }
}