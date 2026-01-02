package md.vnastasi.shoppinglist.screen.shared.content

import androidx.compose.runtime.compositionLocalOf

enum class PresentationMode {

    Dialog, FullScreen
}

val LocalPresentationMode = compositionLocalOf { PresentationMode.FullScreen }
