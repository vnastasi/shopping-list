package md.vnastasi.shoppinglist.domain

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import md.vnastasi.shoppinglist.db.dao.NameSuggestionDao
import md.vnastasi.shoppinglist.db.dao.ShoppingItemDao
import md.vnastasi.shoppinglist.db.dao.ShoppingListDao
import md.vnastasi.shoppinglist.domain.repository.LocalNameSuggestionRepository
import md.vnastasi.shoppinglist.domain.repository.LocalShoppingItemRepository
import md.vnastasi.shoppinglist.domain.repository.LocalShoppingListRepository
import md.vnastasi.shoppinglist.domain.repository.NameSuggestionRepository
import md.vnastasi.shoppinglist.domain.repository.ShoppingItemRepository
import md.vnastasi.shoppinglist.domain.repository.ShoppingListRepository

@Module
@InstallIn(SingletonComponent::class)
class DomainModule {

    @Provides
    fun providesShoppingListRepository(shoppingListDao: ShoppingListDao): ShoppingListRepository =
        LocalShoppingListRepository(shoppingListDao)

    @Provides
    fun providesShoppingItemRepository(shoppingListDao: ShoppingListDao, shoppingItemDao: ShoppingItemDao): ShoppingItemRepository =
        LocalShoppingItemRepository(shoppingListDao, shoppingItemDao)

    @Provides
    fun providesNameSuggestionRepository(nameSuggestionDao: NameSuggestionDao): NameSuggestionRepository =
        LocalNameSuggestionRepository(nameSuggestionDao)
}
