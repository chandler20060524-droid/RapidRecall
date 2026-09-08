package com.deming1.rapidrecall

sealed interface GameStep {
    data class PreGameStep(val stringId: Int): GameStep
    data class FlashSequenceStep(val seqText: String): GameStep
}