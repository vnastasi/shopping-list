package md.vnastasi.shoppinglist.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import md.vnastasi.shoppinglist.db.dao.ShoppingListDao
import md.vnastasi.shoppinglist.domain.model.ShoppingList
import md.vnastasi.shoppinglist.db.model.ShoppingList as ShoppingListEntity

class ShoppingListRepository(
    private val shoppingListDao: ShoppingListDao
) {

    fun findAll(): Flow<List<ShoppingList>> =
        shoppingListDao.findAll().map { list ->
            list.map { it.toDomainModel() }
        }

    fun findById(id: Long): Flow<ShoppingList> =
        shoppingListDao.findById(id).filterNotNull().map { it.toDomainModel() }

    suspend fun create(shoppingList: ShoppingList) {
        shoppingListDao.create(shoppingList.toEntity())
    }

    suspend fun update(shoppingList: ShoppingList) {
        shoppingListDao.update(shoppingList.toEntity())
    }

    suspend fun delete(shoppingList: ShoppingList) {
        shoppingListDao.delete(shoppingList.toEntity())
    }

    private fun ShoppingList.toEntity() = ShoppingListEntity(id, name)

    private fun ShoppingListEntity.toDomainModel() = ShoppingList(id, name)
}
