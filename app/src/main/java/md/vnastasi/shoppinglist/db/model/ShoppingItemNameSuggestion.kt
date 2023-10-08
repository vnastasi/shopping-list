package md.vnastasi.shoppinglist.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "shopping_item_name_suggestions",
    indices = [
        Index(
            name = "idx_shopping_item_name_suggestions_value",
            value = ["value"],
            unique = true
        )
    ]
)
data class ShoppingItemNameSuggestion(
    @PrimaryKey
    val id: Long,

    @ColumnInfo(
        name = "value",
        collate = ColumnInfo.NOCASE
    )
    val value: String
)
