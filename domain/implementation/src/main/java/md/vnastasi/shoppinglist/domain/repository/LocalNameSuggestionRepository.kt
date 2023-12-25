package md.vnastasi.shoppinglist.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import md.vnastasi.shoppinglist.db.dao.NameSuggestionDao
import md.vnastasi.shoppinglist.domain.model.NameSuggestion
import md.vnastasi.shoppinglist.db.model.NameSuggestion as NameSuggestionEntity

internal class LocalNameSuggestionRepository(
    private val nameSuggestionDao: NameSuggestionDao
) : NameSuggestionRepository {

    override fun findAllMatching(searchTerm: String): Flow<List<NameSuggestion>> = when {
        searchTerm.isEmpty() -> flowOf(emptyList())
        searchTerm.length < 3 -> flowOf(listOf(NameSuggestion(-1L, searchTerm)))
        else -> combine(searchTerm)
    }

    override suspend fun create(value: String) {
        nameSuggestionDao.create(NameSuggestionEntity(value = value))
    }

    override suspend fun delete(suggestion: NameSuggestion) {
        nameSuggestionDao.delete(suggestion.toEntity())
    }

    private fun combine(searchTerm: String): Flow<List<NameSuggestion>> = combine(
        flowOf(listOf(NameSuggestion(-1L, searchTerm))),
        nameSuggestionDao.findAll(searchTerm).map { list -> list.map { it.toDomainModel() } },
        transform = { left, right -> left + right }
    )

    private fun NameSuggestionEntity.toDomainModel() = NameSuggestion(id, value)

    private fun NameSuggestion.toEntity() = NameSuggestionEntity(id, name)
}
