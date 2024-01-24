package com.buttersus.blg

// This class provides better position tracking for strings
data class Source(
    val `𝜔`: String, // Immutable string (text)
) {
    // Computed properties
    private val `𝕀ₗ`: Array<Index> = `𝜔`.mapIndexedNotNull { i, c -> if (c == '\n') i else null }.toTypedArray()
    val `𝚜` get() = `𝜔`.length // Length of the string
    val `𝚕` get() = `𝕀ₗ`.size // Number of lines
    val positions get() = `𝜔`.indices.map { Position(this, it) } // Iterable of all positions

    // Additional properties
    fun `𝚢`(`𝚒`: Index): Index {
        if (`𝚒` !in 0..`𝜔`.length) throw IndexOutOfBoundsException()
        return `𝕀ₗ`.bisect(`𝚒`) + 1
    } // Line number (Note, starts at 0)

    fun `𝚡`(`𝚒`: Index): Index {
        if (`𝚒` !in 0..`𝜔`.length) throw IndexOutOfBoundsException()
        val `𝚒₀` = `𝕀ₗ`.bisect(`𝚒`) + 1
        return if (`𝚒₀` > 0) `𝚒` - `𝕀ₗ`[`𝚒₀` - 1] else `𝚒` + 1
    } // Column number (Note, starts at 1, or 0 if on a new line)

    fun `𝚒`(`𝚢`: Index, `𝚡`: Index): Index {
        return if (`𝚢` == 0) `𝚡` - 1 else `𝕀ₗ`[`𝚢` - 1] + `𝚡`
    } // Index at line and column

    operator fun get(`𝚒`: Index) = `𝜔`[`𝚒`] // Character at index
    operator fun get(`𝚙`: Position) =
        if (`𝚙`.𝚂 == this) `𝜔`[`𝚙`.`𝚒`] else throw NoSuchElementException() // Character at position

    fun `𝚙`(`𝚒`: Index) = Position(this, `𝚒`) // Position at index
}