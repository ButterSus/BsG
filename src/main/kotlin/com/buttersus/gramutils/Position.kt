@file:Suppress("Unused", "MemberVisibilityCanBePrivate")

package com.buttersus.gramutils

/**
 * This class represents a position in a source.
 * It's shortcut for storing the source and the index.
 *
 * Usage:
 * ```
 * val 𝚂 = Source("Hello, world!")
 * val 𝚙 = Position(𝚂, 7)
 * ```
 *
 * @param 𝚂 The source.
 * @param 𝚒 The index into the source.
 * @property 𝚜 The length of the source. _(Computed)_
 * @property 𝚕 The number of lines in the source. _(Computed)_
 * @property 𝚢 The line number of the character at this position. _(Computed)_
 * @property 𝚡 The column number of the character at this position. _(Computed)_
 * @property 𝚊 The character at this position. _(Computed)_
 *
 * @see Source
 */
data class Position(
    val `𝚂`: Source,    // Source
    val `𝚒`: Index = 0, // Index into the string
) {
    // Public properties
    val `𝚜` get() = 𝚂.`𝚜`
    val `𝚕` get() = 𝚂.`𝚕`
    val `𝚢` get() = 𝚂.`𝚢`(`𝚒`)
    val `𝚡` get() = 𝚂.`𝚡`(`𝚒`)
    val `𝚊` get() = 𝚂.`𝜔`[`𝚒`]

    // Methods
    val next get() = Position(𝚂, `𝚒` + 1)
    val prev get() = Position(𝚂, `𝚒` - 1)
    override fun toString() = "(${`𝚢`}, ${`𝚡`})"

    // Boolean methods
    /**
     * Returns `true` if the position is at the end of the source.
     *
     * Usage:
     * ```
     * val 𝚂 = Source("Hello, world!")
     * val 𝚙 = Position(𝚂, 7)
     * 𝚙.isAtEnd() // false
     * ```
     */
    fun isAtEnd() = `𝚒` >= 𝚂.`𝚜`

    /**
     * Returns `true` if the position is not at the end of the source.
     * Nice for readability with loops.
     *
     * Usage:
     * ```
     * val 𝚂 = Source("Hello, world!")
     * val 𝚙 = Position(𝚂)
     * while (𝚙.isNotAtEnd()) { … }
     * ```
     */
    fun isNotAtEnd() = !isAtEnd()

    // Operators
    operator fun plus(`𝚒`: Index) = Position(𝚂, this.`𝚒` + `𝚒`)
    operator fun plus(`𝚙`: Position) = if (`𝚙`.`𝚂` == 𝚂)
        Position(𝚂, this.`𝚒` + `𝚙`.`𝚒`) else throw Exception("Cannot add positions from different sources")

    operator fun minus(`𝚒`: Index) = Position(𝚂, this.`𝚒` - `𝚒`)
    operator fun minus(`𝚙`: Position) = if (`𝚙`.`𝚂` == 𝚂)
        Position(𝚂, this.`𝚒` - `𝚙`.`𝚒`) else throw Exception("Cannot subtract positions from different sources")

    operator fun compareTo(`𝚙`: Position) = if (`𝚙`.`𝚂` == 𝚂)
        this.`𝚒` - `𝚙`.`𝚒` else throw Exception("Cannot compare positions from different sources")
}
