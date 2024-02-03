@file:Suppress("Unused")

package com.buttersus.gramutils

/**
 * A wrapper around a string that provides additional functionality for parsing.
 * Helps with keeping track of line and column numbers.
 *
 * @param 𝜔 The string to wrap
 * @property 𝚜 The length of the string _(Computed)_
 * @property 𝚕 The number of lines in the string _(Computed)_
 */
data class Source(
    val `𝜔`: String,
) {
    // Public properties
    val `𝚜` get() = `𝜔`.length
    val `𝚕` get() = `𝕀ₗ`.size

    // Positional system
    private val `𝕀ₗ`: Array<Index> = `𝜔`.mapIndexedNotNull { i, c ->
        if (c == '\n') i else null
    }.toTypedArray()

    /**
     * Returns the column of the character at the given index.
     * Column numbers start at 1, except for newlines, which are 0.
     * Guaranteed to be a valid column number.
     *
     * @param 𝚒 The index of the character
     * @return The column number of the character
     *
     * @throws IndexOutOfBoundsException if `𝚒` is out of bounds
     */
    fun `𝚡`(`𝚒`: Index): Index {
        if (`𝚒` !in 0..`𝜔`.length) throw IndexOutOfBoundsException()
        val `𝚒₀` = `𝕀ₗ`.bisect(`𝚒`) + 1
        return if (`𝚒₀` > 0) `𝚒` - `𝕀ₗ`[`𝚒₀` - 1] else `𝚒` + 1
    }

    /**
     * Returns the line number of the character at the given index.
     * Line numbers start at 0.
     * Guaranteed to be a valid line number.
     *
     * @param 𝚒 The index of the character
     * @return The line number of the character
     *
     * @throws IndexOutOfBoundsException if `𝚒` is out of bounds
     */
    fun `𝚢`(`𝚒`: Index): Index {
        if (`𝚒` !in 0..`𝜔`.length) throw IndexOutOfBoundsException()
        return `𝕀ₗ`.bisect(`𝚒`) + 1
    }

    /**
     * Returns the position of the character at the given index.
     * Guaranteed to be a valid position.
     *
     * @param 𝚒 The index of the character
     * @return The position of the character
     *
     * @throws IndexOutOfBoundsException if `𝚒` is out of bounds
     */
    fun `𝚙`(`𝚒`: Index) = Position(this, `𝚒`.also { if (it !in 0..`𝜔`.length) throw IndexOutOfBoundsException() })

    /**
     * Returns the index of the character at the given line and column.
     *
     * @param 𝚢 The line number of the character
     * @param 𝚡 The column number of the character
     * @return The index of the character
     *
     * @throws IndexOutOfBoundsException if `𝚢` or `𝚡` is out of bounds
     */
    fun `𝚒`(`𝚢`: Index, `𝚡`: Index): Index {
        if (`𝚢` == 0) return (`𝚡` - 1).also { if (it !in 0..`𝜔`.length) throw IndexOutOfBoundsException() }
        val `𝚒₀` = `𝕀ₗ`.getOrElse(`𝚢` - 1) { throw IndexOutOfBoundsException() } + `𝚡`
        return `𝚒₀`.also { if (it !in 0..`𝜔`.length) throw IndexOutOfBoundsException() }
    }

    // Operators
    /**
     * Returns the character at the given index.
     * Doesn't check if the index is out of bounds. _(Due to performance reasons)_
     */
    operator fun get(`𝚒`: Index) = `𝜔`[`𝚒`]

    /**
     * Returns the character at the given position.
     * Doesn't check if the position is out of bounds. _(Due to performance reasons)_
     */
    operator fun get(`𝚙`: Position) =
        if (`𝚙`.𝚂 == this) `𝜔`[`𝚙`.`𝚒`] else throw NoSuchElementException()
}