package md.vnastasi.shoppinglist.ui.flow

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import md.vnastasi.shoppinglist.MainActivity
import md.vnastasi.shoppinglist.db.model.NameSuggestion
import md.vnastasi.shoppinglist.db.model.ShoppingList
import md.vnastasi.shoppinglist.support.async.DispatchersProvider
import md.vnastasi.shoppinglist.ui.robot.addItemsScreen
import md.vnastasi.shoppinglist.ui.robot.listDetailsScreen
import md.vnastasi.shoppinglist.ui.robot.overviewScreen
import md.vnastasi.shoppinglist.ui.rule.createDatabaseRule
import md.vnastasi.shoppinglist.ui.rule.createKoinTestModuleRule
import md.vnastasi.shoppinglist.ui.rule.disableAnimationsRule
import md.vnastasi.shoppinglist.ui.support.UiTestDispatcherProvider
import org.junit.Rule
import org.junit.Test

class ManageSuggestionsFlowTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    val animationsRule = disableAnimationsRule()

    @get:Rule
    val koinTestModeRule = createKoinTestModuleRule {
        single<DispatchersProvider> { UiTestDispatcherProvider() }
    }

    @get:Rule
    val databaseRule = createDatabaseRule(
        setUp = {
            shoppingListDao().create(ShoppingList(1L, "My list"))
            shoppingItemNameSuggestionDao().create(NameSuggestion(1L, "Eggs"))
            shoppingItemNameSuggestionDao().create(NameSuggestion(2L, "Egg white"))
            shoppingItemNameSuggestionDao().create(NameSuggestion(3L, "Egg yolks"))
            shoppingItemNameSuggestionDao().create(NameSuggestion(4L, "Boiled eggs"))
        }
    )

    @Test
    fun manageNameSuggestions(): Unit = with(composeRule) {
        overviewScreen {
            clickOnShoppingListCard("My list")
        }

        listDetailsScreen {
            clickOnAddItemsFab()
        }

        addItemsScreen {
            hasEmptySearchbar()
            typeSearchQuery("egg")
            hasSuggestionItem("egg")
            hasSuggestionItem("Eggs")
            hasSuggestionItem("Egg white")
            hasSuggestionItem("Egg yolks")
            hasSuggestionItem("Boiled eggs")
            deleteSuggestionItem("Egg yolks")
            deleteSuggestionItem("Egg white")
            hasNoSuggestionItem("Egg yolks")
            hasNoSuggestionItem("Egg white")
            clickOnSuggestionItem("Boiled eggs")
            navigateBack()
        }

        listDetailsScreen {
            hasUncheckedItem("Boiled eggs")
            navigateBack()
        }
    }
}
