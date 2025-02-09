package md.vnastasi.shoppinglist.component

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModelProvider
import kotlinx.collections.immutable.ImmutableMap
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.named

@Immutable
class ViewModelFactoryCreator(
    val map: ImmutableMap<Qualifier, () -> ViewModelProvider.Factory>
) {

    inline fun <reified T : ViewModelProvider.Factory> create(): T = map.getValue(named<T>()).invoke() as T
}
