package md.vnastasi.shoppinglist.component

import android.app.Activity
import org.koin.core.component.KoinComponent

class ActivityFactory : KoinComponent {

    fun instantiateActivityCompat(cl: ClassLoader, className: String): Activity? =
        getKoin().getOrNull(cl.loadClass(className).kotlin)
}
