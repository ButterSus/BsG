@file:Suppress("MemberVisibilityCanBePrivate")

package com.buttersus.gramutils

/**
 * Syntax error exception base.
 * It's used by the parser to throw syntax errors.
 *
 * @param 𝚙ₛ Start position of the error
 * @param 𝚙ₑ End position of the error
 * @param message Message of the error
 * @see Position
 * @see ParserBase
 */
abstract class SyntaxExceptionBase(
    val `𝚙ₛ`: Position,
    val `𝚙ₑ`: Position,
    message: String,
) : Exception(
    """
    |Syntax error: $message
    """.trimMargin()
)