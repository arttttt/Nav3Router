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
import com.arttttt.nav3router.rememberRouter

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
                },
            )
        }

        ButtonsGrid(
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
    }
}

@Composable
private fun ButtonsGrid(
    onPop: () -> Unit,
    onPush: () -> Unit,
    onReplace: () -> Unit,
    onNewChain: () -> Unit,
    onReplaceStack: () -> Unit,
    onClearStack: () -> Unit,
    onDropStack: () -> Unit,
) {
    LazyVerticalGrid(
        modifier = Modifier,
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        contentPadding = PaddingValues(16.dp),
    ) {
        navigationButtonItem(
            text = "pop",
            onClick = onPop,
        )

        navigationButtonItem(
            text = "push",
            onClick = onPush,
        )

        navigationButtonItem(
            text = "replace",
            onClick = onReplace,
        )

        navigationButtonItem(
            text = "push 3 screens",
            onClick = onNewChain,
        )

        navigationButtonItem(
            text = "replace stack",
            onClick = onReplaceStack,
        )

        navigationButtonItem(
            text = "clear stack",
            onClick = onClearStack,
        )

        navigationButtonItem(
            text = "drop stack",
            onClick = onDropStack,
        )
    }
}

private fun LazyGridScope.navigationButtonItem(
    text: String,
    onClick: () -> Unit,
) {
    item {
        Button(
            onClick = onClick,
        ) {
            Text(
                text = text
            )
        }
    }
}