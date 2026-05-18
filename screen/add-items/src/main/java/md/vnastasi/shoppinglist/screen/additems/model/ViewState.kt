package md.vnastasi.shoppinglist.screen.additems.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import md.vnastasi.shoppinglist.domain.model.NameSuggestion

@Immutable
data class ViewState(
    val suggestions: ImmutableList<NameSuggestion>
) {

    companion object {

        val INIT = ViewState(suggestions = persistentListOf())
    }
}
