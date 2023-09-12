package md.vnastasi.shoppinglist.support.state

sealed interface ScreenState<out T : Any, out R : Any> {

    data object Loading : ScreenState<Nothing, Nothing>

    data object Empty : ScreenState<Nothing, Nothing>

    data class Ready<out T : Any>(val data: T) : ScreenState<T, Nothing>

    data class Failure<out R : Any>(val failure: R) : ScreenState<Nothing, R>

    companion object {

        fun loading(): ScreenState<Nothing, Nothing> = Loading

        fun empty(): ScreenState<Nothing, Nothing> = Empty

        fun <T : Any> ready(data: T): ScreenState<T, Nothing> = Ready(data)

        fun <R : Any> failure(failure: R): ScreenState<Nothing, R> = Failure(failure)
    }
}
