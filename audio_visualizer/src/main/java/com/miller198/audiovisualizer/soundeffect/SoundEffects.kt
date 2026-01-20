package com.miller198.audiovisualizer.soundeffect

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
/**
 * Sound visualization effects represented as a sealed hierarchy.
 * This allows each effect to be an independent object or class, offering more flexibility than Enum.
 */
sealed interface SoundEffect {

    @Composable
    fun Draw(
        audioData: List<Float>,
        color: Color,
        modifier: Modifier
    )

    /** No effect. Renders nothing. */
    data object None : SoundEffect {
        @Composable
        override fun Draw(audioData: List<Float>, color: Color, modifier: Modifier) {
            // 빈 공간 (No-op)
        }
    }

    /** A vertical bar graph representation. */
    data object Bar : SoundEffect {
        @Composable
        override fun Draw(audioData: List<Float>, color: Color, modifier: Modifier) {
            SoundEffectBar(audioData, color, modifier)
        }
    }

    /** A waveform rendered using stroke (outline only). */
    data object WaveStroke : SoundEffect {
        @Composable
        override fun Draw(audioData: List<Float>, color: Color, modifier: Modifier) {
            SoundEffectWaveStroke(audioData, color, modifier)
        }
    }

    /** A waveform rendered as a filled shape. */
    data object WaveFill : SoundEffect {
        @Composable
        override fun Draw(audioData: List<Float>, color: Color, modifier: Modifier) {
            SoundEffectWaveFill(audioData, color, modifier)
        }
    }

    // UI에서 선택지(목록)로 보여줄 때 사용하기 위해 모아둔 리스트
    companion object {
        val entries = listOf(None, Bar, WaveStroke, WaveFill)
    }
}
