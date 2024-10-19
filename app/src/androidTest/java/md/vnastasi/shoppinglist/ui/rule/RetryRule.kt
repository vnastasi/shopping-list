package md.vnastasi.shoppinglist.ui.rule

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

fun enableRetryRule(maxAttempts: Int = 1): TestRule = RetryRule(maxAttempts)

private class RetryRule(
    private val maxAttempts: Int
) : TestRule {

    override fun apply(base: Statement, description: Description): Statement =
        object : Statement() {
            override fun evaluate() {
                var lastThrowable: Throwable? = null
                for (counter in 1..maxAttempts) {
                    try {
                        base.evaluate()
                        return
                    } catch (t: Throwable) {
                        lastThrowable = t
                        System.err.println("${description.methodName}: test failed on run: `$counter`. Will run a maximum of `$maxAttempts` times.",)
                        t.printStackTrace()
                    }
                }
                if (lastThrowable != null) {
                    throw lastThrowable
                }
            }
        }
}
