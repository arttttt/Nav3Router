package com.arttttt.nav3router.sample.shared.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import com.arttttt.nav3router.Nav3Host
import com.arttttt.nav3router.rememberRouter
import com.arttttt.nav3router.sample.shared.ButtonsGrid
import com.arttttt.nav3router.sample.shared.DialogSceneStrategy
import com.arttttt.nav3router.sample.shared.NavDisplay
import com.arttttt.nav3router.sample.shared.Screen
import com.arttttt.nav3router.sample.shared.SinglePaneSceneStrategy
import com.arttttt.nav3router.sample.shared.createStackManipulationButtons
import com.arttttt.nav3router.sample.shared.dialog
import com.arttttt.nav3router.sample.shared.rememberNavBackStack
import com.arttttt.nav3router.sample.shared.strategies.BottomSheetSceneStrategy
import com.arttttt.nav3router.sample.shared.strategies.DelegatedScreenStrategy
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NestedContainerScreen() {
    var index by remember { mutableIntStateOf(0) }
    val router = rememberRouter<Screen>()

    val backStack = rememberNavBackStack(Screen.Simple(0))

    val backStackString by derivedStateOf {
        backStack.joinToString(
            separator = "->"
        ) { screen ->
            when (screen) {
                is Screen.Simple -> screen.index.toString()
                else -> screen.toString()
            }
        }
    }

    val stackManipulationButtons = createStackManipulationButtons(
        onPop = {
            router.pop()
            index--
        },
        onPush = {
            router.push(Screen.Simple(++index))
        },
        onReplace = {
            router.replaceCurrent(Screen.Simple(++index))
        },
        onNewChain = {
            router.push(
                Screen.Simple(++index),
                Screen.Simple(++index),
                Screen.Simple(++index),
            )
        },
        onReplaceStack = {
            router.replaceStack(Screen.Simple(++index))
        },
        onClearStack = {
            router.clearStack()
        },
        onDropStack = {
            router.dropStack()
        },
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
    ) {
        Text(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 4.dp,
            ),
            text = "Stack: $backStackString",
            maxLines = 1,
            overflow = TextOverflow.MiddleEllipsis,
        )

        Text(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 4.dp,
            ),
            text = "Nav3 container",
        )

        Nav3Host(
            backStack = backStack,
            router = router,
        ) { backStack, onBack, router ->
            NavDisplay(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f)
                    .border(
                        2.dp,
                        Color.Red
                    ),
                backStack = backStack,
                sceneStrategy = DelegatedScreenStrategy(
                    strategyMap = mapOf(
                        "bottomsheet" to BottomSheetSceneStrategy(),
                        "dialog" to DialogSceneStrategy(),
                    ),
                    fallbackStrategy = SinglePaneSceneStrategy(),
                ),
                onBack = {
                    if (backStack.lastOrNull() is Screen.Simple) {
                        index--
                    }
                    onBack(it)
                },
                entryProvider = entryProvider {
                    entry<Screen.Simple> { screen ->
                        SimpleScreen(
                            index = screen.index,
                        )
                    }

                    entry<Screen.BottomSheet>(
                        metadata = BottomSheetSceneStrategy.bottomSheet(),
                    ) {
                        BottomSheetScreen()
                    }

                    entry<Screen.Dialog>(
                        metadata = DialogSceneStrategy.Companion.dialog(),
                    ) {
                        DialogScreen(
                            onClick = {
                                router.pop()
                            },
                        )
                    }
                },
            )
        }

        ButtonsGrid(stackManipulationButtons)
    }
}

@Preview
@Composable
private fun NestedContainerScreenContentPreview() {
    NestedContainerScreen()
}