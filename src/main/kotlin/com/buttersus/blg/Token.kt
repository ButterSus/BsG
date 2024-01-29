package com.buttersus.blg

data class Token(
    val `𝚃`: Type,
    val `𝚟`: String,
    val `𝚙₁`: Position,
    val `𝚙₂`: Position,
) {
    // Constructor shortcut
    constructor(`𝕃`: Lexer, `𝚃`: Type, `𝚟`: String) : this(`𝚃`, `𝚟`, `𝕃`.`𝚙`, `𝕃`.`𝚙` + `𝚟`.length - 1)

    // Methods
    override fun toString(): String = "$`𝚃`(${
        `𝚟`.replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t")
    }) @ $`𝚙₁`..$`𝚙₂`"
}
