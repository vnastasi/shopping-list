package md.vnastasi.shoppinglist.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import md.vnastasi.shoppinglist.db.model.NameSuggestion

@Dao
interface NameSuggestionDao {

    @Query("SELECT * FROM name_suggestions WHERE value LIKE '%' || :searchTerm || '%' ORDER BY id DESC")
    fun findAll(searchTerm: String): Flow<List<NameSuggestion>>

    @Delete
    suspend fun delete(suggestion: NameSuggestion)
}
