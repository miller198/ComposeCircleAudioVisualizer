package com.miller198.audiovisualizsample.presentation.screen.player

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.miller198.audiovisualizer.configs.GradientConfig
import com.miller198.audiovisualizer.configs.VisualizerConfig
import com.miller198.audiovisualizer.soundeffect.SoundEffect
import com.miller198.audiovisualizer.ui.CircleVisualizer
import com.miller198.audiovisualizsample.R

@Composable
internal fun CircleAlbumCover(
    soundEffect: SoundEffect,
    audioEffectColor: Color,
    audioSessionId: Int,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
    ) {
        CircleVisualizer(
            audioSessionId = audioSessionId,
            soundEffects = soundEffect,
            visualizerConfig = VisualizerConfig.FftCaptureConfig.Default,
            gradientConfig = GradientConfig.Default,
            color = audioEffectColor,
            modifier = modifier.align(Alignment.Center)
        )

        Image(
            painter = painterResource(R.drawable.sample_cover),
            contentDescription = "sample album cover",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
                .aspectRatio(1f)
                .clip(CircleShape)
                .align(Alignment.Center),
            contentScale = ContentScale.Crop
        )
    }
}
