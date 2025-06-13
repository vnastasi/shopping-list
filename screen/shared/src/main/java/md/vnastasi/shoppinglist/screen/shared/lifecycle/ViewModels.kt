@file:JvmName("ViewModelSupport")

package md.vnastasi.shoppinglist.screen.shared.lifecycle

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.core.os.bundleOf
import androidx.lifecycle.DEFAULT_ARGS_KEY
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
inline fun <reified VM : ViewModel> viewModel(
    viewModelStoreOwner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) { "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner" },
    key: String? = null,
    factory: ViewModelProvider.Factory? = null,
    extraArguments: Bundle = bundleOf()
): VM {
    val defaultExtras = when (viewModelStoreOwner) {
        is HasDefaultViewModelProviderFactory -> viewModelStoreOwner.defaultViewModelCreationExtras
        else -> CreationExtras.Empty
    }
    val arguments = defaultExtras[DEFAULT_ARGS_KEY] + extraArguments
    val newExtras = MutableCreationExtras(defaultExtras).apply { set(DEFAULT_ARGS_KEY, arguments) }

    return viewModel(viewModelStoreOwner, key, factory, newExtras)
}
