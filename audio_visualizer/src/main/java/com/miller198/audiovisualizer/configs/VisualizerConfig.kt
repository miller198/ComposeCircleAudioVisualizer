package com.miller198.audiovisualizer.configs

import android.media.audiofx.Visualizer
import com.miller198.audiovisualizer.defaultPreProcessFftData
import com.miller198.audiovisualizer.defaultPreProcessWaveData

/**
 * Interface for configuring the audio visualizer.
 * Supports custom, FFT, and waveform capture configurations.
 */
sealed interface VisualizerConfig {

    /** Whether to use waveform (time domain) capture */
    val useWaveCapture: Boolean

    /** Whether to use FFT (frequency domain) capture */
    val useFftCapture: Boolean

    /** Buffer size for audio capture (e.g., 512, 1024) */
    val captureSize: Int

    /** Optional FFT data processing function */
    val processFftData: ((Visualizer, ByteArray, Int) -> List<Float>)?

    /** Optional waveform data processing function */
    val processWaveData: ((Visualizer, ByteArray, Int) -> List<Float>)?

    /** Fully custom configuration defined by the user. */
    data class Custom(
        override val useWaveCapture: Boolean,
        override val useFftCapture: Boolean,
        override val captureSize: Int,
        override val processFftData: ((Visualizer, ByteArray, Int) -> List<Float>)?,
        override val processWaveData: ((Visualizer, ByteArray, Int) -> List<Float>)?
    ) : VisualizerConfig

    /** FFT-based visualizer configuration. */
    data class FftCaptureConfig(
        val minFrequency: Int, // Minimum frequency to analyze (Hz)
        val maxFrequency: Int, // Maximum frequency to analyze (Hz)
        override val captureSize: Int,
        override val processFftData: (Visualizer, ByteArray, Int) -> List<Float>,
    ) : VisualizerConfig {
        override val useWaveCapture: Boolean = false
        override val useFftCapture: Boolean = true
        override val processWaveData: ((Visualizer, ByteArray, Int) -> List<Float>)? = null

        init {
            require(minFrequency >= 0 && maxFrequency >= 0) {
                throw IllegalArgumentException("Minimum and maximum frequencies must be non-negative.")
            }
            require(minFrequency <= maxFrequency) {
                throw IllegalArgumentException("Minimum frequency must be less than or equal to maximum frequency.")
            }
        }

        /** Default FFT configuration. */
        companion object {
            const val DEFAULT_MIN_FREQ = 40
            const val DEFAULT_MAX_FREQ = 4000
            const val DEFAULT_CAPTURE_SIZE = 1024

            val Default = FftCaptureConfig(
                minFrequency = DEFAULT_MIN_FREQ,
                maxFrequency = DEFAULT_MAX_FREQ,
                captureSize = DEFAULT_CAPTURE_SIZE,
                processFftData = { _, byteArray, samplingRate ->
                    // Default FFT preprocessing (custom implementation required)
                    defaultPreProcessFftData(byteArray, DEFAULT_CAPTURE_SIZE, DEFAULT_MIN_FREQ, DEFAULT_MAX_FREQ, samplingRate)
                }
            )
        }
    }

    /**
     * Waveform-based visualizer configuration.
     */
    data class WaveCaptureConfig(
        override val captureSize: Int,
        override val processWaveData: (Visualizer, ByteArray, Int) -> List<Float>
    ) : VisualizerConfig {
        override val useWaveCapture: Boolean = true
        override val useFftCapture: Boolean = false
        override val processFftData: ((Visualizer, ByteArray, Int) -> List<Float>)? = null

        companion object {
            const val DEFAULT_CAPTURE_SIZE = 512

            /** Default waveform configuration. */
            val Default = WaveCaptureConfig(
                captureSize = DEFAULT_CAPTURE_SIZE,
                processWaveData = { _, byteArray, _ ->
                    defaultPreProcessWaveData(byteArray)
                }
            )
        }
    }
}
