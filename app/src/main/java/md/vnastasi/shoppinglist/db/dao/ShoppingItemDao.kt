package md.vnastasi.shoppinglist.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import md.vnastasi.shoppinglist.db.model.ShoppingItem

@Dao
interface ShoppingItemDao {

    @Query("SELECT * FROM shopping_items WHERE list_id = :listId ORDER BY id DESC, is_checked DESC")
    fun getAllShoppingItems(listId: Long): Flow<List<ShoppingItem>>

    @Query("SELECT * FROM shopping_items WHERE id = :id LIMIT 1")
    suspend fun getShoppingItemById(id: Long): ShoppingItem

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addShoppingItem(item: ShoppingItem)

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateShoppingItem(item: ShoppingItem)

    @Delete
    suspend fun deleteShoppingItem(item: ShoppingItem)
}