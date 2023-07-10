package md.vnastasi.shoppinglist.domain

import md.vnastasi.shoppinglist.domain.repository.ShoppingItemRepository
import md.vnastasi.shoppinglist.domain.repository.ShoppingListRepository
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

object DomainModule {

    operator fun invoke() = module {

        factoryOf(::ShoppingListRepository)

        factoryOf(::ShoppingItemRepository)
    }
}