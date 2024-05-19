package md.vnastasi.shoppinglist.ui.flow

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import md.vnastasi.shoppinglist.MainActivity
import md.vnastasi.shoppinglist.support.async.DispatchersProvider
import md.vnastasi.shoppinglist.ui.robot.addItemsScreen
import md.vnastasi.shoppinglist.ui.robot.listDetailsScreen
import md.vnastasi.shoppinglist.ui.robot.overviewScreen
import md.vnastasi.shoppinglist.ui.robot.shoppingListForm
import md.vnastasi.shoppinglist.ui.rule.createDatabaseRule
import md.vnastasi.shoppinglist.ui.rule.createKoinTestModuleRule
import md.vnastasi.shoppinglist.ui.support.UiTestDispatcherProvider
import org.junit.Rule
import org.junit.Test

class CreateShoppingListFlowTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    val koinTestModeRule = createKoinTestModuleRule {
        single<DispatchersProvider> { UiTestDispatcherProvider() }
    }

    @get:Rule
    val databaseRule = createDatabaseRule()

    @Test
    fun createNewShoppingList(): Unit = with(composeRule) {
        overviewScreen {
            hasEmptyOverviewMessage()
            clickOnNewShoppingListFab()
        }

        shoppingListForm {
            typeShoppingListName("Test new shopping list")
            clickOnSaveButton()
        }

        overviewScreen {
            hasNoEmptyOverviewMessage()
            hasShoppingListCard("Test new shopping list", 0, 0)
            clickOnShoppingListCard("Test new shopping list")
        }

        listDetailsScreen {
            hasToolbarName("Test new shopping list")
            hasEmptyShoppingListMessage()
            clickOnAddItemsFab()
        }

        addItemsScreen {
            hasEmptySearchbar()
            typeSearchQuery("Milk")
            clickOnSuggestionItem("Milk")
            clearSearchbar()
            typeSearchQuery("Bread")
            acceptValueFromKeyboard()
            typeSearchQuery("Coconut oil")
            acceptValueFromKeyboard()
            navigateBack()
        }

        listDetailsScreen {
            hasNoEmptyShoppingListMessage()
            hasUncheckedItem("Coconut oil")
            hasUncheckedItem("Bread")
            hasUncheckedItem("Milk")
            navigateBack()
        }

        overviewScreen {
            hasShoppingListCard("Test new shopping list", 3, 0)
        }
    }
}
