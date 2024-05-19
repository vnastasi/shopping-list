package md.vnastasi.shoppinglist.ui.flow

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import md.vnastasi.shoppinglist.MainActivity
import md.vnastasi.shoppinglist.support.async.DispatchersProvider
import md.vnastasi.shoppinglist.ui.robot.addItemsScreen
import md.vnastasi.shoppinglist.ui.robot.listDetailsScreen
import md.vnastasi.shoppinglist.ui.robot.overviewScreen
import md.vnastasi.shoppinglist.ui.robot.shoppingListForm
import md.vnastasi.shoppinglist.ui.rule.createKoinTestModule
import md.vnastasi.shoppinglist.ui.support.UiTestDispatcherProvider
import org.junit.Rule
import org.junit.Test

private const val SHOPPING_LIST_NAME = "Test new shopping list"
private const val SHOPPING_ITEM_NAME_1 = "Milk"
private const val SHOPPING_ITEM_NAME_2 = "Bread"
private const val SHOPPING_ITEM_NAME_3 = "Coconut oil"

class CreateShoppingListFlowTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    val koinTestModeRule = createKoinTestModule {
        single<DispatchersProvider> { UiTestDispatcherProvider() }
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
            typeSearchQuery(SHOPPING_ITEM_NAME_1)
            clickOnSuggestionItem(SHOPPING_ITEM_NAME_1)
            clearSearchbar()
            typeSearchQuery(SHOPPING_ITEM_NAME_2)
            acceptValueFromKeyboard()
            typeSearchQuery(SHOPPING_ITEM_NAME_3)
            acceptValueFromKeyboard()
            navigateBack()
        }

        listDetailsScreen {
            hasNoEmptyShoppingListMessage()
            hasUncheckedItem(SHOPPING_ITEM_NAME_3)
            hasUncheckedItem(SHOPPING_ITEM_NAME_2)
            hasUncheckedItem(SHOPPING_ITEM_NAME_1)
            navigateBack()
        }

        overviewScreen {
            hasShoppingListCard(SHOPPING_LIST_NAME, 3, 0)
        }
    }
}
