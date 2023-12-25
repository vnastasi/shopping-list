package md.vnastasi.shoppinglist.domain

import md.vnastasi.shoppinglist.domain.model.ShoppingItem
import md.vnastasi.shoppinglist.domain.model.ShoppingList

object TestData {

    const val DEFAULT_SHOPPING_LIST_ID = 1L
    const val DEFAULT_SHOPPING_LIST_NAME = "My list"
    const val DEFAULT_SHOPPING_LIST_ITEM_ID = 10L
    const val DEFAULT_SHOPPING_LIST_ITEM_NAME = "My item"

    fun createShoppingList(block: ShoppingListBuilder.() -> Unit = {}) = ShoppingListBuilder().apply(block).build()

    fun createShoppingItem(block: ShoppingItemBuilder.() -> Unit = {}) = ShoppingItemBuilder().apply(block).build()

    class ShoppingListBuilder(
        var id: Long = DEFAULT_SHOPPING_LIST_ID,
        var name: String = DEFAULT_SHOPPING_LIST_NAME
    ) {

        fun build() = ShoppingList(id, name)
    }

    class ShoppingItemBuilder(
        var id: Long = DEFAULT_SHOPPING_LIST_ITEM_ID,
        var name: String = DEFAULT_SHOPPING_LIST_ITEM_NAME,
        var isChecked: Boolean = false,
        var shoppingList: ShoppingListBuilder.() -> Unit = {}
    ) {

        fun build() = ShoppingItem(
            id = id,
            name = name,
            isChecked = isChecked,
            list = createShoppingList(shoppingList)
        )
    }
}
