package com.miller198.audiovisualizsample.presentation.playerUi

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Forward5
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay5
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.miller198.audiovisualizsample.ui.theme.ComposeSoundVisualizerProjectTheme

@Composable
internal fun PlayerControls(
    isPlaying: Boolean,
    onReplayClick: () -> Unit,
    onPauseToggle: () -> Unit,
    onForwardClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier,
    ) {
        Box(
            modifier = modifier
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = onReplayClick
            ) {
                Icon(
                    imageVector = Icons.Default.Replay5,
                    contentDescription = "play backward",
                    modifier = Modifier.size(64.dp),
                    tint = White
                )
            }
        }
        Box(
            modifier = modifier
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = onPauseToggle
            ) {
                Icon(
                    imageVector = if (isPlaying) {
                        Icons.Default.Pause
                    } else {
                        Icons.Default.PlayArrow
                    },
                    contentDescription = "play/pause button",
                    modifier = Modifier.size(64.dp),
                    tint = White
                )
            }
        }

        Box(
            modifier = modifier
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = onForwardClick
            ) {
                Icon(
                    imageVector = Icons.Default.Forward5,
                    contentDescription = "play forward",
                    modifier = Modifier.size(64.dp),
                    tint = White
                )
            }
        }
    }
}

@Preview
@Composable
private fun PlayerControlsPreview() {
    ComposeSoundVisualizerProjectTheme {
        PlayerControls(
            isPlaying = true,
            onReplayClick = {},
            onPauseToggle = {},
            onForwardClick = {})
    }
}
