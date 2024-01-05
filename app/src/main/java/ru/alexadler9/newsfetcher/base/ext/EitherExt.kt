package ru.alexadler9.newsfetcher.base.ext

import ru.alexadler9.newsfetcher.base.Either

inline fun <TYPE, LEFT, RIGHT> Either<LEFT, RIGHT>.flatMap(
    transform: (RIGHT) -> Either<LEFT, TYPE>
): Either<LEFT, TYPE> = when (this) {
    is Either.Left -> Either.Left(value)
    is Either.Right -> transform(value)
}

inline fun <reified T> attempt(func: () -> T): Either<Throwable, T> = try {
    Either.Right(func.invoke())
} catch (e: Throwable) {
    Either.Left(e)
}