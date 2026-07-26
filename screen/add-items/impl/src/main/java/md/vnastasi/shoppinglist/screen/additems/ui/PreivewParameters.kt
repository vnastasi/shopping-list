package md.vnastasi.shoppinglist.screen.additems.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import md.vnastasi.shoppinglist.domain.model.NameSuggestion

private val previewSuggestion = NameSuggestion(id = 4L, name = "Powdered milk")

private val previewSuggestions = listOf(
    NameSuggestion(id = -1L, name = "Mil"),
    NameSuggestion(id = 1L, name = "Milk"),
    NameSuggestion(id = 2L, name = "Oat milk"),
    NameSuggestion(id = 3L, name = "Milkshake"),
    previewSuggestion,
    NameSuggestion(id = 5L, name = "Milk chocolate"),
)

internal class SuggestionsParameterProvider : PreviewParameterProvider<NameSuggestion> {

    override val values: Sequence<NameSuggestion> = sequenceOf(previewSuggestion)
}

internal class ListOfSuggestionsParameterProvider : PreviewParameterProvider<ImmutableList<NameSuggestion>> {

    override val values: Sequence<ImmutableList<NameSuggestion>> = sequenceOf(previewSuggestions.toImmutableList())
}
