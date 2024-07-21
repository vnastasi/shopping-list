package md.vnastasi.shoppinglist.support.async

fun <T, U> crossJoin(s1: Sequence<T>, s2: Sequence<U>): List<Pair<T, U>> =
    s1.flatMap { e1 -> s2.map { e2 -> e1 to e2 } }.toList()
