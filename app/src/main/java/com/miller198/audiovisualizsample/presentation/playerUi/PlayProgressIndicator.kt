package com.miller198.audiovisualizsample.presentation.playerUi

import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@Composable
internal fun PlayProgressIndicator(
    modifier: Modifier = Modifier,
    currentTime: Float,
    bufferPercentage: Float,
    duration: Float,
    onSeekChanged: (Float) -> Unit
) {
    val updatedDuration by rememberUpdatedState(duration)

    Box(modifier = modifier) {
        LinearProgressIndicator(
            progress = { bufferPercentage / 100f },
            modifier = Modifier.fillMaxWidth(),
            color = DarkGray,
            trackColor = DarkGray,
            strokeCap = StrokeCap.Butt,
        )

        LinearProgressIndicator(
            progress = { currentTime / duration },
            modifier = Modifier.fillMaxWidth(),
            color = White,
            trackColor = Color.Transparent,
            strokeCap = StrokeCap.Butt
        )

        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(3.dp)
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        Log.d("PlayProgressIndicator", "duration: $updatedDuration")
                        val newTime = offset.x / size.width * updatedDuration
                        onSeekChanged(newTime)
                    }
                }
        )
    }
}
