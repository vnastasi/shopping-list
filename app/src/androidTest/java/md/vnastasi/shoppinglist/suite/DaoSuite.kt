package md.vnastasi.shoppinglist.suite

import md.vnastasi.shoppinglist.db.dao.NameSuggestionDaoTest
import md.vnastasi.shoppinglist.db.dao.ShoppingItemDaoTest
import md.vnastasi.shoppinglist.db.dao.ShoppingListDaoTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    value = [
        NameSuggestionDaoTest::class,
        ShoppingItemDaoTest::class,
        ShoppingListDaoTest::class
    ]
)
class DaoSuite
