package com.buttersus.blg

/**
 * Regex token type.
 * These names are used when logging.
 *
 * Note: EOF is necessary for the parser to know when to stop;
 *       Don't name tokens longer than 10 characters. (For logging)
 */
enum class Type {
    NAME, CNAME, OPERATOR,
    INDENT, DEDENT, EOF, NEWLINE,
    S_STR, D_STR;
}