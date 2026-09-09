package com.deming1.rapidrecall.ui

import androidx.compose.ui.text.AnnotatedString

sealed interface TextStep {
    data class StringIdStep(val stringId: Int): TextStep
    data class StringStep(val seqText: AnnotatedString): TextStep
}