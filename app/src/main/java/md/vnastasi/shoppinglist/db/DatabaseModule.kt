package md.vnastasi.shoppinglist.db

import androidx.room.Room
import md.vnastasi.shoppinglist.db.dao.ShoppingItemDao
import md.vnastasi.shoppinglist.db.dao.ShoppingListDao
import md.vnastasi.shoppinglist.db.migration.MigrationFrom1To2
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

object DatabaseModule {

    operator fun invoke() = module {
        single<ShoppingListDatabase> {
            Room.databaseBuilder(androidApplication(), ShoppingListDatabase::class.java, ShoppingListDatabase.DB_NAME)
                .addMigrations(MigrationFrom1To2())
                .fallbackToDestructiveMigrationOnDowngrade()
                .build()
        }

        factory<ShoppingListDao> {
            get<ShoppingListDatabase>().shoppingListDao()
        }

        factory<ShoppingItemDao> {
            get<ShoppingListDatabase>().shoppingItemDao()
        }
    }
}
