package md.vnastasi.shoppinglist

import kotlinx.collections.immutable.persistentMapOf
import md.vnastasi.shoppinglist.component.ViewModelFactoryCreator
import md.vnastasi.shoppinglist.screen.additems.vm.AddItemsViewModel
import md.vnastasi.shoppinglist.screen.listdetails.vm.ListDetailsViewModel
import md.vnastasi.shoppinglist.screen.overview.vm.OverviewViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

object ApplicationModule {

    operator fun invoke() = module {
        single {
            ViewModelFactoryCreator(
                persistentMapOf(
                    named<OverviewViewModel.Factory>() to { get<OverviewViewModel.Factory>() },
                    named<ListDetailsViewModel.Factory>() to { get<ListDetailsViewModel.Factory>() },
                    named<AddItemsViewModel.Factory>() to { get<AddItemsViewModel.Factory>() }
                )
            )
        }

        factoryOf(::MainActivity)
    }
}
