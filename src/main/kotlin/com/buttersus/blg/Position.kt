@file:Suppress("MemberVisibilityCanBePrivate")

package com.buttersus.blg

// Represents a position in a source
data class Position(
    val `𝚂`: Source,    // Source
    val `𝚒`: Index = 0, // Index into the string
) {
    // Computed properties
    val `𝚜` get() = 𝚂.`𝚜`    // Length of the string
    val `𝚕` get() = 𝚂.`𝚕`    // Number of lines
    val `𝚢` get() = 𝚂.`𝚢`(`𝚒`) // Line number
    val `𝚡` get() = 𝚂.`𝚡`(`𝚒`) // Column number

    // Additional properties
    val `𝚊` get() = 𝚂.`𝜔`[`𝚒`] // Character at this position
    val next get() = Position(𝚂, `𝚒` + 1)
    val prev get() = Position(𝚂, `𝚒` - 1)

    // Methods
    override fun toString() = "(${`𝚢`}, ${`𝚡`})"
    fun isAtEnd() = `𝚒` >= 𝚂.`𝚜`
    fun isNotAtEnd() = !isAtEnd()

    // Operators
    operator fun plus(`𝚒`: Index) = Position(𝚂, this.`𝚒` + `𝚒`)
    operator fun plus(`𝚙`: Position) = Position(𝚂, this.`𝚒` + `𝚙`.`𝚒`)
    operator fun minus(`𝚒`: Index) = Position(𝚂, this.`𝚒` - `𝚒`)
    operator fun minus(`𝚙`: Position) = Position(𝚂, this.`𝚒` - `𝚙`.`𝚒`)
    operator fun compareTo(`𝚙`: Position) = this.`𝚒` - `𝚙`.`𝚒`
}
