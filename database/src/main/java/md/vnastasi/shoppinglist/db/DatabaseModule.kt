package md.vnastasi.shoppinglist.db

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import md.vnastasi.shoppinglist.db.callback.CreateAutoPositionShoppingListTriggerCallback
import md.vnastasi.shoppinglist.db.callback.CreateNameSuggestionTriggerCallback
import md.vnastasi.shoppinglist.db.dao.NameSuggestionDao
import md.vnastasi.shoppinglist.db.dao.ShoppingItemDao
import md.vnastasi.shoppinglist.db.dao.ShoppingListDao
import md.vnastasi.shoppinglist.db.migration.MigrationFrom1To2
import md.vnastasi.shoppinglist.db.migration.MigrationFrom2To3
import md.vnastasi.shoppinglist.db.migration.MigrationFrom3To4
import md.vnastasi.shoppinglist.db.migration.MigrationFrom4To5
import md.vnastasi.shoppinglist.db.migration.MigrationFrom5To6
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ShoppingListDatabase =
        Room.databaseBuilder(context, ShoppingListDatabase::class.java, ShoppingListDatabase.DB_NAME)
            .addMigrations(
                MigrationFrom1To2(),
                MigrationFrom2To3(),
                MigrationFrom3To4(),
                MigrationFrom4To5(),
                MigrationFrom5To6()
            )
            .fallbackToDestructiveMigrationOnDowngrade(dropAllTables = true)
            .addCallback(CreateNameSuggestionTriggerCallback())
            .addCallback(CreateAutoPositionShoppingListTriggerCallback())
            .build()

    @Provides
    @Singleton
    fun provideShoppingListDao(database: ShoppingListDatabase): ShoppingListDao =
        database.shoppingListDao()

    @Provides
    @Singleton
    fun provideShoppingItemDao(database: ShoppingListDatabase): ShoppingItemDao =
        database.shoppingItemDao()

    @Provides
    @Singleton
    fun provideNameSuggestionDao(database: ShoppingListDatabase): NameSuggestionDao =
        database.shoppingItemNameSuggestionDao()
}
