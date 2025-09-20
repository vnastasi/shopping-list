package md.vnastasi.shoppinglist.ui.flow

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import md.vnastasi.shoppinglist.MainActivity
import md.vnastasi.shoppinglist.db.model.ShoppingItem
import md.vnastasi.shoppinglist.db.model.ShoppingList
import md.vnastasi.shoppinglist.ui.robot.addItemsScreen
import md.vnastasi.shoppinglist.ui.robot.listDetailsScreen
import md.vnastasi.shoppinglist.ui.robot.overviewScreen
import md.vnastasi.shoppinglist.ui.rule.databaseRule
import md.vnastasi.shoppinglist.ui.rule.disableAnimationsRule
import md.vnastasi.shoppinglist.ui.rule.retryOnFailureRule
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.rules.TestRule

@HiltAndroidTest
class ManageSuggestionsFlowTest {

    private val hiltAndroidRule = HiltAndroidRule(this)

    private val composeRule = createAndroidComposeRule<MainActivity>()

    private val databaseRule = databaseRule(
        onSetUp = {
            val shoppingListDao = shoppingListDao()
            val shoppingItemDao = shoppingItemDao()

            shoppingListDao.create(ShoppingList(1L, "My list"))

            sequenceOf(
                ShoppingItem(id = 1L, name = "Eggs", isChecked = false, listId = 1L),
                ShoppingItem(id = 2L, name = "Egg white", isChecked = false, listId = 1L),
                ShoppingItem(id = 3L, name = "Egg yolks", isChecked = false, listId = 1L),
                ShoppingItem(id = 4L, name = "Boiled eggs", isChecked = false, listId = 1L),
            ).forEach { shoppingItem ->
                shoppingItemDao.create(shoppingItem)
                shoppingItemDao.delete(shoppingItem)
            }
        }
    )

    @get:Rule
    val ruleChain: TestRule = RuleChain
        .outerRule(hiltAndroidRule)
        .around(databaseRule)
        .around(composeRule)
        .around(retryOnFailureRule(maxAttempts = 3))
        .around(disableAnimationsRule())

    @Test
    fun manageNameSuggestions() {
        overviewScreen(composeRule) {
            clickOnShoppingListCard("My list")
        }

        listDetailsScreen(composeRule) {
            clickOnAddItemsFab()
        }

        addItemsScreen(composeRule) {
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

        listDetailsScreen(composeRule) {
            hasUncheckedItem("Boiled eggs")
            navigateBack()
        }
    }
}
