# ComposeCircleAudioVisualizer

A Jetpack Compose library for creating beautiful circular audio visualizations in Android applications.

![Min SDK](https://img.shields.io/badge/minSdk-26-green)
![Compile SDK](https://img.shields.io/badge/compileSdk-35-blue)

## Features

- **Circular Audio Visualization**: Display audio data in a circular format
- **Multiple Sound Effects**: Bar, WaveStroke, WaveFill visualizations (sealed interface)
- **FFT & Waveform Support**: Process both frequency domain (FFT) and time domain (Waveform) data
- **Gradient Animation**: Optional animated gradient effects
- **Customizable Clipping Radius**: Control the inner radius of the visualization
- **Data Processing Utilities**: Built-in FFT and Waveform data processors with normalization, filtering, and scaling

## Installation

### Step 1. Add JitPack repository

Add it in your root `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

### Step 2. Add the dependency

Add it in your module `build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.github.miller198:ComposeCircleAudioVisualizer:1.0.0")
}
```

### Step 3. Add RECORD_AUDIO permission

Add the following permission to your `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.RECORD_AUDIO" />
```

> **Note**: You must request this permission at runtime for Android 6.0 (API 23) and above.

---

## Basic Usage

### CircleVisualizer Composable

The main entry point is the `CircleVisualizer` composable:

```kotlin
@Composable
fun CircleVisualizer(
    audioSessionId: Int,
    soundEffects: SoundEffect,
    visualizerConfig: VisualizerConfig,
    modifier: Modifier = Modifier,
    color: Color = White,
    clippingRadiusConfig: ClippingRadiusConfig = ClippingRadiusConfig.FullClip,
    gradientConfig: GradientConfig = GradientConfig.Default,
)
```

### Simple Example

```kotlin
// Get audio session ID from MediaPlayer or ExoPlayer
val audioSessionId = mediaPlayer.audioSessionId

CircleVisualizer(
    audioSessionId = audioSessionId,
    soundEffects = SoundEffect.Bar,
    visualizerConfig = VisualizerConfig.FftCaptureConfig.Default,
    modifier = Modifier.size(300.dp),
    color = Color.Cyan
)
```

---

## Sound Effects

The library provides four types of sound visualization effects through a sealed interface:

| Effect | Description |
|--------|-------------|
| `SoundEffect.None` | No visualization |
| `SoundEffect.Bar` | Radial bar graph extending from center |
| `SoundEffect.WaveStroke` | Smooth waveform outline using Catmull-Rom splines |
| `SoundEffect.WaveFill` | Filled smooth waveform shape |

### Example

```kotlin
// Bar visualization
CircleVisualizer(
    audioSessionId = audioSessionId,
    soundEffects = SoundEffect.Bar,
    visualizerConfig = VisualizerConfig.FftCaptureConfig.Default,
)

// Filled wave visualization
CircleVisualizer(
    audioSessionId = audioSessionId,
    soundEffects = SoundEffect.WaveFill,
    visualizerConfig = VisualizerConfig.WaveCaptureConfig.Default,
)
```

---

## Visualizer Configuration

### VisualizerConfig

Sealed interface for configuring audio capture mode:

#### FftCaptureConfig (Frequency Domain)

```kotlin
// Default FFT configuration
val config = VisualizerConfig.FftCaptureConfig.Default

// Custom FFT configuration
val customConfig = VisualizerConfig.FftCaptureConfig(
    minFrequency = 40,      // Minimum frequency (Hz)
    maxFrequency = 4000,    // Maximum frequency (Hz)
    captureSize = 1024,     // FFT window size (must be power of 2)
    processFftData = { visualizer, bytes, samplingRate ->
        // Custom FFT processing
        defaultPreProcessFftData(bytes, 1024, 40, 4000, samplingRate)
    }
)
```

#### WaveCaptureConfig (Time Domain)

```kotlin
// Default waveform configuration
val config = VisualizerConfig.WaveCaptureConfig.Default

// Custom waveform configuration
val customConfig = VisualizerConfig.WaveCaptureConfig(
    captureSize = 512,
    processWaveData = { visualizer, bytes, samplingRate ->
        // Custom waveform processing
        defaultPreProcessWaveData(bytes)
    }
)
```

#### Custom Configuration

```kotlin
val customConfig = VisualizerConfig.Custom(
    useWaveCapture = true,
    useFftCapture = false,
    captureSize = 512,
    processWaveData = { _, bytes, _ ->
        WaveDataProcessor(bytes)
            .downSample(64)
            .normalize()
            .result()
    },
    processFftData = null
)
```

---

## Gradient Configuration

Control the animated gradient effect:

```kotlin
// Default (enabled with white gradient)
gradientConfig = GradientConfig.Default

// Custom gradient
gradientConfig = GradientConfig.Enabled(
    duration = 3000,        // Animation duration in ms
    color = Color.Yellow    // Gradient color
)

// Disabled
gradientConfig = GradientConfig.Disabled
```

---

## Clipping Radius Configuration

Control the inner radius of the circular visualization:

| Config | Description |
|--------|-------------|
| `ClippingRadiusConfig.NoClip` | No inner gap (ratio = 0.0) |
| `ClippingRadiusConfig.Small` | Small inner gap (ratio = 0.3) |
| `ClippingRadiusConfig.Medium` | Medium inner gap (ratio = 0.7) |
| `ClippingRadiusConfig.FullClip` | Full inner clipping (ratio = 1.0) |
| `ClippingRadiusConfig.Fixed(dp)` | Fixed radius in dp |
| `ClippingRadiusConfig.Ratio(ratio)` | Custom ratio (0.0 ~ 1.0) |

### Example

```kotlin
// Use ratio-based clipping
CircleVisualizer(
    audioSessionId = audioSessionId,
    soundEffects = SoundEffect.WaveFill,
    visualizerConfig = VisualizerConfig.FftCaptureConfig.Default,
    clippingRadiusConfig = ClippingRadiusConfig.Ratio(0.5f)
)

// Use fixed dp clipping
CircleVisualizer(
    audioSessionId = audioSessionId,
    soundEffects = SoundEffect.Bar,
    visualizerConfig = VisualizerConfig.FftCaptureConfig.Default,
    clippingRadiusConfig = ClippingRadiusConfig.Fixed(100.dp)
)
```

---

## Data Processing

### FftDataProcessor

Chain-able processor for FFT data with various transformations:

```kotlin
val processedData = FftDataProcessor(rawFftBytes)
    .filterFrequency(samplingRate, captureSize, minFreq = 40, maxFreq = 4000)
    .scaleFrequencies(listOf(
        FrequencyScale(0.0f..0.3f, 1.2f),  // Boost low frequencies
        FrequencyScale(0.3f..0.7f, 1.0f),  // Normal mid frequencies
        FrequencyScale(0.7f..1.0f, 0.8f)   // Reduce high frequencies
    ))
    .applyLogScale(base = 10f)
    .compressDynamicRangeRoot()
    .normalizeByZScore()
    .normalize()
    .result()
```

#### Available Methods

| Method | Description |
|--------|-------------|
| `filterFrequency()` | Filter to specific frequency range |
| `scaleFrequencies()` | Apply weights to frequency ranges |
| `applyLogScale()` | Logarithmic scale transformation |
| `compressDynamicRangeRoot()` | Square root compression |
| `normalizeByZScore()` | Z-Score normalization (zero mean, unit variance) |
| `normalize()` | Min-Max normalization (0 to 1) |

### WaveDataProcessor

Chain-able processor for waveform data:

```kotlin
val processedData = WaveDataProcessor(rawWaveBytes)
    .downSample(64)           // Reduce to 64 samples
    .maxPool(4)               // Max pooling with block size 4
    .averagePool(2)           // Average pooling with block size 2
    .normalizeByZScore()
    .normalize()
    .result()
```

#### Available Methods

| Method | Description |
|--------|-------------|
| `downSample()` | Reduce sample count by selecting evenly spaced samples |
| `maxPool()` | Max pooling (select max in each block) |
| `averagePool()` | Average pooling (mean of each block) |
| `normalizeByZScore()` | Z-Score normalization |
| `normalize()` | Min-Max normalization |

---

## Complete Example

```kotlin
@Composable
fun AudioPlayerWithVisualizer(
    audioSessionId: Int
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Album art or background
        Image(
            painter = painterResource(R.drawable.album_cover),
            contentDescription = null,
            modifier = Modifier.size(200.dp).clip(CircleShape)
        )

        // Visualizer around the album art
        CircleVisualizer(
            audioSessionId = audioSessionId,
            soundEffects = SoundEffect.WaveFill,
            visualizerConfig = VisualizerConfig.FftCaptureConfig(
                minFrequency = 60,
                maxFrequency = 8000,
                captureSize = 1024,
                processFftData = { _, bytes, samplingRate ->
                    FftDataProcessor(bytes)
                        .filterFrequency(samplingRate / 1000, 1024, 60, 8000)
                        .applyLogScale()
                        .normalizeByZScore()
                        .normalize()
                        .result()
                }
            ),
            modifier = Modifier.size(300.dp),
            color = Color.Magenta,
            clippingRadiusConfig = ClippingRadiusConfig.Fixed(100.dp),
            gradientConfig = GradientConfig.Enabled(
                duration = 2000,
                color = Color.White
            )
        )
    }
}
```

---

## API Reference

### CircleVisualizer Parameters

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `audioSessionId` | `Int` | Required | Audio session ID from MediaPlayer/ExoPlayer |
| `soundEffects` | `SoundEffect` | Required | Visualization effect type |
| `visualizerConfig` | `VisualizerConfig` | Required | Audio capture configuration |
| `modifier` | `Modifier` | `Modifier` | Layout modifier |
| `color` | `Color` | `White` | Primary visualization color |
| `clippingRadiusConfig` | `ClippingRadiusConfig` | `FullClip` | Inner radius configuration |
| `gradientConfig` | `GradientConfig` | `Default` | Gradient animation configuration |

---

## Requirements

- **Min SDK**: 26 (Android 8.0)
- **Compile SDK**: 35
- **Kotlin**: 1.9+
- **Jetpack Compose**: 1.5+

---
