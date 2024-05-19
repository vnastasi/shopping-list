package md.vnastasi.shoppinglist.suite

import md.vnastasi.shoppinglist.db.dao.NameSuggestionDaoTest
import md.vnastasi.shoppinglist.db.dao.ShoppingItemDaoTest
import md.vnastasi.shoppinglist.db.dao.ShoppingListDaoTest
import md.vnastasi.shoppinglist.db.migration.MigrationFrom1To2Test
import md.vnastasi.shoppinglist.db.migration.MigrationFrom2To3Test
import md.vnastasi.shoppinglist.db.migration.MigrationFrom3To4Test
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    value = [
        NameSuggestionDaoTest::class,
        ShoppingItemDaoTest::class,
        ShoppingListDaoTest::class,
        MigrationFrom1To2Test::class,
        MigrationFrom2To3Test::class,
        MigrationFrom3To4Test::class
    ]
)
class DatabaseSuite
