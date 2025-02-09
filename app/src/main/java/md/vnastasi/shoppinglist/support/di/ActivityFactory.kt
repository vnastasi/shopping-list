package md.vnastasi.shoppinglist.support.di

import android.app.Activity
import org.koin.core.component.KoinComponent

fun activityFactory() = ActivityFactory()

class ActivityFactory : KoinComponent {

    fun instantiateActivityCompat(cl: ClassLoader, className: String): Activity? =
        getKoin().getOrNull(cl.loadClass(className).kotlin)
}
