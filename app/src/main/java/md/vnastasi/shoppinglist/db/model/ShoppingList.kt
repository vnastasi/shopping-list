package md.vnastasi.shoppinglist.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_lists")
data class ShoppingList(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "name")
    val name: String
)
