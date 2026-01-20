package com.miller198.audiovisualizer.configs

import androidx.compose.ui.graphics.Color

/**
 * Interface for gradient animation configuration.
 */
sealed interface GradientConfig {
    val useGradient: Boolean      // Whether gradient is enabled
    val duration: Int             // Animation duration in milliseconds
    val color: Color              // Base color of the gradient

    /** Default gradient config (enabled). */
    data object Default : GradientConfig {
        override val useGradient: Boolean = true
        override val duration: Int = DEFAULT_GRADIENT_DURATION
        override val color: Color = Color.Companion.White
    }

    /** Enabled gradient config with custom options. */
    data class Enabled(
        override val duration: Int,
        override val color: Color,
    ) : GradientConfig {
        override val useGradient: Boolean = true
    }

    /** Disabled gradient config (no animation, transparent color) */
    data object Disabled : GradientConfig {
        override val useGradient: Boolean = false
        override val duration: Int = 0
        override val color: Color = Color.Companion.Transparent
    }

    companion object {
        const val DEFAULT_GRADIENT_DURATION = 2500
    }
}
