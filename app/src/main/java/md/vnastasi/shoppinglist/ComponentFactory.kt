package md.vnastasi.shoppinglist

import android.app.Activity
import android.app.Application
import android.content.Intent
import androidx.core.app.AppComponentFactory
import md.vnastasi.shoppinglist.db.DatabaseModule
import md.vnastasi.shoppinglist.domain.DomainModule
import md.vnastasi.shoppinglist.screen.additems.AddItemsScreenModule
import md.vnastasi.shoppinglist.screen.listdetails.ListDetailsScreenModule
import md.vnastasi.shoppinglist.screen.overview.OverviewScreenModule
import md.vnastasi.shoppinglist.support.di.activityFactory
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.Module

@Suppress("unused")
open class ComponentFactory : AppComponentFactory() {

    private val activityFactory = activityFactory()

    override fun instantiateApplicationCompat(cl: ClassLoader, className: String): Application =
        super.instantiateApplicationCompat(cl, className).also(::setupKoin)

    override fun instantiateActivityCompat(cl: ClassLoader, className: String, intent: Intent?): Activity =
        activityFactory.instantiateActivityCompat(cl, className) ?: super.instantiateActivityCompat(cl, className, intent)

    protected open fun modules(): List<Module> = listOf(
        ApplicationModule(),
        DatabaseModule(),
        DomainModule(),
        OverviewScreenModule(),
        ListDetailsScreenModule(),
        AddItemsScreenModule()
    )

    private fun setupKoin(application: Application) {
        startKoin {
            androidLogger()
            androidContext(application)
            modules(this@ComponentFactory.modules())
        }
    }
}
