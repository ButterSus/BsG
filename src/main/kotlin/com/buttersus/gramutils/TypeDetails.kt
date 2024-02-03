@file:Suppress("DeprecatedCallableAddReplaceWith", "Unused")

package com.buttersus.gramutils

/**
 * This is the interface used to provide details for the parser.
 * It must be used in the companion object of the enum class
 * to provide details for the parser.
 *
 * Example:
 * ```
 * enum class Type {
 *    NAME, CNAME, OPERATOR,
 *    INDENT, DEDENT, EOF, NEWLINE,
 *    S_STR, D_STR;
 *
 *    companion object Details : TypeDetails {
 *        override fun getEOF() = EOF
 *        override fun getNewline() = NEWLINE
 *    }
 * }
 * ```
 *
 * @param TB The type of the tokens.
 * @see TypeBase
 */
interface TypeDetails<TB: TypeBase> {
    // Options
    /**
     * Should the parser generate newline tokens?
     *
     * @see lastNewline
     * @see getNewline
     */
    val generateNewlines: Boolean
        get() = true

    /**
     * PEG parsers usually stop only when they reach the end of the file,
     * this is generally represented by an EOF token.
     *
     * @see getEOF
     */
    val generateEOF: Boolean
        get() = true

    /**
     * Should the parser generate indent and dedent tokens?
     *
     * @see getIndent
     * @see getDedent
     */
    val generateIndents: Boolean
        get() = false

    /**
     * Should the parser add a newline token at the end of the file if it's missing?
     * This is useful for some grammars, like the Python grammar.
     *
     * @see generateNewlines
     * @see getNewline
     */
    val lastNewline: Boolean
        get() = false

    // Tokens
    /**
     * @see generateEOF
     */
    fun getEOF(): TB? = null

    /**
     * @see generateNewlines
     */
    fun getNewline(): TB? = null

    /**
     * @see generateIndents
     */
    fun getIndent(): TB? = null

    /**
     * @see generateIndents
     */
    fun getDedent(): TB? = null
}