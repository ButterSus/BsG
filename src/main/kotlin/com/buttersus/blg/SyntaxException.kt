package com.buttersus.blg

import com.buttersus.gramutils.*

class SyntaxException(
    `𝚙ₛ`: Position,
    `𝚙ₑ`: Position,
    message: String,
) : SyntaxExceptionBase(`𝚙ₛ`, `𝚙ₑ`, message)