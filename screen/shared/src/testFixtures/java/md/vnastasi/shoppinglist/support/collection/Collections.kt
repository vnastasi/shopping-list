package md.vnastasi.shoppinglist.support.collection

fun <T, U> crossJoin(s1: Sequence<T>, s2: Sequence<U>): List<Pair<T, U>> =
    s1.flatMap { e1 -> s2.map { e2 -> e1 to e2 } }.toList()

fun <T, U, V> crossJoin(s1: Sequence<T>, s2: Sequence<U>, s3: Sequence<V>): List<Triple<T, U, V>> =
    s1.flatMap { e1 -> s2.flatMap { e2 -> s3.map { e3 -> Triple(e1, e2, e3) } } }.toList()