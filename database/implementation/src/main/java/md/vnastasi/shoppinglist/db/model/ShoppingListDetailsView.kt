package md.vnastasi.shoppinglist.db.model

import androidx.room.DatabaseView

@DatabaseView(
    viewName = "shopping_list_details",
    value = "SELECT "
            + "list.id AS id, "
            + "list.name AS name, "
            + "(SELECT count(*) FROM shopping_items item WHERE item.list_id = list.id) AS totalItems, "
            + "(SELECT count(*) FROM shopping_items item WHERE item.list_id = list.id AND item.is_checked = 1) AS checkedItems "
        + "FROM shopping_lists list"
)
data class ShoppingListDetailsView(
    val id: Long,
    val name: String,
    val totalItems: Long,
    val checkedItems: Long
)
