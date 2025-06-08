package md.vnastasi.shoppinglist.domain.repository

import md.vnastasi.shoppinglist.db.dao.NameSuggestionDao
import md.vnastasi.shoppinglist.domain.model.NameSuggestion
import md.vnastasi.shoppinglist.db.model.NameSuggestion as NameSuggestionEntity

private const val MIN_CHARACTER_THRESHOLD = 3

internal class LocalNameSuggestionRepository(
    private val nameSuggestionDao: NameSuggestionDao
) : NameSuggestionRepository {

    override suspend fun findAllMatching(searchTerm: String): List<NameSuggestion> = when {
        searchTerm.isEmpty() -> emptyList()
        searchTerm.length < MIN_CHARACTER_THRESHOLD -> listOf(NameSuggestion(-1L, searchTerm))
        else -> listOf(NameSuggestion(-1L, searchTerm)) + nameSuggestionDao.findAll(searchTerm).map { it.toDomainModel() }
    }

    override suspend fun delete(suggestion: NameSuggestion) {
        nameSuggestionDao.delete(suggestion.toEntity())
    }

    private fun NameSuggestionEntity.toDomainModel() = NameSuggestion(id, value)

    private fun NameSuggestion.toEntity() = NameSuggestionEntity(id, name)
}
