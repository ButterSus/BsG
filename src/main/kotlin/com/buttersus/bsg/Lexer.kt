@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package com.buttersus.bsg

import com.buttersus.gramutils.*

class Lexer : LexerBase<Lexer, TokenType, Token>(TokenType.Details) {
    // Create methods
    override fun createToken(`𝚃`: TokenType, `𝚟`: String): Token = Token(this, `𝚃`, `𝚟`)

    // Lex method
    override fun lex() = iterator {
        skipRegex("""//.*?(?=\n)""") ?: return@iterator
        yieldRegex("""\d+""", TokenType.NUMBER) ?: return@iterator
        yieldRegex("""\p{L}[\p{L}0-9]*(?!-)\b""", TokenType.NAME) ?: return@iterator
        yieldRegex("""\p{L}[\p{L}0-9-]*\b""", TokenType.CNAME) ?: return@iterator
        yieldRegex("""'.*?'""", TokenType.S_STR) ?: return@iterator
        yieldRegex("""".*?"""", TokenType.D_STR) ?: return@iterator
        yieldRegex("""=>|[:.<>{}()=${'$'}+*?!|,@]|->|\?!""", TokenType.OPERATOR) ?: return@iterator
    }
}
