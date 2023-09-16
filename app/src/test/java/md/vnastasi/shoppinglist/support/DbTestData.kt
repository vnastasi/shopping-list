package md.vnastasi.shoppinglist.support

import md.vnastasi.shoppinglist.db.model.ShoppingItem
import md.vnastasi.shoppinglist.db.model.ShoppingList

object DbTestData {

    const val DEFAULT_SHOPPING_LIST_ID = 1L
    const val DEFAULT_SHOPPING_LIST_NAME = "My list"
    const val DEFAULT_SHOPPING_LIST_ITEM_ID = 10L
    const val DEFAULT_SHOPPING_LIST_ITEM_NAME = "My item"

    fun createShoppingListEntity(block: ShoppingListEntityBuilder.() -> Unit = {}) = ShoppingListEntityBuilder().apply(block).build()

    fun createShoppingItemEntity(block: ShoppingItemEntityBuilder.() -> Unit = {}) = ShoppingItemEntityBuilder().apply(block).build()

    class ShoppingListEntityBuilder(
        var id: Long = DEFAULT_SHOPPING_LIST_ID,
        var name: String = DEFAULT_SHOPPING_LIST_NAME
    ) {

        fun build() = ShoppingList(id, name)
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
