package com.deming1.rapidrecall

import androidx.compose.ui.text.AnnotatedString

data class AttemptData(
    val time: String,
    val userInput: String,
    val sequence: AnnotatedString,
    val correctDigits: Int,
    val totalDigits: Int
)
