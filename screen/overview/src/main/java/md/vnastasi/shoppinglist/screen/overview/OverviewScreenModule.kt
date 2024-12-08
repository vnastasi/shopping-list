package md.vnastasi.shoppinglist.screen.overview

import md.vnastasi.shoppinglist.screen.overview.vm.OverviewViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

object OverviewScreenModule {

    operator fun invoke() = module {
        factoryOf(OverviewViewModel::Factory)
    }
}
