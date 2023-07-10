package md.vnastasi.shoppinglist

import android.app.Application
import md.vnastasi.shoppinglist.db.DatabaseModule
import md.vnastasi.shoppinglist.domain.DomainModule
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
            modules(DatabaseModule(), DomainModule())
        }
    }
}
