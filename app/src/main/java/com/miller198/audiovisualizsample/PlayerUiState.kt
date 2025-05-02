package com.miller198.audiovisualizsample

data class PlayerUiState(
    val isReady: Boolean = true,
    val isPlaying: Boolean = false,
    val duration: Long = 30_000L,
    val currentPosition: Long = 0L,
    val bufferPercentage: Int = 0,
) {
    companion object {
        val PLAYER_STATE_INITIAL = PlayerUiState(isReady = false)
        val PLAYER_STATE_STOP = PlayerUiState(isReady = false, isPlaying = false)
    }
}
