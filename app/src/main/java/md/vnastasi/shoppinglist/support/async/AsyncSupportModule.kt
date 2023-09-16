package md.vnastasi.shoppinglist.support.async

import org.koin.dsl.module

object AsyncSupportModule {

    operator fun invoke() = module {
        single<DispatchersProvider> {
            DefaultDispatchersProvider()
        }
    }
}
