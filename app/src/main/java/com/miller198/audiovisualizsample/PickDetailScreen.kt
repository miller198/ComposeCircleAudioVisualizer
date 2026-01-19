package com.miller198.audiovisualizsample

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.miller198.audiovisualizsample.playerUi.MusicPlayer

@Composable
fun PickDetailScreen(
    audioUri: Uri,
) {
    val context = LocalContext.current
    val playerViewModel = remember { PlayerViewModel() }
    val playerUiState = playerViewModel.playerUiState.collectAsStateWithLifecycle()
    val audioSessionId by playerViewModel.audioSessionId.collectAsStateWithLifecycle()

    val mainColor = White

    LaunchedEffect(audioUri) {
        playerViewModel.readyPlayer(context, audioUri)
    }

    LaunchedEffect(playerUiState.value) {
        Log.d("PlayerViewModel", "playerUiState: ${playerUiState.value}")
    }

    PickDetailContents(
        playerUiState = playerUiState.value,
        mainColor = mainColor,
        audioSessionId = audioSessionId,
        onSeekChanged = playerViewModel::playerSeekTo,
        onReplayForwardClick = playerViewModel::replayForward,
        onPauseToggle = playerViewModel::togglePlayPause
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PickDetailContents(
    audioSessionId: Int,
    mainColor: Color,
    playerUiState: PlayerUiState,
    onSeekChanged: (Long) -> Unit = { _ -> },
    onReplayForwardClick: (Long) -> Unit = { _ -> },
    onPauseToggle: () -> Unit = { },
) {
    val backGroundColor = Black
    val scrollState = rememberScrollState()

    val audioEffectColor = mainColor.copy(
        red = (mainColor.red + 0.2f).coerceAtMost(1.0f),
        green = (mainColor.green + 0.2f).coerceAtMost(1.0f),
        blue = (mainColor.blue + 0.2f).coerceAtMost(1.0f),
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "SAMPLE PLAYER",
                        style = MaterialTheme.typography.titleLarge,
                        color = White,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backGroundColor
                ),
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = backGroundColor
                )
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(top = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                SongInfo(
                    songName = "sample song",
                    artistName = "sample artist",
                    textColor = White,
                    modifier = Modifier.zIndex(1f)
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.CenterHorizontally)
                        .zIndex(0f)
                        .padding(top = 10.dp)
                ) {
                    if(audioSessionId != 0) {
                        CircleAlbumCover(
                            audioEffectColor = audioEffectColor,
                            audioSessionId = audioSessionId,
                            modifier = Modifier
                                .size(360.dp)
                                .align(Alignment.Center),
                        )
                    }
                }
            }

            MusicPlayer(
                playerState = playerUiState,
                onSeekChanged = onSeekChanged,
                onReplayForwardClick = { isForward ->
                    if (isForward) {
                        onReplayForwardClick(5_000L)
                    } else {
                        onReplayForwardClick(-5_000L)
                    }
                },
                onPauseToggle = onPauseToggle,
            )
        }
    }
}

@Composable
internal fun SongInfo(
    songName: String,
    artistName: String,
    textColor: Color,
    modifier: Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = songName,
            color = textColor,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )

        Text(
            text = artistName,
            color = textColor,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview
@Composable
private fun PickDetailPreview() {
    PickDetailContents(
        audioSessionId = 123,
        mainColor = White,
        playerUiState = PlayerUiState.PLAYER_STATE_INITIAL,
        onSeekChanged = { },
        onReplayForwardClick = { },
        onPauseToggle = { },
    )
}
