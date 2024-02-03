@file:Suppress("Unused", "MemberVisibilityCanBePrivate")

package com.buttersus.gramutils

import mu.KotlinLogging

/**
 * Base class for all lexers.
 *
 * @param S Self type.
 * @param TT Token type enum.
 * @param TB Token type.
 * @property 𝚂 Source to lex.
 * @property 𝚙 Current position.
 */
abstract class LexerBase<S : LexerBase<S, TT, TB>, TT : TypeBase, TB : TokenBase<TT>>(
    protected val details: TypeDetails<TT>,
) : Iterable<TB> {
    // Public properties
    lateinit var `𝚂`: Source
    lateinit var `𝚙`: Position

    // Logging
    protected val logger = KotlinLogging.logger {}

    // Methods
    private val indentStack = mutableListOf<Int>()

    /**
     * Main method of the lexer, which called every time the parser needs a new token.
     * It must be an iterator, which yields the tokens.
     *
     * @return Iterator of tokens.
     * @see iterator
     */
    protected abstract fun lex(): Iterator<TB>

    /**
     * Tokenizes the source, and returns an iterator of tokens.
     * Note, that during the tokenization, you can interact with the lexer
     * to implement context-sensitive lexing.
     */
    override fun iterator(): Iterator<TB> = iterator {
        logger.info { "Starting..." }
        while (`𝚙`.isNotAtEnd()) {
            Regex("""[^\S\r\n]*""").matchAt(`𝚙`)!!
                .also { this@LexerBase.`𝚙` += it.value.length }
            if (details.generateNewlines && Regex("""\r?\n(?:[^\S\r\n]*\r?\n)*""").matchAt(`𝚙`)
                    ?.also { yield(newToken(details.getNewline()!!, it.value)) } != null
            ) {
                Regex("""[^\S\r\n]*""").matchAt(`𝚙`)!!
                    .also {
                        if (!details.generateIndents) return@also
                        val newIndentLevel = it.value.length
                        val currentIndentLevel = indentStack.lastOrNull() ?: 0
                        when {
                            newIndentLevel > currentIndentLevel -> {
                                indentStack.add(newIndentLevel); yield(
                                    newToken(details.getIndent()!!, it.value)
                                )
                            }

                            newIndentLevel < currentIndentLevel -> {
                                while (newIndentLevel < (indentStack.lastOrNull() ?: 0)) {
                                    indentStack.removeLast()
                                    yield(newToken(details.getDedent()!!, ""))
                                }
                            }
                        }
                    }
                continue
            }
            val iterator = lex()
            if (iterator.hasNext()) yieldAll(iterator)
            else throw Exception("Unexpected character at $`𝚙` -> ${`𝚙`.`𝚊`}")
        }
        if (details.generateIndents) indentStack
            .run { forEach { _ -> yield(newToken(details.getDedent()!!, "")) }; clear() }
        if (details.generateNewlines && details.lastNewline) `𝚂`.`𝜔`.indices.reversed().find { `𝚂`.`𝜔`[it] !in "\t\r " }
            ?.let { if (`𝚂`.`𝜔`[it] != '\n') yield(newToken(details.getNewline()!!, "")) }
        if (details.generateEOF) yield(newToken(details.getEOF()!!, ""))
        logger.info { "Finished" }
    }

    /**
     * Yields a token if the given pattern matches the source at the current position.
     * To recognize if the pattern matches, check if the returned value **is** `null`.
     *
     * Usage:
     * ```
     * yieldRegex("""\d+""", TokenType.NUMBER) ?: return
     * ```
     */
    protected suspend fun SequenceScope<TB>.yieldRegex(pattern: String, type: TT): Unit? =
        Regex(pattern, RegexOption.DOT_MATCHES_ALL).matchAt(`𝚙`)?.also { yield(newToken(type, it.value)) }
            .let { if (it == null) Unit else null }

    /**
     * Creates a new token of the given type and value.
     * It's used by the lexer to create tokens, so you must override it.
     */
    protected abstract fun createToken(`𝚃`: TT, `𝚟`: String): TB

    /**
     * Creates a new token of the given type and value,
     * also updates the current position.
     *
     * @param 𝚃 Type of the token.
     * @param 𝚟 Value of the token.
     * @return The created token.
     */
    protected fun newToken(`𝚃`: TT, `𝚟`: String): TB = createToken(`𝚃`, `𝚟`)
        .also { token ->
            logger.trace { "$token" }
            `𝚙` += `𝚟`.length
        }

    /**
     * Resets the lexer to its initial state,
     * and sets the source to the given one.
     * _(Useful for reusing the same lexer)_
     *
     * @param 𝚂 Source to lex.
     * @return This lexer.
     */
    operator fun invoke(`𝚂`: Source): S {
        this.𝚂 = 𝚂
        this.`𝚙` = Position(𝚂)
        @Suppress("UNCHECKED_CAST")
        return this as S
    }
}