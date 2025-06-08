package md.vnastasi.shoppinglist.db.support

import android.database.Cursor
import assertk.assertThat
import assertk.assertions.isEqualTo

fun Cursor.containsAtColumnWithIndex(index: Int, vararg values: String) {
    assertThat(this.count).isEqualTo(values.size)
    repeat(values.size) {
        moveToNext()
        assertThat(getString(index)).isEqualTo(values[it])
    }
}
