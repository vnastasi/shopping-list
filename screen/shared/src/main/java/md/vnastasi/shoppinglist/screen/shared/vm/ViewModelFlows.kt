package md.vnastasi.shoppinglist.screen.shared.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

private const val FLOW_SUBSCRIPTION_TIMEOUT = 5_000L

context(viewModel: ViewModel)
fun <T> Flow<T>.asStateFlow(initialValue: T): StateFlow<T> =
    stateIn(
        scope = viewModel.viewModelScope,
        started = SharingStarted.WhileSubscribed(FLOW_SUBSCRIPTION_TIMEOUT),
        initialValue = initialValue
    )
