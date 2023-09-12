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

    @Query("SELECT * FROM shopping_lists")
    fun getShoppingLists(): Flow<List<ShoppingList>>

    @Query("SELECT * FROM shopping_lists WHERE id = :id LIMIT 1")
    fun getShoppingListById(id: Long): Flow<ShoppingList>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addShoppingList(list: ShoppingList)

    @Update(onConflict = OnConflictStrategy.ABORT)
    suspend fun updateShoppingList(list: ShoppingList)

    @Delete
    suspend fun deleteShoppingList(list: ShoppingList)
}
