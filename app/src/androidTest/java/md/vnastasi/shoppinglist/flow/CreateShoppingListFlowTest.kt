package md.vnastasi.shoppinglist.flow

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import md.vnastasi.shoppinglist.MainActivity
import md.vnastasi.shoppinglist.res.R
import md.vnastasi.shoppinglist.screen.listdetails.ui.TestTags.ADD_LIST_ITEMS_FAB
import md.vnastasi.shoppinglist.screen.listdetails.ui.TestTags.LIST_DETAILS_TOOLBAR
import md.vnastasi.shoppinglist.screen.overview.ui.TestTags.NEW_SHOPPING_LIST_FAB
import md.vnastasi.shoppinglist.screen.overview.ui.TestTags.NEW_SHOPPING_LIST_TEXT_FIELD
import md.vnastasi.shoppinglist.screen.overview.ui.TestTags.SHOPPING_LIST_CARD
import org.junit.Rule
import org.junit.Test

private const val SHOPPING_LIST_NAME = "Test new shopping list"

@OptIn(ExperimentalTestApi::class)
class CreateShoppingListFlowTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun createShoppingList() {
        val resources = composeRule.activity.resources

        composeRule.onNode(hasText(resources.getString(R.string.overview_empty_list))).isDisplayed()

        composeRule.onNode(hasTestTag(NEW_SHOPPING_LIST_FAB)).performClick()

        composeRule.onNode(hasTestTag(NEW_SHOPPING_LIST_TEXT_FIELD)).performTextInput(SHOPPING_LIST_NAME)

        composeRule.onNode(hasText(resources.getString(R.string.list_form_btn_save))).performClick()

        composeRule.onNode(hasText(resources.getString(R.string.overview_empty_list))).isNotDisplayed()

        composeRule.waitUntilAtLeastOneExists(hasTestTag(SHOPPING_LIST_CARD) and hasAnyDescendant(hasText(SHOPPING_LIST_NAME)) and hasAnyDescendant(hasText("0 / 0")), 1_000L)

        composeRule.onNode(hasTestTag(SHOPPING_LIST_CARD) and hasAnyDescendant(hasText(SHOPPING_LIST_NAME)) and hasAnyDescendant(hasText("0 / 0"))).performScrollTo().isDisplayed()

        composeRule.onNode(hasTestTag(SHOPPING_LIST_CARD) and hasAnyDescendant(hasText(SHOPPING_LIST_NAME))).performScrollTo().performClick()

        composeRule.onNode(hasTestTag(LIST_DETAILS_TOOLBAR) and hasAnyDescendant(hasTestTag(SHOPPING_LIST_NAME))).isDisplayed()

        composeRule.onNode(hasText(resources.getString(R.string.list_details_empty))).isDisplayed()

        composeRule.onNode(hasTestTag(ADD_LIST_ITEMS_FAB)).performClick()

        composeRule.onNode(hasText(resources.getString(R.string.add_items_search_title))).isDisplayed()
    }
}