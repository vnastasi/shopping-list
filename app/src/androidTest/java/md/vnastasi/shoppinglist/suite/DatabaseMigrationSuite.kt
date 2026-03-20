package md.vnastasi.shoppinglist.suite

import md.vnastasi.shoppinglist.db.migration.MigrationFrom1To2Test
import md.vnastasi.shoppinglist.db.migration.MigrationFrom2To3Test
import md.vnastasi.shoppinglist.db.migration.MigrationFrom3To4Test
import md.vnastasi.shoppinglist.db.migration.MigrationFrom4To5Test
import md.vnastasi.shoppinglist.db.migration.MigrationFrom5To6Test
import md.vnastasi.shoppinglist.db.migration.MigrationFrom6To7Test
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    value = [
        MigrationFrom1To2Test::class,
        MigrationFrom2To3Test::class,
        MigrationFrom3To4Test::class,
        MigrationFrom4To5Test::class,
        MigrationFrom5To6Test::class,
        MigrationFrom6To7Test::class,
    ]
)
class DatabaseMigrationSuite
