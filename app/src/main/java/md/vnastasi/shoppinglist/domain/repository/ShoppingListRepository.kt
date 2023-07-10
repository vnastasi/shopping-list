package md.vnastasi.shoppinglist.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import md.vnastasi.shoppinglist.db.dao.ShoppingListDao
import md.vnastasi.shoppinglist.domain.model.ShoppingList
import md.vnastasi.shoppinglist.db.model.ShoppingList as ShoppingListEntity

class ShoppingListRepository(
    private val shoppingListDao: ShoppingListDao
) {

    fun getAvailableLists(): Flow<List<ShoppingList>> =
        shoppingListDao.getShoppingLists().map { list ->
            list.map { it.toDomainModel() }
        }

    suspend fun create(shoppingList: ShoppingList) {
        shoppingListDao.addShoppingList(shoppingList.toEntity())
    }

    suspend fun update(shoppingList: ShoppingList) {
        shoppingListDao.updateShoppingList(shoppingList.toEntity())
    }

    suspend fun delete(shoppingList: ShoppingList) {
        shoppingListDao.deleteShoppingList(shoppingList.toEntity())
    }

    private fun ShoppingList.toEntity() = ShoppingListEntity(id, name)

    private fun ShoppingListEntity.toDomainModel() = ShoppingList(id, name)
}
