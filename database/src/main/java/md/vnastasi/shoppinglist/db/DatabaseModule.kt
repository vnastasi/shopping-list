package md.vnastasi.shoppinglist.db

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import md.vnastasi.shoppinglist.db.callback.CreateNameSuggestionTriggerCallback
import md.vnastasi.shoppinglist.db.dao.NameSuggestionDao
import md.vnastasi.shoppinglist.db.dao.ShoppingItemDao
import md.vnastasi.shoppinglist.db.dao.ShoppingListDao
import md.vnastasi.shoppinglist.db.migration.MigrationFrom1To2
import md.vnastasi.shoppinglist.db.migration.MigrationFrom2To3
import md.vnastasi.shoppinglist.db.migration.MigrationFrom3To4
import md.vnastasi.shoppinglist.db.migration.MigrationFrom4To5
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): ShoppingListDatabase =
        Room.databaseBuilder(context, ShoppingListDatabase::class.java, ShoppingListDatabase.DB_NAME)
            .addMigrations(MigrationFrom1To2(), MigrationFrom2To3(), MigrationFrom3To4(), MigrationFrom4To5())
            .fallbackToDestructiveMigrationOnDowngrade(dropAllTables = true)
            .addCallback(CreateNameSuggestionTriggerCallback())
            .build()

    @Provides
    fun provideShoppingListDao(database: ShoppingListDatabase): ShoppingListDao =
        database.shoppingListDao()

    @Provides
    fun provideShoppingItemDao(database: ShoppingListDatabase): ShoppingItemDao =
        database.shoppingItemDao()

    @Provides
    fun provideNameSuggestionDao(database: ShoppingListDatabase): NameSuggestionDao =
        database.shoppingItemNameSuggestionDao()
}
