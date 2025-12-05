package md.vnastasi.shoppinglist.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import md.vnastasi.shoppinglist.db.dao.NameSuggestionDao
import md.vnastasi.shoppinglist.domain.model.NameSuggestion
import md.vnastasi.shoppinglist.db.model.NameSuggestion as NameSuggestionEntity

private const val MIN_CHARACTER_THRESHOLD = 3

internal class LocalNameSuggestionRepository(
    private val nameSuggestionDao: NameSuggestionDao
) : NameSuggestionRepository {

    override fun findAllMatching(searchTerm: String): Flow<List<NameSuggestion>> =
        when {
            searchTerm.isEmpty() -> flowOf(emptyList())
            searchTerm.length < MIN_CHARACTER_THRESHOLD -> flowOf(listOf(NameSuggestion(-1L, searchTerm)))
            else -> nameSuggestionDao.findAll(searchTerm).map { originalList ->
                buildList {
                    add(NameSuggestion(-1L, searchTerm))
                    addAll(originalList.map { it.toDomainModel() })
                }
            }
        }

    override suspend fun delete(suggestion: NameSuggestion) {
        nameSuggestionDao.delete(suggestion.toEntity())
    }

    private fun NameSuggestionEntity.toDomainModel() = NameSuggestion(id, value)

    private fun NameSuggestion.toEntity() = NameSuggestionEntity(id, name)
}
