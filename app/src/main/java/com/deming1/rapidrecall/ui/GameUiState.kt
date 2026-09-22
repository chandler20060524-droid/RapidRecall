package com.deming1.rapidrecall.ui

import com.deming1.rapidrecall.AttemptData

/*
* Description:
* The GameUiState stores all the essential UI data in the entire game, which can be accessed by any
* UI function at any instance.
*
* Design Rationale:
* I used data class so that I can call copy() method when updating its values.
*
* Outstanding Issue: None
* */
data class GameUiState(
    val currentSequence: String = "",
    val currentTime: String = "",
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