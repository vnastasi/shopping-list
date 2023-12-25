package md.vnastasi.shoppinglist.domain.repository

import kotlinx.coroutines.flow.Flow
import md.vnastasi.shoppinglist.domain.model.ShoppingItem

interface ShoppingItemRepository {

    fun findAll(shoppingListId: Long): Flow<List<ShoppingItem>>

    suspend fun create(item: ShoppingItem)

    suspend fun update(item: ShoppingItem)

    suspend fun delete(item: ShoppingItem)
}
