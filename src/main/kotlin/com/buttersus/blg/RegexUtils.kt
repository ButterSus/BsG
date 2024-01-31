@file:Suppress("Unused", "UNCHECKED_CAST")

package com.buttersus.blg

import kotlin.reflect.*
import kotlin.reflect.full.*

// Positioning
// ===========>
typealias Index = Int

fun String.toSource(): Source = Source(this)
fun String.toDeltaPosition(`𝚂`: Source): Position = Position(`𝚂`, this.length)

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

// String methods
// ==============>
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

// Lexer methods
// =============>
fun Lexer.newToken(`𝚃`: Type, `𝚟`: String): Token = Token(this, `𝚃`, `𝚟`).also {
    logger.trace { it }
    `𝚙` += `𝚟`.toDeltaPosition(this.`𝚂`)
}

// Node methods
// ============>
fun Token.wrap(): Node.Wrapper = Node.Wrapper(this)
fun Node.Wrapper.unwrap(): Token = this.`𝚝`
fun List<Node>.toGroup(): Node.Group = Node.Group(*this.toTypedArray())
fun Node.toPretty(indent: Int = 2): String {
    when (this) {
        is Node.Group -> {
            val content: List<String> = this.map { it.toPretty(indent) }
            val lines = content[0].count { it == '\n' }
            return when {
                this.size == 0 -> "{ ∅ }"
                this.size == 1 && lines == 0 -> "{ 1 -> ${content[0]} }"
                else -> {
                    val offset = " ".repeat(indent)
                    val inner = content
                        .mapIndexed { i, s -> "${i + 1} -> $s" }
                        .joinToString(",\n")
                        .prependIndent(offset)
                    "{\n$inner\n}"
                }
            }
        }

        is Node.Catalog -> {
            val content: List<String> = this.map { it.toPretty(indent) }
            val lines = content.getOrNull(0)?.count { it == '\n' }
            return when {
                this.size == 0 -> "[ ∅ ]"
                this.size == 1 && lines == 0 -> "[ ${content[0]} ]"
                else -> {
                    val offset = " ".repeat(indent)
                    val inner = content
                        .joinToString(",\n")
                        .prependIndent(offset)
                    "[\n$inner\n]"
                }
            }
        }

        is Node.Wrapper -> return if (this.`𝚝`.`𝚃` != Type.EOF) {
            "⟨${this.`𝚝`.`𝚟`}⟩"
        } else "File end..."

        is Node.Empty -> return "ε"
        else -> {
            val inner = this.properties
                .map { (k, v) -> "$k: ${v.toPretty()}" }
                .joinToString(",\n")
                .prependIndent(" ".repeat(indent))
            if (this.parameters.isEmpty())
                return "${this::class.simpleName} {\n$inner\n}"
            val params = this.parameters
                .map { (k, v) -> "$k: $v" }
                .joinToString(", ")
                .prependIndent(" ".repeat(indent) + "$")
            return "${this::class.simpleName} {\n$params\n$inner\n}"
        }
    }
}

// Function extension
// ==================>

// General documentation
// ---------------------
// This method is used to decrease the number of arguments in the
// function, which is achieved by passing default values.
//
// It returns a lambda, which can be called later to get the result.

fun <T1, R, F : (T1) -> R>
        F.`→`(arg1: T1): () -> R = {
    this(arg1)
}

fun <T1, T2, R, F : (T1, T2) -> R>
        F.`→`(arg1: T1, arg2: T2): () -> R = {
    this(arg1, arg2)
}

fun <T1, T2, T3, R, F : (T1, T2, T3) -> R>
        F.`→`(arg1: T1, arg2: T2, arg3: T3): () -> R = {
    this(arg1, arg2, arg3)
}

fun <T1, T2, T3, T4, R, F : (T1, T2, T3, T4) -> R>
        F.`→`(arg1: T1, arg2: T2, arg3: T3, arg4: T4): () -> R = {
    this(arg1, arg2, arg3, arg4)
}

fun <T1, T2, T3, T4, T5, R, F : (T1, T2, T3, T4, T5) -> R>
        F.`→`(arg1: T1, arg2: T2, arg3: T3, arg4: T4, arg5: T5): () -> R = {
    this(arg1, arg2, arg3, arg4, arg5)
}

fun <T1, T2, T3, T4, T5, T6, R, F : (T1, T2, T3, T4, T5, T6) -> R>
        F.`→`(arg1: T1, arg2: T2, arg3: T3, arg4: T4, arg5: T5, arg6: T6): () -> R = {
    this(arg1, arg2, arg3, arg4, arg5, arg6)
}

fun <T1, T2, T3, T4, T5, T6, T7, R, F : (T1, T2, T3, T4, T5, T6, T7) -> R>
        F.`→`(arg1: T1, arg2: T2, arg3: T3, arg4: T4, arg5: T5, arg6: T6, arg7: T7): () -> R = {
    this(arg1, arg2, arg3, arg4, arg5, arg6, arg7)
}

fun <T1, T2, T3, T4, T5, T6, T7, T8, R, F : (T1, T2, T3, T4, T5, T6, T7, T8) -> R>
        F.`→`(arg1: T1, arg2: T2, arg3: T3, arg4: T4, arg5: T5, arg6: T6, arg7: T7, arg8: T8): () -> R = {
    this(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8)
}

inline fun <reified T, R, F : (List<T>) -> R>
        F.`→`(vararg args: T): () -> R = {
    this(args.toList())
}

// Complex function extension
// ==========================>

// General documentation
// ---------------------
// This method is used to decrease the number of arguments in the
// function, but it's more complex than the previous one.
// It takes any amount of arguments, which must be wrapped in a lambda.
//
// Also, it [optional] takes a set of indices, which are used to unpack
// the returned list of arguments. _(see unpack method)_
//
// It returns a lambda, which can be called later to get the result.

fun<R> KFunction<R>.`→…`(`𝚒s`: Set<Index>, vararg args: () -> Any?): () -> R? = {
    val `𝚊` = ArrayList<Any?>()
    for ((index, arg) in args.withIndex()) when {
        index + 1 in `𝚒s` -> (arg() as List<Any?>?)?.let { `𝚊`.addAll(it) }
        else -> `𝚊`.add(arg())
    }
    if (`𝚊`.size != this.parameters.size || `𝚊`.any { it == null }) null
    else this.call(*`𝚊`.toTypedArray())
}

fun<R> KFunction<R>.`→…`(vararg args: () -> Any?): () -> R? = this.`→…`(setOf(), *args)

// Selecting elements
// ==================>

/**
 * Chooses the needed nodes from the group when function is called.
 *
 * Usage:
 * ```
 * val (𝚗₁, 𝚗₃) = Node.Group(𝚗₁, 𝚗₂, 𝚗₃).select(1, 3)
 * ```
 *
 * @param `𝚒s` Indices of needed nodes _(starts from 1)_
 * @return List of needed nodes
 * @see Node.Group
 */
fun <F : () -> Node.Group?> F.select(vararg `𝚒s`: Index): () -> Node.Group? = {
    this()?.select(*`𝚒s`)
}

/**
 * Chooses one node from the group when function is called.
 *
 * Usage:
 * ```
 * val 𝚗₁ = Node.Group(𝚗₁, 𝚗₂, 𝚗₃).item(1)
 * ```
 *
 * @param `𝚒` Index of needed node _(starts from 1)_
 * @return Needed node
 * @see Node.Group
 */
fun <F : () -> Node.Group?> F.item(`𝚒`: Index): () -> Node? = {
    this()?.item(`𝚒`)
}
