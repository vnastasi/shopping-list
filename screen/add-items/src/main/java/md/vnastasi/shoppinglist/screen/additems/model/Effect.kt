package md.vnastasi.shoppinglist.screen.additems.model

import androidx.compose.runtime.Immutable
import md.vnastasi.shoppinglist.screen.shared.toast.ToastMessage

@Immutable
sealed interface Effect {

    @Immutable
    data class Navigation(val target: NavigationTarget) : Effect

    @Immutable
    data class ShowToast(val toastMessage: ToastMessage) : Effect
}
