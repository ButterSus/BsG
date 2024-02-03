package com.buttersus.blg

import com.buttersus.gramutils.*

enum class TokenType {
    NAME, CNAME, OPERATOR,
    INDENT, DEDENT, EOF, NEWLINE,
    S_STR, D_STR;

    companion object Details : TypeDetails<TokenType> {
        override val generateEOF: Boolean = true
        override fun getEOF() = EOF

        override val generateNewlines: Boolean = true
        override fun getNewline() = NEWLINE

        override val generateIndents: Boolean = true
        override fun getIndent() = INDENT
        override fun getDedent() = DEDENT

        override val lastNewline: Boolean = true
    }
}
