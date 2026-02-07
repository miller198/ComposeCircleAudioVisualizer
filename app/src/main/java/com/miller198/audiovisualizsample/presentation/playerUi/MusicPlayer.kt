package com.miller198.audiovisualizsample.presentation.playerUi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.miller198.audiovisualizsample.presentation.screen.player.Constants.DEFAULT_PADDING
import com.miller198.audiovisualizsample.presentation.PlayerUiState

@Composable
fun MusicPlayer(
    playerState: PlayerUiState,
    onSeekChanged: (Long) -> Unit,
    onReplayForwardClick: (Boolean) -> Unit,
    onPauseToggle: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = DEFAULT_PADDING)
            .background(
                color = Color(0xFF353535),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 30.dp, vertical = DEFAULT_PADDING),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        PlayBar(
            duration = playerState.duration,
            currentTime = playerState.currentPosition,
            bufferPercentage = playerState.bufferPercentage,
            isPlaying = playerState.isPlaying,
            onSeekChanged = { timeMs ->
                onSeekChanged(timeMs.toLong())
            },
            onReplayClick = {
                onReplayForwardClick(false)
            },
            onPauseToggle = {
                onPauseToggle()
            },
            onForwardClick = {
                onReplayForwardClick(true)
            },
        )
    }
}
