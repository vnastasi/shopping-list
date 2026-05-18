package md.vnastasi.shoppinglist.screen.additems.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import md.vnastasi.shoppinglist.domain.model.NameSuggestion
import md.vnastasi.shoppinglist.screen.shared.toast.ToastMessage

@Immutable
data class ViewState(
    val suggestions: ImmutableList<NameSuggestion>,
    val toastMessage: ToastMessage?
) {

    companion object {

        fun init() = ViewState(suggestions = persistentListOf(), toastMessage = null)
    }
}
