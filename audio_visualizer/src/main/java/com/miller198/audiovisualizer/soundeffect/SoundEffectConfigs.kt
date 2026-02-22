package com.miller198.audiovisualizer.soundeffect

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import com.miller198.audiovisualizer.configs.ClippingRadiusConfig
import com.miller198.audiovisualizer.configs.GradientConfig
import kotlin.math.min

/**
 * CompositionLocal for providing [GradientConfig] to sound effect composables.
 */
val LocalGradientConfig = compositionLocalOf<GradientConfig> { GradientConfig.Default }

/**
 * CompositionLocal for providing [ClippingRadiusConfig] to sound effect composables.
 */
val LocalClippingRadiusConfig = compositionLocalOf<ClippingRadiusConfig> { ClippingRadiusConfig.FullClip }

/**
 * Configuration and utility object for controlling the sound effect drawing behavior.
 */
object SoundEffectConfigs {

    /** Ratio used to calculate the gradient radius relative to the maximum effect radius. */
    const val GRADIENT_RADIUS_RATIO = 0.8f

    /** Divisor used to determine the maximum height of the visual wave effect. */
    private const val EFFECT_HEIGHT_DIVISOR = 5f

    /**
     * Animated radius value for a radial gradient brush.
     * Only animates if [GradientConfig.useGradient] is true.
     *
     * @param gradientConfig The gradient configuration to use.
     * @param easing Easing function used in the animation.
     * @return Current animated radius value.
     */
    @Composable
    fun animatedGradientRadius(gradientConfig: GradientConfig, easing: Easing): Float {
        return if (gradientConfig.useGradient) {
            val transition = rememberInfiniteTransition(label = "GradientRadiusTransition")
            transition.animateFloat(
                initialValue = 0.01f,
                targetValue = 1.0f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = gradientConfig.duration, easing = easing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "AnimatedGradientRadius"
            ).value
        } else {
            1f
        }
    }

    /**
     * Called when the canvas size changes to update core dimensions like inner radius and maximum effect height.
     *
     * @param width New width of the canvas.
     * @param height New height of the canvas.
     * @param clippingRadiusConfig Configuration for calculating the clipping radius.
     * @param onRadiusCalculated Callback to provide the computed inner radius.
     * @param onMaxEffectHeightCalculated Callback to provide the computed max wave height.
     */
    internal fun onCanvasSizeChanged(
        width: Int,
        height: Int,
        clippingRadiusConfig: ClippingRadiusConfig,
        onRadiusCalculated: (Float) -> Unit,
        onMaxEffectHeightCalculated: (Float) -> Unit
    ) {
        val canvasSize = min(width, height)

        onRadiusCalculated(clippingRadiusConfig.calculateRadius(canvasSize))

        onMaxEffectHeightCalculated(canvasSize / EFFECT_HEIGHT_DIVISOR)
    }
}

/**
 * Constants related to drawing the sound bars (e.g., circular bars).
 */
internal object DrawSoundBarConstants {
    /** Default stroke width for drawing sound bars. */
    const val STROKE_WIDTH = 25f
}

/**
 * Constants related to drawing stroke-style sound waves.
 */
internal object DrawSoundWaveStrokeConstants {
    /** Default stroke width for drawing wave outlines. */
    const val STROKE_WIDTH = 6f
}
