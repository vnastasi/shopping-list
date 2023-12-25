package md.vnastasi.shoppinglist.domain.repository

import kotlinx.coroutines.flow.Flow
import md.vnastasi.shoppinglist.domain.model.ShoppingList

interface ShoppingListRepository {

    fun findAll(): Flow<List<ShoppingList>>

    fun findById(id: Long): Flow<ShoppingList>

    suspend fun create(shoppingList: ShoppingList)

    suspend fun update(shoppingList: ShoppingList)

    suspend fun delete(shoppingList: ShoppingList)
}
