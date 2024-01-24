package com.buttersus

import com.buttersus.blg.*

fun main() {
    // resources/main.txt
    val `𝚂` = RegexLexer::class.java.getResource("/main.blg")
        ?.readText()?.toSource() ?: throw Exception("Could not read file")
    val `𝕃` = RegexLexer()(`𝚂`).tokenize()
//    val `ℙ` = RegexParser()(`𝕃`)
    for (token in `𝕃`) {
        println(token)
    }
}
