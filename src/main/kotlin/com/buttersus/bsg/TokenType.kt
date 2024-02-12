package com.buttersus.bsg

import com.buttersus.gramutils.*

enum class TokenType {
    NAME, CNAME, OPERATOR, NUMBER,
    INDENT, DEDENT, EOF, NEWLINE,
    S_STR, D_STR;

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
