package md.vnastasi.shoppinglist.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import md.vnastasi.shoppinglist.db.dao.ShoppingItemDao
import md.vnastasi.shoppinglist.db.dao.ShoppingListDao
import md.vnastasi.shoppinglist.domain.model.ShoppingItem
import md.vnastasi.shoppinglist.domain.model.ShoppingList
import md.vnastasi.shoppinglist.db.model.ShoppingItem as ShoppingItemEntity
import md.vnastasi.shoppinglist.db.model.ShoppingList as ShoppingListEntity

class ShoppingItemRepository(
    private val shoppingListDao: ShoppingListDao,
    private val shoppingItemDao: ShoppingItemDao
) {

    fun getAllItems(shoppingListId: Long): Flow<List<ShoppingItem>> = flow {
        val shoppingList = shoppingListDao.getShoppingListById(shoppingListId).toDomainModel()
        shoppingItemDao.getAllShoppingItems(shoppingListId).map { list ->
            list.map { it.toDomainModel(shoppingList) }
        }.also { emitAll(it) }
    }

    suspend fun create(item: ShoppingItem) {
        shoppingItemDao.addShoppingItem(item.toEntity())
    }

    suspend fun update(item: ShoppingItem) {
        shoppingItemDao.updateShoppingItem(item.toEntity())
    }

    suspend fun delete(item: ShoppingItem) {
        shoppingItemDao.deleteShoppingItem(item.toEntity())
    }

    private fun ShoppingItem.toEntity() = ShoppingItemEntity(id, name, isChecked, list.id)

    private fun ShoppingItemEntity.toDomainModel(list: ShoppingList) = ShoppingItem(id, name, isChecked, list)

    private fun ShoppingList.toEntity() = ShoppingListEntity(id, name)

    private fun ShoppingListEntity.toDomainModel() = ShoppingList(id, name)
}
