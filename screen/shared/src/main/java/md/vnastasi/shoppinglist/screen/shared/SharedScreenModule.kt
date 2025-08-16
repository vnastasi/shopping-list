package md.vnastasi.shoppinglist.screen.shared

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Module
@InstallIn(SingletonComponent::class)
class SharedScreenModule {

    @Provides
    fun providesViewModelScope(): CoroutineScope =
        CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())
}
