package md.vnastasi.shoppinglist.ui.rule

import androidx.test.ext.junit.rules.AppComponentFactoryRule
import md.vnastasi.shoppinglist.ComponentFactory
import org.junit.rules.TestRule

fun createComponentFactoryRule(): TestRule = AppComponentFactoryRule(ComponentFactory())
