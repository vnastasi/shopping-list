package md.vnastasi.shoppinglist.screen.additems

import md.vnastasi.shoppinglist.screen.additems.vm.AddItemsViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

object AddItemsScreenModule {

    operator fun invoke() = module {
        factoryOf(AddItemsViewModel::Factory)
    }
}
