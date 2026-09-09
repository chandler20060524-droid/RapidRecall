package com.deming1.rapidrecall.ui

data class GameUiState(
    val currentSequence: String = "",
    val correct: Boolean = false,
    val wrong: Boolean = false,
    val currentCorrectDigits: Int = 0,
    val currentDigits: Int = 0,
    val totalCorrectDigits: Int = 0,
    val totalDigits: Int = 0,
    val previousCorrectDigits: MutableList<Int> = mutableListOf(),
    val previousDigits: MutableList<Int> = mutableListOf()
)