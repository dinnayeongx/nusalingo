package com.example.nusalingo.feature.quiz

import org.junit.Test

class QuizAksaraTest {
    @Test(expected = IllegalArgumentException::class)
    fun requiresEveryAnswerTileInTheBank() {
        QuizAksara(
            bubble = "BASA JAWA",
            answer = listOf("ꦧ", "ꦱ", "ꦗ", "ꦮ"),
            bank = listOf("ꦧ", "ꦱ", "ꦗ")
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun requiresRepeatedAnswerTilesToAppearEnoughTimes() {
        QuizAksara(
            bubble = "RAMA MACA",
            answer = listOf("ꦩ", "ꦫ", "ꦩ", "ꦕ"),
            bank = listOf("ꦩ", "ꦫ", "ꦕ")
        )
    }
}
