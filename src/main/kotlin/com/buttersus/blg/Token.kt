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
    override fun toString(): String = `𝚃`.toString().padEnd(10) +
            "(${`𝚟`.noSpec().strictEnd(10)})".padEnd(12) + "@ $`𝚙₁`..$`𝚙₂`"

    /**
     * Returns a short string representation of the token.
     *
     * Usage:
     * ```
     * val token = Token(Type.IDENTIFIER, "foo", Position(…), Position(…))
     * println(token.toShortString()) // IDENTIFIER(foo)
     * ```
     *
     * @return A short string representation of the token.
     * @see toString
     */
    fun toShortString(): String = "$𝚃(${`𝚟`.noSpec().strictEnd(10)})"
}
