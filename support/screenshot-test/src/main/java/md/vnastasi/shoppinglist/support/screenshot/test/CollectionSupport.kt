package md.vnastasi.shoppinglist.support.screenshot.test

internal fun <T, U> crossJoin(s1: Sequence<T & Any>, s2: Sequence<U & Any>): List<Pair<T & Any, U & Any>> =
    s1.flatMap { e1 -> s2.map { e2 -> e1 to e2 } }.toList()
