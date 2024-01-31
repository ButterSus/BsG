@file:Suppress("MemberVisibilityCanBePrivate")

package com.buttersus.blg

import mu.KotlinLogging

class Lexer {
    // Attributes
    internal lateinit var `𝚂`: Source
    internal lateinit var `𝚙`: Position
    internal val logger = KotlinLogging.logger {}

    operator fun invoke(`𝚂`: Source): Lexer {
        this.𝚂 = 𝚂
        this.`𝚙` = Position(𝚂)
        return this
    }

    private val indentStack = mutableListOf(0)

    // Methods
    fun tokenize(): Iterator<Token> = iterator {
        logger.info { "Starting..." }
        while (`𝚙`.isNotAtEnd()) {
            // Skip whitespaces
            Regex("""[^\S\r\n]*""").matchAt(`𝚙`)!!
                .also { this@Lexer.`𝚙` += it.value.length }
            // Indent; Dedent; Newline
            if (Regex("""\r?\n(?:[^\S\r\n]*\r?\n)*""").matchAt(`𝚙`)
                    ?.also { yield(newToken(Type.NEWLINE, it.value)) } != null
            ) {
                Regex("""[^\S\r\n]*""").matchAt(`𝚙`)!!
                    .also {
                        val newIndentLevel = it.value.length
                        val currentIndentLevel = indentStack.last()

                        when {
                            newIndentLevel > currentIndentLevel -> {
                                indentStack.add(newIndentLevel)
                                yield(newToken(Type.INDENT, it.value))
                            }

                            newIndentLevel < currentIndentLevel -> {
                                while (newIndentLevel < indentStack.last()) {
                                    indentStack.removeAt(indentStack.lastIndex)
                                    yield(newToken(Type.DEDENT, ""))
                                }
                            }
                        }
                    }
                continue
            }
            if (Regex("""\p{L}+(?!-)\b""").matchAt(`𝚙`)
                    ?.also { yield(newToken(Type.NAME, it.value)) } != null
            ) continue
            if (Regex("""[\p{L}0-9-]+\b""").matchAt(`𝚙`)
                    ?.also { yield(newToken(Type.CNAME, it.value)) } != null
            ) continue
            if (Regex("""'.*?'""", RegexOption.DOT_MATCHES_ALL).matchAt(`𝚙`)
                    ?.also { yield(newToken(Type.S_STR, it.value)) } != null
            ) continue
            if (Regex("""".*?"""", RegexOption.DOT_MATCHES_ALL).matchAt(`𝚙`)
                    ?.also { yield(newToken(Type.D_STR, it.value)) } != null
            ) continue
            if (Regex("""=>|[:.<>{}()=$+*?!|,]|->|\?!""", RegexOption.DOT_MATCHES_ALL).matchAt(`𝚙`)
                    ?.also { yield(newToken(Type.OPERATOR, it.value)) } != null
            ) continue
            // Unexpected character
            throw Exception("Unexpected character at $`𝚙` -> ${`𝚙`.`𝚊`}")
        }
        // Add dedents; Newline; EOF
        indentStack.run { forEach { _ -> yield(newToken(Type.DEDENT, "")) }; clear() }
        `𝚂`.`𝜔`.indices.reversed().find { `𝚂`.`𝜔`[it] !in "\t\r " }
            ?.let { if (`𝚂`.`𝜔`[it] != '\n') yield(newToken(Type.NEWLINE, "")) }
        yield(newToken(Type.EOF, ""))
        logger.info { "Finished" }
    }
}
