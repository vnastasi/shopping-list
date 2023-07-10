package md.vnastasi.shoppinglist.screen.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun MainScreen(navController: NavHostController) {

    Column {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

            text = "This is the main screen"
        )

        Button(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally),

            onClick = { navController.navigate("shopping-list/12345") },
        ) {
            Text("Click me!")
        }
    }
}
