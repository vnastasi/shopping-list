package md.vnastasi.shoppinglist.db

import androidx.room.Database
import androidx.room.RoomDatabase
import md.vnastasi.shoppinglist.db.dao.ShoppingItemDao
import md.vnastasi.shoppinglist.db.dao.ShoppingListDao
import md.vnastasi.shoppinglist.db.model.ShoppingItem
import md.vnastasi.shoppinglist.db.model.ShoppingList

@Database(
    entities = [ShoppingItem::class, ShoppingList::class],
    version = 1,
    exportSchema = true
)
abstract class ShoppingListDatabase : RoomDatabase() {

    abstract fun shoppingListDao(): ShoppingListDao

    abstract fun shoppingItemDao(): ShoppingItemDao
}
