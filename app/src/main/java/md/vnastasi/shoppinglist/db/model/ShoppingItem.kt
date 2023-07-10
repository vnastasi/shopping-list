package md.vnastasi.shoppinglist.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_items")
data class ShoppingItem(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "is_checked")
    val isChecked: Boolean,

    @ColumnInfo(name = "list_id")
    val listId: Long
)
