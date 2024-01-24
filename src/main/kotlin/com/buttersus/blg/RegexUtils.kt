package com.buttersus.blg

// Index is an integer
typealias Index = Int

// Convert a string to a source
fun String.toSource(): Source = Source(this)

// Convert a string to a position
fun String.toDeltaPosition(`𝚂`: Source): Position = Position(`𝚂`, this.length)

// returns max element index that less or equal to element
fun <T : Comparable<T>> Array<T>.bisect(element: T): Index {
    var left = 0
    var right = lastIndex
    while (left <= right) {
        val mid = (left + right) / 2
        when {
            this[mid] < element -> left = mid + 1
            this[mid] > element -> right = mid - 1
            else -> return mid
        }
    }
    return right
}

// Shortcut to avoid writing `𝚂`.`𝚙`.`𝚒`
fun Regex.matchAt(`𝚙`: Position): MatchResult? = this.matchAt(`𝚙`.`𝚂`.`𝜔`, `𝚙`.`𝚒`)
fun RegexLexer.newToken(`𝚃`: Type, `𝚟`: String): Token = Token(this, `𝚃`, `𝚟`).also {
    this.`𝚙` += `𝚟`.toDeltaPosition(this.`𝚂`)
}

fun Token.wrap(): Node.Wrapper = Node.Wrapper(this)
fun Node.Wrapper.unwrap(): Token = this.`𝚝`
fun List<Node>.toGroup(): Node.Group = Node.Group(*this.toTypedArray())
fun Node.toPretty(indent: Int = 2): String {
    when (this) {
        is Node.Group -> {
            val content: List<String> = this.map { it.toPretty(indent) }
            val lines = content[0].count { it == '\n' }
            return when {
                this.size == 0 -> "{ ∅ }"
                this.size == 1 && lines == 0 -> "{ 1 -> ${content[0]} }"
                else -> {
                    val offset = " ".repeat(indent)
                    val inner = content
                        .mapIndexed { i, s -> "${i + 1} -> $s" }
                        .joinToString(",\n")
                        .prependIndent(offset)
                    "{\n$inner\n}"
                }
            }
        }
        is Node.Catalog -> {
            val content: List<String> = this.map { it.toPretty(indent) }
            val lines = content[0].count { it == '\n' }
            return when {
                this.size == 0 -> "[ ∅ ]"
                this.size == 1 && lines == 0 -> "[ ${content[0]} ]"
                else -> {
                    val offset = " ".repeat(indent)
                    val inner = content
                        .joinToString(",\n")
                        .prependIndent(offset)
                    "[\n$inner\n]"
                }
            }
        }
        is Node.Wrapper -> if (this.`𝚝`.`𝚃` != Type.EOF) {
            return "⟨${this.`𝚝`.`𝚟`}⟩"
        } else return "File end..."
        is Node.Empty -> return "ε"
        else -> {
            val inner = this.properties
                .map { (k, v) -> "$k: ${v.toPretty()}" }
                .joinToString(",\n")
                .prependIndent(" ".repeat(indent))
            if (this.parameters.isEmpty())
                return "${this::class.simpleName} {\n$inner\n}"
            val params = this.parameters
                .map { (k, v) -> "$k: $v" }
                .joinToString(", ")
                .prependIndent(" ".repeat(indent) + "$")
            return "${this::class.simpleName} {\n$params\n$inner\n}"
        }
    }
}
