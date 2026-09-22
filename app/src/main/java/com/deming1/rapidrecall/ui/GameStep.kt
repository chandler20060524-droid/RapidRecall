package com.deming1.rapidrecall.ui

import androidx.compose.ui.text.AnnotatedString

/*
* Description:
* The TextStep sealed interface works as a "glue" that brings data class StringIdStep and
* StringStep under one parent TextStep, so that it can be stored in Flow<TextStep> while
* remaining mutually exclusive (otherwise I cannot store Int and AnnotatedString type into one
* single variable).
*
* Design Rationale:
* I designed TextStep seal interface in order to fit the two data classes into a same flow, which
* is used to emit the flashing texts and sequence during game play; however, before and after the
* flashing numerical sequence, the texts are represented in Int type (string IDs) while the flashing
* sequences and final colored result are represented by AnnotatedString type, I cannot emit all
* of them in a same flow without the help of sealed class.
*
* Outstanding Issue: None
* */
sealed interface TextStep {
    data class StringIdStep(val stringId: Int): TextStep
    data class StringStep(val seqText: AnnotatedString): TextStep
}