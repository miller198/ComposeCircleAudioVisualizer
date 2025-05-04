package com.miller198.audiovisualizer

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

class WaveDataProcessor(rawWaveBytes: ByteArray) {
    // The Fft data applied pre-processing function
    private var processedData: List<Float> = emptyList()

    init {
        processedData = convertToUnsignedBytes(rawWaveBytes)
    }

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
        processedData = if (max - min <= 1f) List(processedData.size) { 0f } else processedData.map { (it - min) / (max - min) }

        return this
    }

    fun downSample(outputSize: Int): WaveDataProcessor {
        val factor = processedData.size / outputSize.toFloat()
        processedData = List(outputSize) { i ->
            processedData[(i * factor).toInt()]
        }

        return this
    }

    fun maxPool(blockSize: Int): WaveDataProcessor {
        val outputSize = processedData.size / blockSize
        processedData = List(outputSize) { i ->
            val block = processedData.slice(i * blockSize until (i + 1) * blockSize)
            block.maxByOrNull { abs(it) } ?: 0.0f
        }

        return this
    }

    fun averagePool(blockSize: Int): WaveDataProcessor {
        val outputSize = processedData.size / blockSize
        processedData = List(outputSize) { i ->
            val block = processedData.slice(i * blockSize until (i + 1) * blockSize)
            (block.sum() / block.size)
        }

        return this
    }

    fun result(): List<Float> = processedData
}


fun defaultPreProcessWaveData(rawWaveBytes: ByteArray): List<Float> =
    WaveDataProcessor(rawWaveBytes)
        .averagePool(5)
        .normalizeByZScore()
        .normalize()
        .result()
