package md.vnastasi.shoppinglist.db

import androidx.room.Database
import androidx.room.RoomDatabase
import md.vnastasi.shoppinglist.db.dao.ShoppingItemDao
import md.vnastasi.shoppinglist.db.dao.NameSuggestionDao
import md.vnastasi.shoppinglist.db.dao.ShoppingListDao
import md.vnastasi.shoppinglist.db.model.ShoppingItem
import md.vnastasi.shoppinglist.db.model.NameSuggestion
import md.vnastasi.shoppinglist.db.model.ShoppingList

@Database(
    entities = [ShoppingItem::class, ShoppingList::class, NameSuggestion::class],
    version = 3,
    exportSchema = true
)
abstract class ShoppingListDatabase : RoomDatabase() {

    abstract fun shoppingListDao(): ShoppingListDao

    abstract fun shoppingItemDao(): ShoppingItemDao

    abstract fun shoppingItemNameSuggestionDao(): NameSuggestionDao

    companion object {

        const val DB_NAME = "shopping_list_db"
    }
}
