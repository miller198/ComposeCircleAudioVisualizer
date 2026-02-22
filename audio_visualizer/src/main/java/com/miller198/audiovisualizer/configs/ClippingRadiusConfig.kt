package com.miller198.audiovisualizer.configs

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Represents configuration options for determining the inner clipping radius of a visual element
 * (such as the inner circle in an audio visualization).
 *
 * Implementations define their own radius calculation logic via [calculateRadius].
 */
sealed interface ClippingRadiusConfig {
    /**
     * Calculates the clipping radius based on the canvas size.
     *
     * @param canvasSize The minimum dimension (width or height) of the canvas.
     * @return The calculated clipping radius in pixels.
     */
    fun calculateRadius(canvasSize: Int): Float

    /**
     * Fixed radius configuration using a specific dp value.
     *
     * @param dp Must be >= 0.0
     */
    data class Fixed(val dp: Dp) : ClippingRadiusConfig {
        init {
            require(dp.value >= 0f) {
                throw IllegalArgumentException("dp must be >= 0, but was ${dp.value}")
            }
        }

        override fun calculateRadius(canvasSize: Int): Float = dp.value
    }

    /**
     * Ratio-based radius configuration using a value between 0.0 and 1.0.
     * Represents a percentage of the canvas radius.
     *
     * @param ratio Must be in the range [0.0, 1.0]
     */
    data class Ratio(val ratio: Float) : ClippingRadiusConfig {
        init {
            require(ratio in 0f..1f) {
                throw IllegalArgumentException("ratio must be in the range [0, 1], but was $ratio")
            }
        }

        override fun calculateRadius(canvasSize: Int): Float = (canvasSize / 2) * ratio
    }

    /**
     * Default configuration: ratio = 1.0 (full canvas radius).
     * This means inner clipping is applied maximally.
     */
    data object FullClip : ClippingRadiusConfig {
        override fun calculateRadius(canvasSize: Int): Float = (canvasSize / 2).toFloat()
    }

    /**
     * No clipping applied: ratio = 0.0.
     * The content starts from the edge of the canvas, without an inner gap.
     */
    data object NoClip : ClippingRadiusConfig {
        override fun calculateRadius(canvasSize: Int): Float = 0f
    }

    /**
     * Small inner clipping applied: ratio = 0.3.
     * Leaves a small circular gap in the center.
     */
    data object Small : ClippingRadiusConfig {
        override fun calculateRadius(canvasSize: Int): Float = (canvasSize / 2) * 0.3f
    }

    /**
     * Medium inner clipping applied: ratio = 0.7.
     * Leaves a medium-sized circular gap in the center.
     */
    data object Medium : ClippingRadiusConfig {
        override fun calculateRadius(canvasSize: Int): Float = (canvasSize / 2) * 0.7f
    }
}
