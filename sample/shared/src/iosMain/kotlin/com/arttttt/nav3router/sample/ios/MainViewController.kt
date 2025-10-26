package com.arttttt.nav3router.sample.ios

import androidx.compose.ui.window.ComposeUIViewController
import com.arttttt.nav3router.sample.shared.RootContent
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    return ComposeUIViewController(
        configure = { enforceStrictPlistSanityCheck = false }
    ) {
        RootContent()
    }
}