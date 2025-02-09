package md.vnastasi.shoppinglist.ui.flow

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import md.vnastasi.shoppinglist.MainActivity
import md.vnastasi.shoppinglist.db.model.NameSuggestion
import md.vnastasi.shoppinglist.db.model.ShoppingList
import md.vnastasi.shoppinglist.support.async.DispatchersProvider
import md.vnastasi.shoppinglist.ui.robot.addItemsScreen
import md.vnastasi.shoppinglist.ui.robot.listDetailsScreen
import md.vnastasi.shoppinglist.ui.robot.overviewScreen
import md.vnastasi.shoppinglist.ui.rule.createComponentFactoryRule
import md.vnastasi.shoppinglist.ui.rule.createDatabaseRule
import md.vnastasi.shoppinglist.ui.rule.createKoinTestModuleRule
import md.vnastasi.shoppinglist.ui.rule.disableAnimationsRule
import md.vnastasi.shoppinglist.ui.rule.retryOnFailureRule
import md.vnastasi.shoppinglist.ui.support.UiTestDispatcherProvider
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.rules.TestRule

class ManageSuggestionsFlowTest {

    private val composeRule = createAndroidComposeRule<MainActivity>()

    private val koinTestModeRule = createKoinTestModuleRule {
        single<DispatchersProvider> { UiTestDispatcherProvider() }
    }

    private val databaseRule = createDatabaseRule(
        setUp = {
            shoppingListDao().create(ShoppingList(1L, "My list"))
            shoppingItemNameSuggestionDao().create(NameSuggestion(1L, "Eggs"))
            shoppingItemNameSuggestionDao().create(NameSuggestion(2L, "Egg white"))
            shoppingItemNameSuggestionDao().create(NameSuggestion(3L, "Egg yolks"))
            shoppingItemNameSuggestionDao().create(NameSuggestion(4L, "Boiled eggs"))
        }
    )

    @get:Rule
    val ruleChain: TestRule = RuleChain
        .outerRule(createComponentFactoryRule())
        .around(composeRule)
        .around(retryOnFailureRule(maxAttempts = 3))
        .around(databaseRule)
        .around(koinTestModeRule)
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
