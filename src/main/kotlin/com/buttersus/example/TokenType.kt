package com.buttersus.example

import com.buttersus.gramutils.*

enum class TokenType {
    CUSTOM1, CUSTOM2,
    INDENT, DEDENT, EOF, NEWLINE;

    companion object Details : TypeDetails<TokenType> {
        override val generateEOF: Boolean = true
        override fun getEOF() = EOF

        override val generateNewlines: Boolean = true
        override fun getNewline() = NEWLINE

        override val generateIndents: Boolean = true
        override val keepFirstNewline: Boolean = false
        override fun getIndent() = INDENT
        override fun getDedent() = DEDENT

        override val generateLastNewline: Boolean = true
    }
}
