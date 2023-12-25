package md.vnastasi.shoppinglist

import android.app.Application
import md.vnastasi.shoppinglist.db.DatabaseModule
import md.vnastasi.shoppinglist.domain.DomainModule
import md.vnastasi.shoppinglist.screen.additems.AddItemsScreenModule
import md.vnastasi.shoppinglist.screen.listdetails.ListDetailsScreenModule
import md.vnastasi.shoppinglist.screen.overview.OverviewScreenModule
import md.vnastasi.shoppinglist.support.async.AsyncSupportModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ShoppingListApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        setupKoin()
    }

    private fun setupKoin() {
        startKoin {
            androidLogger()
            androidContext(this@ShoppingListApplication)
            modules(
                AsyncSupportModule(),
                DatabaseModule(),
                DomainModule(),
                OverviewScreenModule(),
                ListDetailsScreenModule(),
                AddItemsScreenModule()
            )
        }
    }
}
