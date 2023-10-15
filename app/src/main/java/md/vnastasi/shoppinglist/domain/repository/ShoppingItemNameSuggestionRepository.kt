package md.vnastasi.shoppinglist.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import md.vnastasi.shoppinglist.db.dao.ShoppingItemNameSuggestionDao
import md.vnastasi.shoppinglist.db.model.ShoppingItemNameSuggestion

class ShoppingItemNameSuggestionRepository(
    private val shoppingItemNameSuggestionDao: ShoppingItemNameSuggestionDao
) {

    fun findAllMatching(searchTerm: String): Flow<List<String>> =  when {
        searchTerm.isEmpty() -> flowOf(emptyList())
        searchTerm.length < 3 -> flowOf(listOf(searchTerm))
        else -> combine(flowOf(listOf(searchTerm)), shoppingItemNameSuggestionDao.findAll(searchTerm)) { left, right -> left + right }
    }

    suspend fun create(value: String) {
        shoppingItemNameSuggestionDao.create(ShoppingItemNameSuggestion(value = value))
    }
}
