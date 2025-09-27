package md.vnastasi.shoppinglist.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import md.vnastasi.shoppinglist.db.dao.ShoppingItemDao
import md.vnastasi.shoppinglist.db.dao.ShoppingListDao
import md.vnastasi.shoppinglist.domain.model.ShoppingItem
import md.vnastasi.shoppinglist.domain.model.ShoppingList
import md.vnastasi.shoppinglist.db.model.ShoppingItem as ShoppingItemEntity
import md.vnastasi.shoppinglist.db.model.ShoppingList as ShoppingListEntity

internal class LocalShoppingItemRepository(
    private val shoppingListDao: ShoppingListDao,
    private val shoppingItemDao: ShoppingItemDao
) : ShoppingItemRepository {

    override fun findAll(shoppingListId: Long): Flow<List<ShoppingItem>> = combine(
        shoppingListDao.findById(shoppingListId).filterNotNull(),
        shoppingItemDao.findAll(shoppingListId)
    ) { shoppingList: ShoppingListEntity, itemList: List<ShoppingItemEntity> ->
        itemList.map { it.toDomainModel(shoppingList.toDomainModel()) }
    }

    override suspend fun create(item: ShoppingItem) {
        shoppingItemDao.create(item.toEntity())
    }

    override suspend fun update(item: ShoppingItem) {
        shoppingItemDao.update(item.toEntity())
    }

    override suspend fun delete(item: ShoppingItem) {
        shoppingItemDao.delete(item.toEntity())
    }

    override suspend fun reorder(items: List<ShoppingItem>) {
        require(items.isNotEmpty())
        shoppingItemDao.reorder(items.reversed().map { it.toEntity().copy(id = 0L) })
    }

    private fun ShoppingItem.toEntity() = ShoppingItemEntity(id, name, isChecked, list.id)

    private fun ShoppingItemEntity.toDomainModel(list: ShoppingList) = ShoppingItem(id, name, isChecked, list)
    
    private fun ShoppingListEntity.toDomainModel() = ShoppingList(id, name)
}
