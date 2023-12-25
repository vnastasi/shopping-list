package md.vnastasi.shoppinglist.domain

import md.vnastasi.shoppinglist.domain.repository.LocalNameSuggestionRepository
import md.vnastasi.shoppinglist.domain.repository.LocalShoppingItemRepository
import md.vnastasi.shoppinglist.domain.repository.LocalShoppingListRepository
import md.vnastasi.shoppinglist.domain.repository.NameSuggestionRepository
import md.vnastasi.shoppinglist.domain.repository.ShoppingItemRepository
import md.vnastasi.shoppinglist.domain.repository.ShoppingListRepository
import org.koin.dsl.module

object DomainModule {

    operator fun invoke() = module {

        factory<ShoppingListRepository> {
            LocalShoppingListRepository(get())
        }

        factory<ShoppingItemRepository> {
            LocalShoppingItemRepository(get(), get())
        }

        factory<NameSuggestionRepository> {
            LocalNameSuggestionRepository(get())
        }
    }
}
