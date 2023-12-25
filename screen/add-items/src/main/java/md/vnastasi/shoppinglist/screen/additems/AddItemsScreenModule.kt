package md.vnastasi.shoppinglist.screen.additems

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

object AddItemsScreenModule {

    operator fun invoke() = module {
        factoryOf(AddItemsViewModel::Factory)
    }
}
