package md.vnastasi.shoppinglist.screen

import md.vnastasi.shoppinglist.screen.main.MainViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

object UiModule {

    operator fun invoke() = module {
        factoryOf(MainViewModel::Factory)
    }
}
