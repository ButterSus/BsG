@file:Suppress("MemberVisibilityCanBePrivate")

package com.buttersus.blg

class RegexLexer {
    // Attributes
    internal lateinit var `𝚂`: Source
    internal lateinit var `𝚙`: Position

    operator fun invoke(`𝚂`: Source): RegexLexer {
        this.𝚂 = 𝚂
        this.`𝚙` = Position(𝚂)
        return this
    }

    private val indentStack = mutableListOf(0)

    // Methods
    fun tokenize(): Iterator<Token> = iterator {
        while (`𝚙`.isNotAtEnd()) {
            Regex("""[^\S\r\n]*""").matchAt(`𝚙`)!!
                .also { this@RegexLexer.`𝚙` += it.value.length }
            if (Regex("""\r?\n(?:[^\S\r\n]*\r?\n)*""").matchAt(`𝚙`)
                    ?.also { yield(newToken(Type.NEWLINE, it.value)) } != null
            ) {
                // indent, dedent handling
                Regex("""[^\S\r\n]*""").matchAt(`𝚙`)!!
                    .also {
                        val newIndentLevel = it.value.length
                        val currentIndentLevel = indentStack.last()

                        when {
                            newIndentLevel > currentIndentLevel -> {
                                indentStack.add(newIndentLevel)
                                yield(newToken(Type.INDENT, it.value))
                            }

                            newIndentLevel < currentIndentLevel -> {
                                while (newIndentLevel < indentStack.last()) {
                                    indentStack.removeAt(indentStack.lastIndex)
                                    yield(newToken(Type.DEDENT, ""))
                                }
                            }
                        }
                    }
                continue
            }
            if (Regex("""\p{L}+(?!-)\b""").matchAt(`𝚙`)
                    ?.also { yield(newToken(Type.NAME, it.value)) } != null
            ) continue
            if (Regex("""[\p{L}0-9-]+\b""").matchAt(`𝚙`)
                    ?.also { yield(newToken(Type.CNAME, it.value)) } != null
            ) continue
            if (Regex("""'.*?'""", RegexOption.DOT_MATCHES_ALL).matchAt(`𝚙`)
                    ?.also { yield(newToken(Type.SINGLE_STRING, it.value)) } != null
            ) continue
            if (Regex("""".*?"""", RegexOption.DOT_MATCHES_ALL).matchAt(`𝚙`)
                    ?.also { yield(newToken(Type.DOUBLE_STRING, it.value)) } != null
            ) continue
            if (Regex("""[:.<>{}()=$+*?!|,]|=>|->|\?!""", RegexOption.DOT_MATCHES_ALL).matchAt(`𝚙`)
                    ?.also { yield(newToken(Type.OPERATOR, it.value)) } != null
            ) continue
            throw Exception("Unexpected character at $`𝚙` -> ${`𝚙`.`𝚊`}")
        }; yield(newToken(Type.EOF, ""))
    }
}
