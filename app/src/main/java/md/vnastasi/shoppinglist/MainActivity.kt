package md.vnastasi.shoppinglist

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import md.vnastasi.shoppinglist.support.di.ViewModelFactoryCreator
import md.vnastasi.shoppinglist.screen.ApplicationScreenContainer
import md.vnastasi.shoppinglist.support.theme.AppTheme

class MainActivity(
    private val viewModelFactories: ViewModelFactoryCreator
) : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                ApplicationScreenContainer(viewModelFactories)
            }
        }
    }
}
