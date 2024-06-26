package md.vnastasi.shoppinglist.db

import md.vnastasi.shoppinglist.db.model.ShoppingItem
import md.vnastasi.shoppinglist.db.model.ShoppingList
import md.vnastasi.shoppinglist.db.model.ShoppingListDetails

object TestData {

    const val DEFAULT_SHOPPING_LIST_ID = 1L
    const val DEFAULT_SHOPPING_LIST_NAME = "My list"
    const val DEFAULT_SHOPPING_LIST_ITEM_ID = 10L
    const val DEFAULT_SHOPPING_LIST_ITEM_NAME = "My item"

    fun createShoppingListEntity(block: ShoppingListEntityBuilder.() -> Unit = {}) = ShoppingListEntityBuilder().apply(block).build()

    fun createShoppingListDetailsView(block: ShoppingListDetailsViewBuilder.() -> Unit = {}) = ShoppingListDetailsViewBuilder().apply(block).build()

    fun createShoppingItemEntity(block: ShoppingItemEntityBuilder.() -> Unit = {}) = ShoppingItemEntityBuilder().apply(block).build()

    class ShoppingListEntityBuilder(
        var id: Long = DEFAULT_SHOPPING_LIST_ID,
        var name: String = DEFAULT_SHOPPING_LIST_NAME
    ) {

        fun build() = ShoppingList(id, name)
    }

    class ShoppingListDetailsViewBuilder(
        var id: Long = DEFAULT_SHOPPING_LIST_ID,
        var name: String = DEFAULT_SHOPPING_LIST_NAME,
        var totalItems: Long = 0L,
        var checkedItems: Long = 0L,
    ) {

        fun build() = ShoppingListDetails(id, name, totalItems, checkedItems)
    }

    class ShoppingItemEntityBuilder(
        var id: Long = DEFAULT_SHOPPING_LIST_ITEM_ID,
        var name: String = DEFAULT_SHOPPING_LIST_ITEM_NAME,
        var isChecked: Boolean = false,
        var listId: Long = DEFAULT_SHOPPING_LIST_ID
    ) {

        fun build() = ShoppingItem(
            id = id,
            name = name,
            isChecked = isChecked,
            listId = listId
        )
    }
}
