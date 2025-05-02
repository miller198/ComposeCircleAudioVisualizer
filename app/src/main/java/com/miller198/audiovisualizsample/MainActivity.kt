package com.miller198.audiovisualizsample

import android.Manifest.permission.RECORD_AUDIO
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.miller198.audiovisualizsample.ui.theme.ComposeSoundVisualizerProjectTheme

class MainActivity : ComponentActivity() {
    private val recordAudioPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                Toast.makeText(this, "Record audio permission not granted", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (ContextCompat.checkSelfPermission(this, RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            recordAudioPermissionLauncher.launch(RECORD_AUDIO)
        }

        setContent {
            ComposeSoundVisualizerProjectTheme {
                PickDetailScreen(
                    audioUri = Uri.parse("rawresource://${packageName}/${R.raw.sample_1}"),
                )
            }
        }
    }
}
