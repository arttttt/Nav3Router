package com.arttttt.nav3router.sample.shared

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import com.arttttt.nav3router.Nav3Host
import com.arttttt.nav3router.Router
import com.arttttt.nav3router.rememberRouter
import com.arttttt.nav3router.sample.shared.navigationButtonItem

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RootContent() {

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
            router.popUp()
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

    val navigationButtons = createNavigationButtons(router)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
    ) {
        Text(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 8.dp,
            ),
            text = "Stack: $backStackString",
            maxLines = 1,
            overflow = TextOverflow.MiddleEllipsis,
        )

        Text(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 8.dp,
            ),
            text = "Nav3 container",
        )

        Nav3Host(
            backStack = backStack,
            router = router,
        ) { backStack, onBack, router ->
            NavDisplay(
                modifier = Modifier
                    .weight(1f)
                    .border(2.dp, Color.Red),
                backStack = backStack,
                sceneStrategy = DialogSceneStrategy(),
                onBack = {
                    index--
                    onBack(it)
                },
                entryProvider = entryProvider {
                    entry<Screen.Simple> { screen ->
                        SimpleScreen(
                            index = screen.index,
                        )
                    }

                    entry<Screen.BottomSheet> {
                        BottomSheetScreen()
                    }

                    entry<Screen.Dialog>(
                        metadata = DialogSceneStrategy.dialog(),
                    ) {
                        DialogScreen(
                            onClick = {
                                router.popUp()
                            },
                        )
                    }
                },
            )
        }

        ButtonsGrid(stackManipulationButtons)
        HorizontalDivider()
        ButtonsGrid(navigationButtons)
    }
}

private data class ButtonInfo(
    val title: String,
    val onClick: () -> Unit,
)

@Composable
private fun ButtonsGrid(
    buttons: List<ButtonInfo>
) {
    LazyVerticalGrid(
        modifier = Modifier,
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        contentPadding = PaddingValues(16.dp),
    ) {
        buttons.forEach { button ->
            navigationButtonItem(button)
        }
    }
}

private fun createNavigationButtons(
    router: Router<Screen>,
): List<ButtonInfo> {
    return listOf(
        ButtonInfo(
            title = "show bottom sheet",
            onClick = {
                router.push(Screen.BottomSheet)
            },
        ),
        ButtonInfo(
            title = "show dialog",
            onClick = {
                router.push(Screen.Dialog)
            },
        ),
    )
}

private fun createStackManipulationButtons(
    onPop: () -> Unit,
    onPush: () -> Unit,
    onReplace: () -> Unit,
    onNewChain: () -> Unit,
    onReplaceStack: () -> Unit,
    onClearStack: () -> Unit,
    onDropStack: () -> Unit,
): List<ButtonInfo> {
    return listOf(
        ButtonInfo(
            title = "pop",
            onClick = onPop,
        ),
        ButtonInfo(
            title = "push",
            onClick = onPush,
        ),
        ButtonInfo(
            title = "replace",
            onClick = onReplace,
        ),
        ButtonInfo(
            title = "push 3 screens",
            onClick = onNewChain,
        ),
        ButtonInfo(
            title = "replace stack",
            onClick = onReplaceStack,
        ),
        ButtonInfo(
            title = "clear stack",
            onClick = onClearStack,
        ),
        ButtonInfo(
            title = "drop stack",
            onClick = onDropStack,
        ),
    )
}

private fun LazyGridScope.navigationButtonItem(
    info: ButtonInfo,
) {
    item {
        Button(
            onClick = info.onClick,
        ) {
            Text(
                text = info.title,
            )
        }
    }
}