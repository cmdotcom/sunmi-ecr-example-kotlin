package com.cm.payplaza.ecr_sdk_integration.domain.model

sealed class Either<out L, out R> {
    /** * Represents the left side of [Either] class which by convention is a "Failure". */
    data class Left<out L>(val a: L) : Either<L, Nothing>()

    /** * Represents the right side of [Either] class which by convention is a "Success". */
    data class Right<out R>(val b: R) : Either<Nothing, R>()

    val isRight get() = this is Right<R>
    val isLeft get() = this is Left<L>

    companion object {
        // Creates a Right or Left type.
        fun <L> left(a: L) = Either.Left(a)
        fun <R> right(b: R) = Either.Right(b)
    }

    fun<V> getEither(action: () -> V): Either<Exception, V> =
        try { Either.right(action()) } catch (e: Exception) { Either.left(e) }

    fun fold(fnL: (L) -> Any, fnR: (R) -> Any): Any =
        when (this) {
            is Left -> fnL(a)
            is Right -> fnR(b)
        }

    // Composes 2 functions
    fun <A, B, C> ((A) -> B).c(f: (B) -> C): (A) -> C = {
        f(this(it))
    }

    /**
     * Right-biased flatMap() FP convention which means that Right is assumed to be the default case
     * to operate on. If it is Left, operations like map, flatMap, ... return the Left value unchanged.
     */
    fun <T, L, R> Either<L, R>.flatMap(fn: (R) -> Either<L, T>): Either<L, T> =
        when (this) {
            is Either.Left -> Either.Left(a)
            is Either.Right -> fn(b)
        }

    /**
     * Right-biased map() FP convention which means that Right is assumed to be the default case
     * to operate on. If it is Left, operations like map, flatMap, ... return the Left value unchanged.
     */
    fun <T, L, R> Either<L, R>.map(fn: (R) -> (T)): Either<L, T> = this.flatMap(fn.c(::right))

    /** Returns the value from this `Right` or the given argument if this is a `Left`.
     *  Right(12).getOrElse(17) RETURNS 12 and Left(12).getOrElse(17) RETURNS 17
     */
    fun <L, R> Either<L, R>.getOrElse(value: R): R =
        when (this) {
            is Either.Left -> value
            is Either.Right -> b
        }

    /**
     * Left-biased onFailure() FP convention dictates that when this class is Left, it'll perform
     * the onFailure functionality passed as a parameter, but, overall will still return an either
     * object so you chain calls.
     */
    fun <L, R> Either<L, R>.onFailure(fn: (failure: L) -> Unit): Either<L, R> =
        this.apply { if (this is Either.Left) fn(a) }

    /**
     * Right-biased onSuccess() FP convention dictates that when this class is Right, it'll perform
     * the onSuccess functionality passed as a parameter, but, overall will still return an either
     * object so you chain calls.
     */
    fun <L, R> Either<L, R>.onSuccess(fn: (success: R) -> Unit): Either<L, R> =
        this.apply { if (this is Either.Right) fn(b) }
}