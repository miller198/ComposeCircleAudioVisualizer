package com.miller198.audiovisualizsample.presentation.screen.player

import android.net.Uri
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.miller198.audiovisualizer.soundeffect.SoundEffect
import com.miller198.audiovisualizsample.domain.PlayerPreference
import com.miller198.audiovisualizsample.presentation.PlayerUiState
import com.miller198.audiovisualizsample.presentation.common.DefaultTopAppBar
import com.miller198.audiovisualizsample.presentation.playerUi.MusicPlayer
import com.miller198.audiovisualizsample.presentation.screen.setting.SettingScreen
import com.miller198.audiovisualizsample.presentation.screen.setting.SettingViewModel
import com.miller198.audiovisualizsample.presentation.screen.setting.SettingViewModelFactory

@Composable
fun MainPlayerScreen(
    audioUri: Uri,
    playerViewModel: PlayerViewModel = viewModel(),
    settingViewModel: SettingViewModel = viewModel(factory = SettingViewModelFactory.Factory)
) {
    val context = LocalContext.current
    val playerUiState = playerViewModel.playerUiState.collectAsStateWithLifecycle()
    val audioSessionId by playerViewModel.audioSessionId.collectAsStateWithLifecycle()
    val playerPreference = settingViewModel.playerPreference.collectAsStateWithLifecycle()
    var showSetting by remember { mutableStateOf(false) }

    val mainColor = White

    LaunchedEffect(audioUri) {
        playerViewModel.readyPlayer(context, audioUri)
    }

    Box (
        modifier = Modifier.fillMaxSize()
    ) {
        PlayerScreenContent(
            playerUiState = playerUiState.value,
            playerPreference = playerPreference.value,
            mainColor = mainColor,
            audioSessionId = audioSessionId,
            onSeekChanged = playerViewModel::playerSeekTo,
            onReplayForwardClick = playerViewModel::replayForward,
            onPauseToggle = playerViewModel::togglePlayPause,
            onActionClick = {
                showSetting = true
            }
        )

        if(showSetting) {
            SettingScreen(
                onBackClick = {
                    showSetting = false
                },
                viewModel = settingViewModel
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlayerScreenContent(
    playerUiState: PlayerUiState,
    playerPreference: PlayerPreference,
    audioSessionId: Int,
    mainColor: Color,
    onSeekChanged: (Long) -> Unit = { _ -> },
    onReplayForwardClick: (Long) -> Unit = { _ -> },
    onPauseToggle: () -> Unit = { },
    onActionClick: () -> Unit = { },
) {
    val scrollState = rememberScrollState()

    val audioEffectColor = mainColor.copy(
        red = (mainColor.red + 0.2f).coerceAtMost(1.0f),
        green = (mainColor.green + 0.2f).coerceAtMost(1.0f),
        blue = (mainColor.blue + 0.2f).coerceAtMost(1.0f),
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DefaultTopAppBar(
                title = "SAMPLE PLAYER",
                showBack = false,
                actions = {
                    IconButton(
                        onClick = onActionClick
                    ) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = null,
                            tint = White
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Black)
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
                            soundEffect = playerPreference.toSoundEffect(),
                            audioEffectColor = Color.Magenta,
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

private fun PlayerPreference.toSoundEffect() = when(this) {
    PlayerPreference.NONE -> SoundEffect.None
    PlayerPreference.BAR -> SoundEffect.Bar
    PlayerPreference.FILL -> SoundEffect.WaveFill
    PlayerPreference.STROKE -> SoundEffect.WaveStroke
}

@Preview
@Composable
private fun PickDetailPreview() {
    PlayerScreenContent(
        audioSessionId = 123,
        mainColor = White,
        playerUiState = PlayerUiState.Companion.PLAYER_STATE_INITIAL,
        onSeekChanged = { },
        onReplayForwardClick = { },
        onPauseToggle = { },
        playerPreference = PlayerPreference.BAR,
    )
}
