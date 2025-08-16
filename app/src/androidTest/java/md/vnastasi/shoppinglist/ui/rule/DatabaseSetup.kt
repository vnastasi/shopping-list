package md.vnastasi.shoppinglist.ui.rule

import kotlinx.coroutines.runBlocking
import md.vnastasi.shoppinglist.db.ShoppingListDatabase

fun ShoppingListDatabase.onSetUp(block: suspend ShoppingListDatabase.() -> Unit = { }) {
    runBlocking {
        truncate()
        block()
    }
}

fun ShoppingListDatabase.onTearDown(block: suspend ShoppingListDatabase.() -> Unit = { }) {
    runBlocking {
        block()
        truncate()
    }
}

private fun ShoppingListDatabase.truncate() {
    openHelper.writableDatabase.run {
        execSQL("DELETE FROM name_suggestions")
        execSQL("DELETE FROM shopping_items")
        execSQL("DELETE FROM shopping_lists")
    }
}
