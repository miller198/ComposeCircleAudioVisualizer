package com.miller198.audiovisualizsample

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.miller198.audiovisualizsample.PlayerUiState.Companion.PLAYER_STATE_INITIAL
import com.miller198.audiovisualizsample.PlayerUiState.Companion.PLAYER_STATE_STOP
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlayerViewModel() : ViewModel() {

    private var player: ExoPlayer? = null

    private var _audioSessionId = MutableStateFlow<Int>(0)
    val audioSessionId = _audioSessionId.asStateFlow()

    private val _playerState = MutableStateFlow(PLAYER_STATE_INITIAL)
    val playerUiState = _playerState.asStateFlow()

    @OptIn(UnstableApi::class)
    private fun initializePlayer(context: Context) {
        ExoPlayer.Builder(context).build().also {
            it.addListener(object : Player.Listener {
                override fun onPlayerError(error: PlaybackException) {
                    handleError(error)
                }

                override fun onPlaybackStateChanged(playbackState: Int) {
                    if (playbackState == Player.STATE_ENDED) {
                        it.seekTo(0)
                        it.pause()
                    }
                }
            })
            it.volume = 0.8f
            this.player = it
            _audioSessionId.value = it.audioSessionId
        }
    }

    fun readyPlayer(context: Context, uri: Uri) {
        if (player != null) return

        initializePlayer(context)

        player?.let {
            val mediaItem = MediaItem.fromUri(uri)
            it.setMediaItem(mediaItem)
            it.prepare()
            it.playWhenReady = false
            it.seekTo(_playerState.value.currentPosition)

            _playerState.value =
                PlayerUiState(isReady = true, currentPosition = _playerState.value.currentPosition)

            updatePlayerStatePeriodically(it)
        }
    }

    private fun updatePlayerStatePeriodically(exoPlayer: ExoPlayer) {
        viewModelScope.launch {
            while (_playerState.value.isReady) {
                _playerState.emit(
                    _playerState.value.copy(
                        isPlaying = exoPlayer.isPlaying,
                        duration = exoPlayer.duration,
                        currentPosition = exoPlayer.currentPosition,
                        bufferPercentage = exoPlayer.bufferedPercentage,
                    )
                )
                delay(1000)
            }
        }
    }

    fun replayForward(sec: Long) {
        player?.let {
            it.seekTo(it.currentPosition + sec)
            viewModelScope.launch {
                _playerState.value =
                    _playerState.value.copy(currentPosition = it.currentPosition)
            }
        }
    }

    fun togglePlayPause() {
        player?.let {
            togglePlayPause(it)
        }
    }

    private fun togglePlayPause(exoPlayer: ExoPlayer) {
        if (exoPlayer.isPlaying) pause(exoPlayer)
        else play(exoPlayer)
    }

    fun play() {
        player?.let {
            play(it)
        }
    }

    private fun play(exoPlayer: ExoPlayer) {
        viewModelScope.launch {
            _playerState.value = _playerState.value.copy(isPlaying = true)
        }
        exoPlayer.play()
    }

    fun pause() {
        player?.let {
            pause(it)
        }
    }

    private fun pause(exoPlayer: ExoPlayer) {
        viewModelScope.launch {
            _playerState.value = _playerState.value.copy(isPlaying = false)
        }
        exoPlayer.pause()
    }

    fun stop() {
        player?.let {
            viewModelScope.launch {
                _playerState.value = PLAYER_STATE_STOP
            }
            it.stop()
        }
    }

    fun playerSeekTo(sec: Long) {
        viewModelScope.launch {
            player?.let {
                _playerState.value = _playerState.value.copy(currentPosition = sec)
                it.seekTo(sec)
            }
        }
    }

    fun savePlayerState() {
        player?.let {
            _playerState.value = _playerState.value.copy(
                isReady = false,
                isPlaying = false,
                currentPosition = it.currentPosition,
            )
        }
    }

    private fun releasePlayer() {
        player?.release()
    }

    private fun handleError(error: PlaybackException) {
        when (error.errorCode) {
            PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED -> {
                Log.d("PlayerViewModel", "Network connection error")
            }

            PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND -> {
                Log.d("PlayerViewModel", "File not found")
            }

            PlaybackException.ERROR_CODE_DECODER_INIT_FAILED -> {
                Log.d("PlayerViewModel", "Decoder initialization error")
            }

            else -> {
                Log.d("PlayerViewModel", "${error.message}")
            }
        }
    }

    override fun onCleared() {
        releasePlayer()
        super.onCleared()
    }
}
