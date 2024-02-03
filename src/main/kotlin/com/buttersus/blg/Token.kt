package com.buttersus.blg

import com.buttersus.gramutils.*

class Token(
    `𝚃`: TokenType,
    `𝚟`: String,
    `𝚙₁`: Position,
    `𝚙₂`: Position,
) : TokenBase<TokenType>(`𝚃`, `𝚟`, `𝚙₁`, `𝚙₂`) {
    constructor(`𝕃`: Lexer, `𝚃`: TokenType, `𝚟`: String) : this(`𝚃`, `𝚟`, `𝕃`.`𝚙`, `𝕃`.`𝚙`)
}
