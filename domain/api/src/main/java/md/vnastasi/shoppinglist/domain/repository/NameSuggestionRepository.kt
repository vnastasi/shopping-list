package md.vnastasi.shoppinglist.domain.repository

import md.vnastasi.shoppinglist.domain.model.NameSuggestion

interface NameSuggestionRepository {

    suspend fun findAllMatching(searchTerm: String): List<NameSuggestion>

    suspend fun delete(suggestion: NameSuggestion)
}
