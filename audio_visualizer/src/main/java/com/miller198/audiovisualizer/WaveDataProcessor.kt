package com.miller198.audiovisualizer

import android.media.audiofx.Visualizer
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Class for processing wave form data captured from [Visualizer.OnDataCaptureListener.onWaveFormDataCapture].
 *
 * @param rawWaveBytes The raw waveform data as a byte array.
 */
class WaveDataProcessor(rawWaveBytes: ByteArray) {
    // The waveform data after pre-processing
    private var processedData: List<Float> = emptyList()

    init {
        processedData = convertToUnsignedBytes(rawWaveBytes)
    }

    /**
     * Converts signed 8-bit PCM waveform data to an unsigned float representation.
     *
     * Raw wave data from Visualizer is in the range [-128, 127]. This method converts
     * it to [0, 255] by adding 128 to each byte, and then converts it to Float.
     *
     * @param input The raw wave data as a byte array.
     * @return A list of Float values in the range [0, 255].
     */
    fun convertToUnsignedBytes(input: ByteArray): List<Float> {
        return input.map { (it.toInt() + 128).toFloat() }
    }

    /**
     * Applies Z-Score normalization to the given audio data.
     *
     * This method standardizes the data to have zero mean and unit variance,
     * making it easier to compare values across different datasets or features.
     *
     * @return A list of normalized values centered around 0 with unit variance.
     *         If input is empty or standard deviation is zero, returns a list of zeros.
     */
    fun normalizeByZScore(): WaveDataProcessor {
        if (processedData.isEmpty()) return this

        val mean = processedData.average().toFloat()
        val stdDev = sqrt(processedData.map { (it - mean).pow(2) }.average()).toFloat()

        processedData = if (stdDev == 0f || stdDev.isNaN()) {
            List(processedData.size) { 0f }
        } else {
            processedData.map { (it - mean) / stdDev }
        }

        return this
    }

    /**
     * Applies Max-Min Normalization to the given audio data.
     *
     * @return A list of normalized values between 0 and 1.
     */
    fun normalize(): WaveDataProcessor {
        val max = processedData.maxOrNull() ?: 1f
        val min = processedData.minOrNull() ?: 0f
        processedData = if ((max - min) <= 1f) {
            List(processedData.size) { 0f }
        } else {
            processedData.map { (it - min) / (max - min) }
        }

        return this
    }

    /**
     * Reduces the size of the waveform data by selecting evenly spaced samples.
     *
     * @param outputSize The desired number of output samples.
     * @return The [WaveDataProcessor] instance for method chaining.
     */
    fun downSample(outputSize: Int): WaveDataProcessor {
        val factor = processedData.size / outputSize.toFloat()
        processedData = List(outputSize) { i ->
            processedData[(i * factor).toInt()]
        }

        return this
    }

    /**
     * Applies max pooling to the waveform data.
     *
     * Selects the maximum absolute value in each block to represent that block.
     *
     * @param blockSize The size of each block for pooling.
     * @return The [WaveDataProcessor] instance for method chaining.
     */
    fun maxPool(blockSize: Int): WaveDataProcessor {
        val outputSize = processedData.size / blockSize
        processedData = List(outputSize) { i ->
            val block = processedData.slice(i * blockSize until (i + 1) * blockSize)
            block.maxByOrNull { abs(it) } ?: 0.0f
        }

        return this
    }

    /**
     * Applies average pooling to the waveform data.
     *
     * Calculates the mean value of each block.
     *
     * @param blockSize The size of each block for pooling.
     * @return The [WaveDataProcessor] instance for method chaining.
     */
    fun averagePool(blockSize: Int): WaveDataProcessor {
        val outputSize = processedData.size / blockSize
        processedData = List(outputSize) { i ->
            val block = processedData.slice(i * blockSize until (i + 1) * blockSize)
            block.sum() / block.size
        }

        return this
    }

    /**
     * Returns the final processed waveform data.
     *
     * @return A list of processed Float values.
     */
    fun result(): List<Float> = processedData
}

/**
 * Applies a default sequence of pre-processing steps to raw waveform data.
 *
 * This includes average pooling, Z-score normalization, and min-max normalization.
 * The steps are designed to reduce noise and scale the waveform data for further use.
 *
 * @param rawWaveBytes The raw waveform byte array from [Visualizer.OnDataCaptureListener].
 * @return A list of processed Float values.
 */
fun defaultPreProcessWaveData(rawWaveBytes: ByteArray): List<Float> =
    WaveDataProcessor(rawWaveBytes)
        .averagePool(5)
        .normalizeByZScore()
        .normalize()
        .result()
