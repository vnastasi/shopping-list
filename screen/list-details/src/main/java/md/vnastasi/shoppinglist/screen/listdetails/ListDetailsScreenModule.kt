package md.vnastasi.shoppinglist.screen.listdetails

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

object ListDetailsScreenModule {

    operator fun invoke() = module {
        factoryOf(ListDetailsViewModel::Factory)
    }
}
