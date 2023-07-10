package md.vnastasi.shoppinglist.db

import androidx.room.Room
import md.vnastasi.shoppinglist.db.dao.ShoppingItemDao
import md.vnastasi.shoppinglist.db.dao.ShoppingListDao
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

object DatabaseModule {

    operator fun invoke() = module {
        single<ShoppingListDatabase> {
            Room.databaseBuilder(androidApplication(), ShoppingListDatabase::class.java, "shopping_list_db").build()
        }

        factory<ShoppingListDao> {
            get<ShoppingListDatabase>().shoppingListDao()
        }

        factory<ShoppingItemDao> {
            get<ShoppingListDatabase>().shoppingItemDao()
        }
    }
}
