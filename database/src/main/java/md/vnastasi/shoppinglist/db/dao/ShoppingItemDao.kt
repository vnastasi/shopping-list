package md.vnastasi.shoppinglist.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import md.vnastasi.shoppinglist.db.model.ShoppingItem

@Dao
abstract class ShoppingItemDao {

    @Query("SELECT * FROM shopping_items WHERE list_id = :listId ORDER BY id DESC, is_checked DESC")
    abstract fun findAll(listId: Long): Flow<List<ShoppingItem>>

    @Query("SELECT * FROM shopping_items WHERE id = :id LIMIT 1")
    abstract fun findById(id: Long): Flow<ShoppingItem>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract suspend fun create(item: ShoppingItem): Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract suspend fun createAll(items: List<ShoppingItem>): List<Long>

    @Update(onConflict = OnConflictStrategy.ABORT)
    abstract suspend fun update(item: ShoppingItem)

    @Delete
    abstract suspend fun delete(item: ShoppingItem)

    @Query("DELETE FROM shopping_items WHERE list_id = :listId")
    abstract suspend fun deleteByListId(listId: Long)

    @Transaction
    open suspend fun reorder(items: List<ShoppingItem>) {
        require(items.isNotEmpty())
        deleteByListId(items.first().listId)
        createAll(items)
    }
}
