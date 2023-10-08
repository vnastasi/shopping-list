package md.vnastasi.shoppinglist.db

import androidx.room.Database
import androidx.room.RoomDatabase
import md.vnastasi.shoppinglist.db.dao.ShoppingItemDao
import md.vnastasi.shoppinglist.db.dao.ShoppingItemNameSuggestionDao
import md.vnastasi.shoppinglist.db.dao.ShoppingListDao
import md.vnastasi.shoppinglist.db.model.ShoppingItem
import md.vnastasi.shoppinglist.db.model.ShoppingItemNameSuggestion
import md.vnastasi.shoppinglist.db.model.ShoppingList

@Database(
    entities = [ShoppingItem::class, ShoppingList::class, ShoppingItemNameSuggestion::class],
    version = 2,
    exportSchema = true
)
abstract class ShoppingListDatabase : RoomDatabase() {

    abstract fun shoppingListDao(): ShoppingListDao

    abstract fun shoppingItemDao(): ShoppingItemDao

    abstract fun shoppingItemNameSuggestionDao(): ShoppingItemNameSuggestionDao

    companion object {

        const val DB_NAME = "shopping_list_db"
    }
}
