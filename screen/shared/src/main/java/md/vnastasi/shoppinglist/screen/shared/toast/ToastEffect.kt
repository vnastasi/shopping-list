package md.vnastasi.shoppinglist.screen.shared.toast

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources

@Composable
fun Toast(
    message: ToastMessage?,
    onToastShown: () -> Unit = { }
) {
    val context = LocalContext.current
    val resources = LocalResources.current
    LaunchedEffect(key1 = message) {
        if (message != null) {
            Toast.makeText(context, resources.getString(message.textResourceId, *message.arguments.toTypedArray()), message.duration).show()
            onToastShown.invoke()
        }
    }
}
