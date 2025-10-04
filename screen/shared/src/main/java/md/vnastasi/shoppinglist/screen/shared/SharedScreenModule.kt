package md.vnastasi.shoppinglist.screen.shared

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Module
@InstallIn(ViewModelComponent::class)
class SharedScreenModule {

    @Provides
    @ViewModelScoped
    fun providesViewModelScope(): CoroutineScope =
        CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())
}
