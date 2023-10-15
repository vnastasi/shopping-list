package md.vnastasi.shoppinglist.screen.nav

import android.os.Bundle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Routes {

    abstract val path: String
    abstract val arguments: List<NamedNavArgument>

    data object ListOverview : Routes() {

        override val path: String = "shopping-list"

        override val arguments: List<NamedNavArgument> = emptyList()

        operator fun invoke(): String = path
    }

    data object ListDetails : Routes() {

        private const val ARG_KEY_SHOPPING_LIST_ID = "SHOPPING_LIST_ID"

        private val shoppingListIdArg = navArgument(ARG_KEY_SHOPPING_LIST_ID) {
            type = NavType.LongType
            nullable = false
        }

        override val path: String = "shopping-list/{$ARG_KEY_SHOPPING_LIST_ID}"

        override val arguments: List<NamedNavArgument> = listOf(shoppingListIdArg)

        operator fun invoke(shoppingListId: Long): String = path.replace("{$ARG_KEY_SHOPPING_LIST_ID}", shoppingListId.toString())

        fun extractShoppingListId(bundle: Bundle?): Long = requireNotNull(bundle?.getLong(ARG_KEY_SHOPPING_LIST_ID))
    }

    data object AddItems : Routes() {

        private const val ARG_KEY_SHOPPING_LIST_ID = "SHOPPING_LIST_ID"

        private val shoppingListIdArg = navArgument(ARG_KEY_SHOPPING_LIST_ID) {
            type = NavType.LongType
            nullable = false
        }

        override val path: String = "shopping-list/{$ARG_KEY_SHOPPING_LIST_ID}/add"

        override val arguments: List<NamedNavArgument> = listOf(shoppingListIdArg)

        operator fun invoke(shoppingListId: Long): String = path.replace("{${ARG_KEY_SHOPPING_LIST_ID}}", shoppingListId.toString())

        fun extractShoppingListId(bundle: Bundle?): Long = requireNotNull(bundle?.getLong(ARG_KEY_SHOPPING_LIST_ID))
    }
}
