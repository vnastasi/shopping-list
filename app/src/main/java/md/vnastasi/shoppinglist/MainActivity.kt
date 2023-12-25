package md.vnastasi.shoppinglist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import md.vnastasi.shoppinglist.nav.NavigationGraph
import md.vnastasi.shoppinglist.support.theme.AppTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                NavigationGraph()
            }
        }
    }
}
