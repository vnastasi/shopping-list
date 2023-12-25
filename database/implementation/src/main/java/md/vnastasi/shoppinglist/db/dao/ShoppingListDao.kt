package md.vnastasi.shoppinglist.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import md.vnastasi.shoppinglist.db.model.ShoppingList

@Dao
interface ShoppingListDao {

    @Query("SELECT * FROM shopping_lists ORDER BY id DESC")
    fun findAll(): Flow<List<ShoppingList>>

    @Query("SELECT * FROM shopping_lists WHERE id = :id LIMIT 1")
    fun findById(id: Long): Flow<ShoppingList>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun create(list: ShoppingList)

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun update(list: ShoppingList)

    @Delete
    suspend fun delete(list: ShoppingList)
}
