package md.vnastasi.shoppinglist.screen.shared.sheet

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.runtime.Composable

@Composable
fun PreviewableSheetLayout(
    content: @Composable ColumnScope.() -> Unit
) {
    val sheetState = SheetState(
        skipPartiallyExpanded = true,
        skipHiddenState = true,
        initialValue = SheetValue.Expanded,
        confirmValueChange = { true },
        positionalThreshold = { 1.0f },
        velocityThreshold = { 1.0f }
    )

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = { },
        content = content
    )
}
