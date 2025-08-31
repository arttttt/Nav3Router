package com.arttttt.nav3router.sample.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arttttt.nav3router.Router

data class ButtonInfo(
    val title: String,
    val onClick: () -> Unit,
)

@Composable
fun ButtonsGrid(
    buttons: List<ButtonInfo>
) {
    LazyVerticalGrid(
        modifier = Modifier,
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.CenterVertically),
        contentPadding = PaddingValues(16.dp),
    ) {
        buttons.forEach { button ->
            navigationButtonItem(button)
        }
    }
}

fun createNavigationButtons(
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
        ButtonInfo(
            title = "nested container",
            onClick = {
                router.push(Screen.NestedContainer())
            },
        ),
    )
}

fun createStackManipulationButtons(
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

fun LazyGridScope.navigationButtonItem(
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