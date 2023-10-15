package md.vnastasi.shoppinglist.screen

import md.vnastasi.shoppinglist.screen.additems.AddItemsViewModel
import md.vnastasi.shoppinglist.screen.listdetails.ListDetailsViewModel
import md.vnastasi.shoppinglist.screen.listoverview.ListOverviewViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

object ScreensModule {

    operator fun invoke() = module {
        factoryOf(ListOverviewViewModel::Factory)
        factoryOf(ListDetailsViewModel::Factory)
        factoryOf(AddItemsViewModel::Factory)
    }
}
