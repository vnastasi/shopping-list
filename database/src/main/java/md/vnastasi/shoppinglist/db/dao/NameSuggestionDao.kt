package md.vnastasi.shoppinglist.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import md.vnastasi.shoppinglist.db.model.NameSuggestion

@Dao
interface NameSuggestionDao {

    @Query("SELECT * FROM name_suggestions WHERE value LIKE '%' || :searchTerm || '%' ORDER BY id DESC")
    suspend fun findAll(searchTerm: String): List<NameSuggestion>

    @Delete
    suspend fun delete(suggestion: NameSuggestion)
}
