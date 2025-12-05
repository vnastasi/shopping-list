package md.vnastasi.shoppinglist.domain.repository

import kotlinx.coroutines.flow.Flow
import md.vnastasi.shoppinglist.domain.model.NameSuggestion

interface NameSuggestionRepository {

    fun findAllMatching(searchTerm: String): Flow<List<NameSuggestion>>

    suspend fun delete(suggestion: NameSuggestion)
}
