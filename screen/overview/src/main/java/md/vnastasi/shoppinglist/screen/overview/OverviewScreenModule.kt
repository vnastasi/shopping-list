package md.vnastasi.shoppinglist.screen.overview

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

object OverviewScreenModule {

    operator fun invoke() = module {
        factoryOf(ListOverviewViewModel::Factory)
    }
}
