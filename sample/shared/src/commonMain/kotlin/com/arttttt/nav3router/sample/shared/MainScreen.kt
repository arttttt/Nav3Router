package com.arttttt.nav3router.sample.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MainScreen(
    index: Int,
    onPop: () -> Unit,
    onPush: (Int) -> Unit,
    onReplace: (Int) -> Unit,
    onNewChain: (Int) -> Unit,
    onNewRoot: (Int) -> Unit,
    onFinishChain: () -> Unit,
) {
    LazyVerticalGrid(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsPadding()
            .fillMaxSize(),
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        contentPadding = PaddingValues(16.dp),
    ) {
        item(
            span = {
                GridItemSpan(2)
            }
        ) {
            Text(
                text = index.toString()
            )
        }

        navigationButtonItem(
            text = "pop",
            onClick = onPop,
        )

        navigationButtonItem(
            text = "push",
            onClick = {
                onPush(index)
            },
        )

        navigationButtonItem(
            text = "replace",
            onClick = {
                onReplace(index)
            },
        )

        navigationButtonItem(
            text = "new chain",
            onClick = {
                onNewChain(index)
            },
        )

        navigationButtonItem(
            text = "new root",
            onClick = {
                onNewRoot(index)
            },
        )

        navigationButtonItem(
            text = "finish chain",
            onClick = onFinishChain,
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

@Preview
@Composable
private fun MainScreenPreview() {
    MainScreen(
        index = 0,
        onPop = {},
        onPush = {},
        onReplace = {},
        onNewChain = {},
        onNewRoot = {},
        onFinishChain = {},
    )
}