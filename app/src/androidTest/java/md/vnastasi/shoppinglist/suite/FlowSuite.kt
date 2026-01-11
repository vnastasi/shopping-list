package md.vnastasi.shoppinglist.suite

import md.vnastasi.shoppinglist.ui.flow.CompleteShoppingListFlowTest
import md.vnastasi.shoppinglist.ui.flow.CreateShoppingListFlowTest
import md.vnastasi.shoppinglist.ui.flow.EditAndDeleteShoppingListFlowTest
import md.vnastasi.shoppinglist.ui.flow.ManageSuggestionsFlowTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    value = [
        CompleteShoppingListFlowTest::class,
        CreateShoppingListFlowTest::class,
        EditAndDeleteShoppingListFlowTest::class,
        ManageSuggestionsFlowTest::class,
    ]
)
class FlowSuite
