package md.vnastasi.shoppinglist.db.model

import androidx.room.DatabaseView

@DatabaseView(
    viewName = "shopping_list_details",
    value = "SELECT "
            + "list.id AS id, "
            + "list.name AS name, "
            + "list.position AS position, "
            + "(SELECT count(*) FROM shopping_items item WHERE item.list_id = list.id) AS totalItems, "
            + "(SELECT count(*) FROM shopping_items item WHERE item.list_id = list.id AND item.is_checked = 1) AS checkedItems "
        + "FROM shopping_lists list "
        + "ORDER BY list.position ASC"
)
data class ShoppingListDetails(
    val id: Long,
    val name: String,
    val position: Long,
    val totalItems: Long,
    val checkedItems: Long
)
