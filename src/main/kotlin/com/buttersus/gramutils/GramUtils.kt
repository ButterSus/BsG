@file:Suppress("Unused")

package com.buttersus.gramutils

import kotlin.reflect.KFunction

// Types
// =====>
/**
 * Represents a position in a source.
 * _(type alias for `Int`)_
 */
typealias Index = Int

/**
 * Represents any type of token.
 * _(type alias for `Enum<*>`)_
 */
typealias TypeBase = Enum<*>

// Positioning
// ===========>
/**
 * Wrapper for the Regex.matchAt method, which makes able to use Position instead of String & Int.
 *
 * Usage:
 * ```
 * val 𝚛 = Regex("a")
 * 𝚛.matchAt(Position(Source("abc"), 1)) // MatchResult
 * ```
 *
 * @param `𝚙` Position of the string to match.
 * @return MatchResult if the match succeeded; otherwise, null.
 * @see Regex.matchAt
 */
fun Regex.matchAt(`𝚙`: Position): MatchResult? = this.matchAt(`𝚙`.`𝚂`.`𝜔`, `𝚙`.`𝚒`)

/**
 * Returns the position of the first occurrence, which is greater than or equal to element.
 * _(List must be sorted)_
 *
 * Usage:
 * ```
 * val 𝚗 = listOf(1, 2, 3, 6, 9)
 * 𝚗.bisect(4) // 2
 * ```
 *
 * @param element The element to search for.
 * @return The position of the first occurrence, which is greater than or equal to element.
 */
fun <T : Comparable<T>> Array<T>.bisect(element: T): Index {
    var left = 0
    var right = lastIndex
    while (left <= right) {
        val mid = (left + right) / 2
        when {
            this[mid] < element -> left = mid + 1
            this[mid] > element -> right = mid - 1
            else -> return mid
        }
    }
    return right
}

// String methods
// ==============>
/**
 * Converts the string to a source.
 * _(wrapper around the Source constructor)_
 *
 * Usage:
 * ```
 * "abc".toSource() // Source("abc")
 * ```
 *
 * @return A source, which wraps the string.
 */
fun String.toSource(): Source = Source(this)

/**
 * Returns a string, which is more readable than the original.
 * _(replaces special characters with their escape sequences)_
 *
 * Usage:
 * ```
 * "a\nb".noSpec() // "a\\nb"
 * ```
 *
 * @return A string, which is more readable than the original.
 */
fun String.noSpec(): String = this
    .replace("\n", "\\n")
    .replace("\r", "\\r")
    .replace("\t", "\\t")

/**
 * Strictly cuts the string to the specified length.
 * _(adds ellipsis if the string is longer than the specified length)_
 *
 * Usage:
 * ```
 * "abcde".strictStart(3) // "…abc"
 * "ab".strictStart(3) // "ab"
 * ```
 *
 * @param n The length of the resulting string.
 * @return A string, which is strictly cut to the specified length.
 */
fun String.strictStart(n: Int): String = if (this.length > n) "…" + this.substring(0, n - 1) else this

/**
 * Strictly cuts the string to the specified length.
 * _(adds ellipsis if the string is longer than the specified length)_
 *
 * Usage:
 * ```
 * "abcde".strictEnd(3) // "abc…"
 * "ab".strictEnd(3) // "ab"
 * ```
 *
 * @param n The length of the resulting string.
 * @return A string, which is strictly cut to the specified length.
 */
fun String.strictEnd(n: Int): String = if (this.length > n) this.substring(0, n - 1) + "…" else this

// Function methods
// ================>
/**
 * Removes all arguments from the function,
 * by applying the specified arguments to it. _(works with 1-8 arguments)_
 *
 * Usage:
 * ```
 * val 𝚏 = { a: Int, b: Int -> a + b }
 * val 𝚏′ = 𝚏.`→`(1, 2)
 * 𝚏′() // 3
 * ```
 *
 * @param arg1 The first argument to apply to the function.
 * @return A function, which has no arguments.
 */
fun <T1, R, F : (T1) -> R>
        F.`→`(arg1: T1): () -> R = {
    this(arg1)
}

/**
 * Removes all arguments from the function,
 * by applying the specified arguments to it. _(works with 1-8 arguments)_
 *
 * Usage:
 * ```
 * val 𝚏 = { a: Int, b: Int -> a + b }
 * val 𝚏′ = 𝚏.`→`(1, 2)
 * 𝚏′() // 3
 * ```
 *
 * @param arg1 The first argument to apply to the function.
 * @param arg2 The second argument to apply to the function.
 * @return A function, which has no arguments.
 */
fun <T1, T2, R, F : (T1, T2) -> R>
        F.`→`(arg1: T1, arg2: T2): () -> R = {
    this(arg1, arg2)
}

/**
 * Removes all arguments from the function,
 * by applying the specified arguments to it. _(works with 1-8 arguments)_
 *
 * Usage:
 * ```
 * val 𝚏 = { a: Int, b: Int -> a + b }
 * val 𝚏′ = 𝚏.`→`(1, 2)
 * 𝚏′() // 3
 * ```
 *
 * @param arg1 The first argument to apply to the function.
 * @param arg2 The second argument to apply to the function.
 * @param arg3 The third argument to apply to the function.
 * @return A function, which has no arguments.
 */
fun <T1, T2, T3, R, F : (T1, T2, T3) -> R>
        F.`→`(arg1: T1, arg2: T2, arg3: T3): () -> R = {
    this(arg1, arg2, arg3)
}

/**
 * Removes all arguments from the function,
 * by applying the specified arguments to it. _(works with 1-8 arguments)_
 *
 * Usage:
 * ```
 * val 𝚏 = { a: Int, b: Int -> a + b }
 * val 𝚏′ = 𝚏.`→`(1, 2)
 * 𝚏′() // 3
 * ```
 *
 * @param arg1 The first argument to apply to the function.
 * @param arg2 The second argument to apply to the function.
 * @param arg3 The third argument to apply to the function.
 * @param arg4 The fourth argument to apply to the function.
 * @return A function, which has no arguments.
 */
fun <T1, T2, T3, T4, R, F : (T1, T2, T3, T4) -> R>
        F.`→`(arg1: T1, arg2: T2, arg3: T3, arg4: T4): () -> R = {
    this(arg1, arg2, arg3, arg4)
}

/**
 * Removes all arguments from the function,
 * by applying the specified arguments to it. _(works with 1-8 arguments)_
 *
 * Usage:
 * ```
 * val 𝚏 = { a: Int, b: Int -> a + b }
 * val 𝚏′ = 𝚏.`→`(1, 2)
 * 𝚏′() // 3
 * ```
 *
 * @param arg1 The first argument to apply to the function.
 * @param arg2 The second argument to apply to the function.
 * @param arg3 The third argument to apply to the function.
 * @param arg4 The fourth argument to apply to the function.
 * @param arg5 The fifth argument to apply to the function.
 * @return A function, which has no arguments.
 */
fun <T1, T2, T3, T4, T5, R, F : (T1, T2, T3, T4, T5) -> R>
        F.`→`(arg1: T1, arg2: T2, arg3: T3, arg4: T4, arg5: T5): () -> R = {
    this(arg1, arg2, arg3, arg4, arg5)
}

/**
 * Removes all arguments from the function,
 * by applying the specified arguments to it. _(works with 1-8 arguments)_
 *
 * Usage:
 * ```
 * val 𝚏 = { a: Int, b: Int -> a + b }
 * val 𝚏′ = 𝚏.`→`(1, 2)
 * 𝚏′() // 3
 * ```
 *
 * @param arg1 The first argument to apply to the function.
 * @param arg2 The second argument to apply to the function.
 * @param arg3 The third argument to apply to the function.
 * @param arg4 The fourth argument to apply to the function.
 * @param arg5 The fifth argument to apply to the function.
 * @param arg6 The sixth argument to apply to the function.
 * @return A function, which has no arguments.
 */
fun <T1, T2, T3, T4, T5, T6, R, F : (T1, T2, T3, T4, T5, T6) -> R>
        F.`→`(arg1: T1, arg2: T2, arg3: T3, arg4: T4, arg5: T5, arg6: T6): () -> R = {
    this(arg1, arg2, arg3, arg4, arg5, arg6)
}

/**
 * Removes all arguments from the function,
 * by applying the specified arguments to it. _(works with 1-8 arguments)_
 *
 * Usage:
 * ```
 * val 𝚏 = { a: Int, b: Int -> a + b }
 * val 𝚏′ = 𝚏.`→`(1, 2)
 * 𝚏′() // 3
 * ```
 *
 * @param arg1 The first argument to apply to the function.
 * @param arg2 The second argument to apply to the function.
 * @param arg3 The third argument to apply to the function.
 * @param arg4 The fourth argument to apply to the function.
 * @param arg5 The fifth argument to apply to the function.
 * @param arg6 The sixth argument to apply to the function.
 * @param arg7 The seventh argument to apply to the function.
 * @return A function, which has no arguments.
 */
fun <T1, T2, T3, T4, T5, T6, T7, R, F : (T1, T2, T3, T4, T5, T6, T7) -> R>
        F.`→`(arg1: T1, arg2: T2, arg3: T3, arg4: T4, arg5: T5, arg6: T6, arg7: T7): () -> R = {
    this(arg1, arg2, arg3, arg4, arg5, arg6, arg7)
}

/**
 * Removes all arguments from the function,
 * by applying the specified arguments to it. _(works with 1-8 arguments)_
 *
 * Usage:
 * ```
 * val 𝚏 = { a: Int, b: Int -> a + b }
 * val 𝚏′ = 𝚏.`→`(1, 2)
 * 𝚏′() // 3
 * ```
 *
 * @param arg1 The first argument to apply to the function.
 * @param arg2 The second argument to apply to the function.
 * @param arg3 The third argument to apply to the function.
 * @param arg4 The fourth argument to apply to the function.
 * @param arg5 The fifth argument to apply to the function.
 * @param arg6 The sixth argument to apply to the function.
 * @param arg7 The seventh argument to apply to the function.
 * @param arg8 The eighth argument to apply to the function.
 * @return A function, which has no arguments.
 */
fun <T1, T2, T3, T4, T5, T6, T7, T8, R, F : (T1, T2, T3, T4, T5, T6, T7, T8) -> R>
        F.`→`(arg1: T1, arg2: T2, arg3: T3, arg4: T4, arg5: T5, arg6: T6, arg7: T7, arg8: T8): () -> R = {
    this(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8)
}

/**
 * Removes all arguments from the function,
 * by applying the specified arguments to it. _(variable arguments)_
 *
 * Usage:
 * ```
 * val 𝚏 = { l: List<Int> -> l.sum() }
 * val 𝚏′ = 𝚏.`→`(1, 2, 3)
 * 𝚏′() // 6
 * ```
 *
 * @param args The arguments to apply to the function.
 * @return A function, which has no arguments.
 */
inline fun <reified T, R, F : (List<T>) -> R>
        F.`→`(vararg args: T): () -> R = {
    this(args.toList())
}

/**
 * Removes all arguments from the function,
 * by applying the specified arguments to it. _(variable arguments)_
 * **But** in contrast to [`→`](#):
 * 1. It takes arguments wrapped in a lambda. _(or just a function)_
 * 2. Optionally, it takes a set of indices, which are used to unpack the returned list of arguments.
 *
 * Usage:
 * ```
 * val 𝚏 = { a: Int, b: Int -> a + b }
 * val 𝚏′ = 𝚏.`→…`({ 1 }, { 2 })
 * 𝚏′() // 3
 * ```
 *
 * @param 𝚒s Indices of needed nodes _(starts from 1)_
 * @param args The arguments to apply to the function.
 * @return A function, which has no arguments.
 */
fun <R> KFunction<R>.`→…`(`𝚒s`: Set<Index>, vararg args: () -> Any?): () -> R? = {
    val `𝚊` = ArrayList<Any?>()
    for ((index, arg) in args.withIndex()) when {
        index + 1 in `𝚒s` -> (arg() as List<Any?>?)?.let { `𝚊`.addAll(it) }
        else -> `𝚊`.add(arg())
    }
    if (`𝚊`.size != this.parameters.size || `𝚊`.any { it == null }) null
    else this.call(*`𝚊`.toTypedArray())
}

/**
 * Removes all arguments from the function,
 * by applying the specified arguments to it. _(variable arguments)_
 * **But** in contrast to [`→`](#):
 * 1. It takes arguments wrapped in a lambda. _(or just a function)_
 * 2. Optionally, it takes a set of indices, which are used to unpack the returned list of arguments.
 *
 * Usage:
 * ```
 * val 𝚏 = { a: Int, b: Int -> a + b }
 * val 𝚏′ = 𝚏.`→…`({ 1 }, { 2 })
 * 𝚏′() // 3
 * ```
 *
 * @param args The arguments to apply to the function.
 * @return A function, which has no arguments.
 */
fun <R> KFunction<R>.`→…`(vararg args: () -> Any?): () -> R? = this.`→…`(setOf(), *args)

/**
 * Chooses the needed nodes from the group when function is called.
 *
 * Usage:
 * ```
 * val 𝚏 = ::{…}.→(𝚙₁, 𝚙₂, 𝚙₃).select(1, 3)
 * 𝚏() // (𝚗₁, 𝚗₃)
 * ```
 *
 * @param 𝚒s Indices of needed nodes _(starts from 1)_
 * @return List of needed nodes
 */
fun <NB : NodeBase<NB>, GB : NodeGroupBase<NB, GB>> (() -> GB?).select(vararg `𝚒s`: Index): () -> GB? = {
    this()?.select(*`𝚒s`)
}

/**
 * Chooses one node from the group when function is called.
 *
 * Usage:
 * ```
 * val 𝚏 = {…}.→(𝚙₁, 𝚙₂, 𝚙₃).item(1)
 * 𝚏() // 𝚗₁
 * ```
 *
 * @param 𝚒 Index of needed node _(starts from 1)_
 * @return Needed node
 */
@Suppress("BOUNDS_NOT_ALLOWED_IF_BOUNDED_BY_TYPE_PARAMETER")
fun <NB : NodeBase<NB>, GB> (() -> GB?).item(`𝚒`: Index): () -> NB? where GB : NB, GB : NodeGroupBase<NB, GB> = {
    this()?.item(`𝚒`)
}

/**
 *
 */
fun NodeBase<*>?.prettier(indent: Int = 4): String = when (this) {
    is NodeWrapperBase<*, *> -> "⟨${`𝚝`.`𝚟`}⟩"
    is NodeEmptyBase -> "∅"
    is NodeGroupBase<*, *> -> {
        val nodes = this.map { it.prettier(indent) }
        val lines = nodes.getOrNull(0)?.count { it == '\n' }
        when {
            size == 0 -> "{ }"
            size == 1 && lines == 0 -> "{ 1 -> ${nodes[0]} }"
            else -> "{\n" + nodes.mapIndexed { index, node ->
                "${index + 1} -> $node"
            }.joinToString(",\n").prependIndent(" ".repeat(indent)) + "\n}"
        }
    }

    is NodeDynamicGroupBase<*, *> -> {
        val nodes = this.map { it.prettier(indent) }
        val lines = nodes.getOrNull(0)?.count { it == '\n' }
        when {
            size == 0 -> "[ ]"
            size == 1 && lines == 0 -> "[ ${nodes[0]} ]"
            else -> "[\n" + nodes.joinToString(",\n").prependIndent(" ".repeat(indent)) + "\n]"
        }
    }

    null -> "Parsing error"
    else -> {
        val inner = getPropertiesList().joinToString(",\n")
        { (n, v) -> "$n: ${v.prettier(indent)}" }.prependIndent(" ".repeat(indent))
        val params = getParametersList()
        if (params.isEmpty()) "${this::class.simpleName} {\n$inner\n}"
        else {
            val innerParams = params.joinToString(",\n") { (n, v) -> "$n: $v" }
                .prependIndent(" ".repeat(indent) + "$")
            "${this::class.simpleName} {\n$innerParams\n$inner\n}"
        }
    }
}
