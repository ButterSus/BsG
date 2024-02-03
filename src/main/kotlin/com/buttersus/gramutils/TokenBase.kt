@file:Suppress("Unused", "MemberVisibilityCanBePrivate")

package com.buttersus.gramutils

/**
 * General class for all tokens.
 * It's used to provide a common interface for all tokens.
 *
 * @param TB Type of the token.
 * @param 𝚃 Type of the token.
 * @param 𝚟 Value of the token.
 * @param 𝚙ₛ Position of the first character of the token.
 * @param 𝚙ₑ Position of the last character of the token.
 *
 * @see TypeBase
 */
abstract class TokenBase<TB: TypeBase>(
    val `𝚃`: TB,
    val `𝚟`: String,
    val `𝚙ₛ`: Position,
    val `𝚙ₑ`: Position,
) {
    /**
     * Returns a short string representation of the token.
     *
     * Usage:
     * ```
     * val token = Token(Type.IDENTIFIER, "foo", Position(…), Position(…))
     * println(token.toShortString()) // IDENTIFIER(foo)
     * ```
     *
     * @return A short string representation of the token.
     */
    override fun toString(): String = "$𝚃(${`𝚟`.noSpec().strictEnd(10)})"

    /**
     * Returns a formatted string representation of the token.
     * _(which is usually used for logging)_
     *
     * Usage:
     * ```
     * val token = Token(Type.IDENTIFIER, "foo", Position(…), Position(…))
     * println(token.toFormattedString()) // IDENTIFIER     (foo)        @ (1, 1)…(1, 3)
     * ```
     *
     * @return A formatted string representation of the token.
     */
    open fun toFormattedString(): String = `𝚃`.toString().padEnd(10) +
            "(${`𝚟`.noSpec().strictEnd(10)})".padEnd(12) + "@ $`𝚙ₛ`…$`𝚙ₑ`"
}