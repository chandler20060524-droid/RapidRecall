package com.deming1.rapidrecall

import androidx.compose.ui.text.AnnotatedString

/*
* Description:
* The AttemptData class packs all attempt data into one class so that it can be easily stored.
*
* Design Rationale:
* I designed this class so that the GameUiState class can directly store all the attempt data as
* multiple AttemptData instances inside one MutableList<AttemptData>; I also used data class so
* that I can call copy() method when updating its value.
*
* Outstanding Issue: None
* */
data class AttemptData(
    val time: String,
    val userInput: String,
    val sequence: AnnotatedString,
    val correctDigits: Int,
    val totalDigits: Int
)
