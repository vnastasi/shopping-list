package md.vnastasi.shoppinglist.ui.flow

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import md.vnastasi.shoppinglist.MainActivity
import md.vnastasi.shoppinglist.support.async.DispatchersProvider
import md.vnastasi.shoppinglist.ui.robot.addItemsScreen
import md.vnastasi.shoppinglist.ui.robot.listDetailsScreen
import md.vnastasi.shoppinglist.ui.robot.overviewScreen
import md.vnastasi.shoppinglist.ui.robot.shoppingListForm
import md.vnastasi.shoppinglist.ui.rule.createKoinTestModule
import md.vnastasi.shoppinglist.ui.support.InstrumentedTestDispatcherProvider
import org.junit.Rule
import org.junit.Test

private const val SHOPPING_LIST_NAME = "Test new shopping list"

class CreateShoppingListFlowTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    val koinTestModeRule = createKoinTestModule {
        single<DispatchersProvider> { InstrumentedTestDispatcherProvider() }
    }

    @Test
    fun createNewShoppingList(): Unit = with(composeRule) {
        overviewScreen {
            hasEmptyOverviewMessage()
            clickOnNewShoppingListFab()
        }

        shoppingListForm {
            typeShoppingListName(SHOPPING_LIST_NAME)
            clickOnSaveButton()
        }

        overviewScreen {
            hasNoEmptyOverviewMessage()
            hasShoppingListCard(SHOPPING_LIST_NAME, 0, 0)
            clickOnShoppingListCard(SHOPPING_LIST_NAME)
        }

        listDetailsScreen {
            hasToolbarName(SHOPPING_LIST_NAME)
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
            hasShoppingListCard(SHOPPING_LIST_NAME, 3, 0)
        }
    }
}
