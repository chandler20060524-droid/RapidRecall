package com.deming1.rapidrecall.ui

import com.deming1.rapidrecall.AttemptData

/*
* This class is designed to store */
data class GameUiState(
    val currentSequence: String = "",
    val correct: Boolean = false,
    val wrong: Boolean = false,
    val currentCorrectDigits: Int = 0,
    val currentDigits: Int = 0,
    val totalCorrectDigits: Float = 0.0f,
    val totalDigits: Int = 0,
    val previousAttempts: List<AttemptData> = mutableListOf(),
    val correctAttempts: Int = 0,
    val totalAttempts: Int = 0
)