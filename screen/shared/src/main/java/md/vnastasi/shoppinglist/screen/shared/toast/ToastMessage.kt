package md.vnastasi.shoppinglist.screen.shared.toast

import androidx.annotation.IntDef
import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Stable
data class ToastMessage(
    @StringRes val textResourceId: Int,
    val arguments: ImmutableList<Any> = persistentListOf(),
    @ToastDuration val duration: Int = DURATION_SHORT
) {

    companion object {

        const val DURATION_SHORT = android.widget.Toast.LENGTH_SHORT
        const val DURATION_LONG = android.widget.Toast.LENGTH_LONG
    }

    @IntDef(
        flag = false,
        value = [DURATION_SHORT, DURATION_LONG]
    )
    private annotation class ToastDuration
}
