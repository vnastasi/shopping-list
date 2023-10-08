package md.vnastasi.shoppinglist.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import md.vnastasi.shoppinglist.db.model.ShoppingItemNameSuggestion

@Dao
interface ShoppingItemNameSuggestionDao {

    @Query("SELECT * FROM shopping_item_name_suggestions WHERE value LIKE '%' + :searchTerm + '%' ORDER BY id DESC")
    fun findAll(searchTerm: String): Flow<List<ShoppingItemNameSuggestion>>

    @Delete
    suspend fun delete(suggestion: ShoppingItemNameSuggestion)
}
