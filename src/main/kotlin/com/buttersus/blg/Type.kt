package com.buttersus.blg

/**
 * Regex token type
 */
enum class Type {
    NAME, CNAME, OPERATOR,
    INDENT, DEDENT, EOF, NEWLINE,
    SINGLE_STRING, DOUBLE_STRING,
}