package com.buttersus.blg

class SyntaxException(
    `𝚙₁`: Position,
    `𝚙₂`: Position,
    message: String,
) : Exception(
    """
    |Syntax error: $message
    """.trimMargin()
)