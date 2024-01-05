package ru.alexadler9.newsfetcher.base

sealed class Either<out LEFT, out RIGHT> {

    class Left<LEFT>(val value: LEFT) : Either<LEFT, Nothing>()
    class Right<RIGHT>(val value: RIGHT) : Either<Nothing, RIGHT>()

    val isRight: Boolean
        get() = this is Right

    val isLeft: Boolean
        get() = this is Left

    inline fun <TYPE> fold(onError: (LEFT) -> TYPE, onSuccess: (RIGHT) -> TYPE): TYPE =
        when (this) {
            is Left -> onError(this.value)
            is Right -> onSuccess(this.value)
        }

    inline fun <TYPE> map(transform: (RIGHT) -> TYPE): Either<LEFT, TYPE> = when (this) {
        is Left -> Left(this.value)
        is Right -> Right(transform(this.value))
    }

    inline fun <TYPE> mapLeft(transform: (LEFT) -> TYPE): Either<TYPE, RIGHT> = when (this) {
        is Left -> Left(transform(this.value))
        is Right -> Right(this.value)
    }
}
