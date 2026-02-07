package md.vnastasi.shoppinglist.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import md.vnastasi.shoppinglist.db.dao.ShoppingListDao
import md.vnastasi.shoppinglist.domain.model.ShoppingList
import md.vnastasi.shoppinglist.domain.model.ShoppingListDetails
import md.vnastasi.shoppinglist.db.model.ShoppingList as ShoppingListEntity
import md.vnastasi.shoppinglist.db.model.ShoppingListDetails as ShoppingListDetailsView

internal class LocalShoppingListRepository(
    private val shoppingListDao: ShoppingListDao
) : ShoppingListRepository {

    override fun findAll(): Flow<List<ShoppingListDetails>> =
        shoppingListDao.findAll().map { list ->
            list.map { it.toDomainModel() }
        }

    override fun findById(id: Long): Flow<ShoppingList> =
        shoppingListDao.findById(id).filterNotNull().map { it.toDomainModel() }

    override suspend fun create(shoppingList: ShoppingList) {
        shoppingListDao.create(shoppingList.toEntity())
    }

    override suspend fun update(shoppingList: ShoppingList) {
        shoppingListDao.update(shoppingList.toEntity())
    }

    override suspend fun update(shoppingLists: List<ShoppingList>) {
        shoppingListDao.update(shoppingLists.map { it.toEntity() })
    }

    override suspend fun delete(shoppingList: ShoppingList) {
        shoppingListDao.delete(shoppingList.toEntity())
    }

    private fun ShoppingList.toEntity() = ShoppingListEntity(id, name, position)

    private fun ShoppingListEntity.toDomainModel() = ShoppingList(id, name, position)

    private fun ShoppingListDetailsView.toDomainModel() = ShoppingListDetails(id, name, position, totalItems, checkedItems)
}
