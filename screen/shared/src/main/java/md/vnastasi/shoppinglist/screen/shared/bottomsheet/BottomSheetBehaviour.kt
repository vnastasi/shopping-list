package md.vnastasi.shoppinglist.screen.shared.bottomsheet

import androidx.compose.material3.SheetState
import androidx.compose.runtime.Stable
import kotlinx.coroutines.CoroutineScope

@Stable
data class BottomSheetBehaviour(
    val state: SheetState,
    val scope: CoroutineScope
)
