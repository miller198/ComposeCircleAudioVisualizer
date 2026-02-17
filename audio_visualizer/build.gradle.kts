plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.maven.publish)
    alias(libs.plugins.vanniktech.maven.publish)
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()

    coordinates(
        groupId = "io.github.miller198",
        artifactId = "ComposeCircleAudioVisualizer", // 라이브러리 이름
        version = libs.versions.libraryVersion.get() // 라이브러리 버전 입력
    )

    pom {
        name = "Compose Circular Audio Visualizer"
        description = "A Jetpack Compose library for audio visualization"
        inceptionYear = "2026"
        url = "https://github.com/miller198/ComposeCircleAudioVisualizer"

        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
                description = "http://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }

        developers {
            developer {
                id = "miller198"
                name = "miller198"
                url = "https://github.com/miller198"
            }
        }

        scm {
            url = "https://github.com/miller198/ComposeCircleAudioVisualizer"
            connection = "scm:git:git://github.com/miller198/ComposeCircleAudioVisualizer.git"
            developerConnection = "scm:git:ssh://git@github.com/miller198/ComposeCircleAudioVisualizer.git"
        }
    }
}

android {
    namespace = "com.miller198.audiovisualizer"
    compileSdk = 35

    defaultConfig {
        aarMetadata {
            minCompileSdk = 26
        }
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures{
        compose = true
    }
}

dependencies {
    implementation(libs.material)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose)
}
