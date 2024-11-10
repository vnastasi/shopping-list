package md.vnastasi.shoppinglist.suite

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    value = [
        InstrumentationSuite::class,
        FlowSuite::class
    ]
)
class AllSuite
