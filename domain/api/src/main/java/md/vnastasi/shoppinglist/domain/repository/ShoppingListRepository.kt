package md.vnastasi.shoppinglist.domain.repository

import kotlinx.coroutines.flow.Flow
import md.vnastasi.shoppinglist.domain.model.ShoppingList
import md.vnastasi.shoppinglist.domain.model.ShoppingListDetails

interface ShoppingListRepository {

    fun findAll(): Flow<List<ShoppingListDetails>>

    fun findById(id: Long): Flow<ShoppingList>

    suspend fun create(shoppingList: ShoppingList)

    suspend fun update(shoppingList: ShoppingList)

    suspend fun delete(shoppingList: ShoppingList)
}
