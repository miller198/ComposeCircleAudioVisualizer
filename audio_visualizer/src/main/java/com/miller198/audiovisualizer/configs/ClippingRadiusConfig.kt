package com.miller198.audiovisualizer.configs

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Represents configuration options for determining the inner clipping radius of a visual element
 * (such as the inner circle in an audio visualization).
 *
 * Implementations specify either a fixed radius in Dp, a relative ratio of the canvas radius,
 * or a default value.
 *
 * @property dp The fixed radius in density-independent pixels (Dp). If greater than 0, this takes priority over [ratio].
 * @property ratio A relative ratio (0.0 to 1.0) used when [dp] is zero. Represents a proportion of the canvas radius.
 */
sealed interface ClippingRadiusConfig {
    val dp: Dp
    val ratio: Float

    /**
     * Fixed radius configuration using a specific dp value.
     * If this is used, the ratio is ignored.
     *
     * @param dp Must be >= 0.0
     */
    data class Fixed(override val dp: Dp) : ClippingRadiusConfig {
        override val ratio: Float = 0f

        init {
            require(dp.value >= 0f) {
                throw IllegalArgumentException("dp must be >= 0, but was ${dp.value}")
            }
        }
    }

    /**
     * Ratio-based radius configuration using a value between 0.0 and 1.0.
     * Represents a percentage of the canvas radius.
     *
     * @param ratio Must be in the range [0.0, 1.0]
     */
    data class Ratio(override val ratio: Float) : ClippingRadiusConfig {
        override val dp: Dp = 0.dp

        init {
            require(ratio in 0f..1f) {
                throw IllegalArgumentException("ratio must be in the range [0, 1], but was $ratio")
            }
        }
    }

    /**
     * Default configuration: ratio = 1.0 (full canvas radius), dp = 0.dp.
     * This means inner clipping is applied maximally.
     */
    data object FullClip : ClippingRadiusConfig {
        override val dp: Dp = 0.dp
        override val ratio: Float = 1f
    }

    /**
     * No clipping applied: ratio = 0.0, dp = 0.dp.
     * The content starts from the edge of the canvas, without an inner gap.
     */
    data object NoClip : ClippingRadiusConfig {
        override val dp: Dp = 0.dp
        override val ratio: Float = 0f
    }

    /**
     * Small inner clipping applied: ratio = 0.3, dp = 0.dp.
     * Leaves a small circular gap in the center.
     */
    data object Small : ClippingRadiusConfig {
        override val dp: Dp = 0.dp
        override val ratio: Float = 0.3f
    }

    /**
     * Medium inner clipping applied: ratio = 0.7, dp = 0.dp.
     * Leaves a medium-sized circular gap in the center.
     */
    data object Medium : ClippingRadiusConfig {
        override val dp: Dp = 0.dp
        override val ratio: Float = 0.7f
    }
}
