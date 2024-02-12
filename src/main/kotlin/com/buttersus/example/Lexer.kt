@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package com.buttersus.example

import com.buttersus.gramutils.*

class Lexer : LexerBase<Lexer, TokenType, Token>(TokenType.Details) {
    // Create methods
    override fun createToken(`𝚃`: TokenType, `𝚟`: String): Token = Token(this, `𝚃`, `𝚟`)

    // Lex method
    override fun lex() = iterator<Token> {
        TODO("Implement lex method")
    }
}
