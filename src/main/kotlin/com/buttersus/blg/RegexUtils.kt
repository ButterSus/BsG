@file:Suppress("Unused", "UNCHECKED_CAST")

package com.buttersus.blg

// Index is an integer
typealias Index = Int

// Convert a string to a source
fun String.toSource(): Source = Source(this)

// Convert a string to a position
fun String.toDeltaPosition(`𝚂`: Source): Position = Position(`𝚂`, this.length)

// returns max element index that less or equal to element
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

// Shortcut to avoid writing `𝚂`.`𝚙`.`𝚒`
fun Regex.matchAt(`𝚙`: Position): MatchResult? = this.matchAt(`𝚙`.`𝚂`.`𝜔`, `𝚙`.`𝚒`)
fun Lexer.newToken(`𝚃`: Type, `𝚟`: String): Token = Token(this, `𝚃`, `𝚟`).also {
    logger.trace { it }
    `𝚙` += `𝚟`.toDeltaPosition(this.`𝚂`)
}

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
            val lines = content[0].count { it == '\n' }
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

private fun unpack(args: Array<out () -> Any?>, `𝚒s`: Set<Index>): Array<Any?> {
    var i = 0
    val `𝚊` = Array<Any?>(args.size) { null }
    while (i < args.size) if ((i + 1) in `𝚒s`) (args[i]() as List<Any?>).forEach { `𝚊`[i] = it; i += 1 }
    else `𝚊`[i] = args[i]().also { i += 1 }
    return `𝚊`
}

// Unpacking arguments
// -> If you want to unpack arguments from a list and pass them, use `→…` method
/** 1 argument unpacking */
fun <T1, R : Node, F : (T1) -> R>
        F.`→…`(`𝚒s`: Set<Index>, vararg args: () -> Any?): () -> R? = {
    val `𝚊` = unpack(args, `𝚒s`)
    this(
        `𝚊`[0] as T1
    )
}

fun <T1, R : Node, F : (T1) -> R>
        F.`→…`(vararg args: () -> Any?): () -> R? = this.`→…`(setOf(), *args)

/** 2 arguments unpacking */
fun <T1, T2, R : Node, F : (T1, T2) -> R>
        F.`→…`(`𝚒s`: Set<Index>, vararg args: () -> Any?): () -> R? = {
    val `𝚊` = unpack(args, `𝚒s`)
    this(
        `𝚊`[0] as T1,
        `𝚊`[1] as T2
    )
}

fun <T1, T2, R : Node, F : (T1, T2) -> R>
        F.`→…`(vararg args: () -> Any?): () -> R? = this.`→…`(setOf(), *args)

fun <T1, T2, T3, R : Node, F : (T1, T2, T3) -> R>
        F.`→…`(`𝚒s`: Set<Index>, vararg args: () -> Any?): () -> R? = {
    val `𝚊` = unpack(args, `𝚒s`)
    this(
        `𝚊`[0] as T1,
        `𝚊`[1] as T2,
        `𝚊`[2] as T3
    )
}

fun <T1, T2, T3, R : Node, F : (T1, T2, T3) -> R>
        F.`→…`(vararg args: () -> Any?): () -> R? = this.`→…`(setOf(), *args)

fun <T1, T2, T3, T4, R : Node, F : (T1, T2, T3, T4) -> R>
        F.`→…`(`𝚒s`: Set<Index>, vararg args: () -> Any?): () -> R? = {
    val `𝚊` = unpack(args, `𝚒s`)
    this(
        `𝚊`[0] as T1,
        `𝚊`[1] as T2,
        `𝚊`[2] as T3,
        `𝚊`[3] as T4
    )
}

fun <T1, T2, T3, T4, R : Node, F : (T1, T2, T3, T4) -> R>
        F.`→…`(vararg args: () -> Any?): () -> R? = this.`→…`(setOf(), *args)

fun <T1, T2, T3, T4, T5, R : Node, F : (T1, T2, T3, T4, T5) -> R>
        F.`→…`(`𝚒s`: Set<Index>, vararg args: () -> Any?): () -> R? = {
    val `𝚊` = unpack(args, `𝚒s`)
    this(
        `𝚊`[0] as T1,
        `𝚊`[1] as T2,
        `𝚊`[2] as T3,
        `𝚊`[3] as T4,
        `𝚊`[4] as T5
    )
}

fun <T1, T2, T3, T4, T5, R : Node, F : (T1, T2, T3, T4, T5) -> R>
        F.`→…`(vararg args: () -> Any?): () -> R? = this.`→…`(setOf(), *args)

fun <T1, T2, T3, T4, T5, T6, R : Node, F : (T1, T2, T3, T4, T5, T6) -> R>
        F.`→…`(`𝚒s`: Set<Index>, vararg args: () -> Any?): () -> R? = {
    val `𝚊` = unpack(args, `𝚒s`)
    this(
        `𝚊`[0] as T1,
        `𝚊`[1] as T2,
        `𝚊`[2] as T3,
        `𝚊`[3] as T4,
        `𝚊`[4] as T5,
        `𝚊`[5] as T6
    )
}

fun <T1, T2, T3, T4, T5, T6, R : Node, F : (T1, T2, T3, T4, T5, T6) -> R>
        F.`→…`(vararg args: () -> Any?): () -> R? = this.`→…`(setOf(), *args)

fun <T1, T2, T3, T4, T5, T6, T7, R : Node, F : (T1, T2, T3, T4, T5, T6, T7) -> R>
        F.`→…`(`𝚒s`: Set<Index>, vararg args: () -> Any?): () -> R? = {
    val `𝚊` = unpack(args, `𝚒s`)
    this(
        `𝚊`[0] as T1,
        `𝚊`[1] as T2,
        `𝚊`[2] as T3,
        `𝚊`[3] as T4,
        `𝚊`[4] as T5,
        `𝚊`[5] as T6,
        `𝚊`[6] as T7
    )
}

fun <T1, T2, T3, T4, T5, T6, T7, R : Node, F : (T1, T2, T3, T4, T5, T6, T7) -> R>
        F.`→…`(vararg args: () -> Any?): () -> R? = this.`→…`(setOf(), *args)

fun <T1, T2, T3, T4, T5, T6, T7, T8, R : Node, F : (T1, T2, T3, T4, T5, T6, T7, T8) -> R>
        F.`→…`(`𝚒s`: Set<Index>, vararg args: () -> Any?): () -> R? = {
    val `𝚊` = unpack(args, `𝚒s`)
    this(
        `𝚊`[0] as T1,
        `𝚊`[1] as T2,
        `𝚊`[2] as T3,
        `𝚊`[3] as T4,
        `𝚊`[4] as T5,
        `𝚊`[5] as T6,
        `𝚊`[6] as T7,
        `𝚊`[7] as T8
    )
}

fun <T1, T2, T3, T4, T5, T6, T7, T8, R : Node, F : (T1, T2, T3, T4, T5, T6, T7, T8) -> R>
        F.`→…`(vararg args: () -> Any?): () -> R? = this.`→…`(setOf(), *args)

/**
 * Add default values to the function.
 */

fun <T1, R, F : (T1) -> R>
        F.`→`(arg1: T1): () -> R = {
    this(arg1)
}

/**
 * Add default values to the function.
 */

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

// Selecting elements
// -> If you want to select elements from a list, use one of these methods
fun <F : () -> Node.Group?> F.select(vararg `𝚒s`: Index): () -> Node.Group? = {
    this()?.select(*`𝚒s`)
}

fun <F : () -> Node.Group?> F.item(`𝚒`: Index): () -> Node? = {
    this()?.item(`𝚒`)
}
