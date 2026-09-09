package com.deming1.rapidrecall.ui

import com.deming1.rapidrecall.AttemptData

data class GameUiState(
    val currentSequence: String = "",
    val correct: Boolean = false,
    val wrong: Boolean = false,
    val currentCorrectDigits: Int = 0,
    val currentDigits: Int = 0,
    val totalCorrectDigits: Int = 0,
    val totalDigits: Int = 0,
    val previousAttempts: List<AttemptData> = mutableListOf(),
)