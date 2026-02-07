package com.miller198.audiovisualizsample.presentation.screen.setting

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.miller198.audiovisualizsample.R
import com.miller198.audiovisualizsample.domain.PlayerPreference
import com.miller198.audiovisualizsample.presentation.common.DefaultTopAppBar

@Composable
fun SettingScreen(
    onBackClick: () -> Unit,
    viewModel: SettingViewModel = viewModel(factory = SettingViewModelFactory.Factory)
) {
    val currentEffect by viewModel.playerPreference.collectAsStateWithLifecycle(null)

    val savePreference: (PlayerPreference) -> Unit = {
        viewModel.savePlayerPreference(it)
    }

    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = stringResource(id = R.string.setting_title),
                onBackClick = onBackClick
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Black)
                .padding(innerPadding),
        ) {
            currentEffect?.let {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    verticalArrangement = Arrangement.spacedBy(35.dp),
                ) {
                    items(PlayerEffectType.entries.size) { index ->
                        val effectType = PlayerEffectType.entries[index]

                        SoundEffectItem(
                            imageRes = effectType.effect.drawable,
                            effectName = stringResource(effectType.effect.name),
                            effect = effectType.effect.preference,
                            currentEffect = it,
                            onClick = {
                                savePreference(effectType.effect.preference)
                            },
                            imagePadding = effectType.effect.drawablePadding
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SoundEffectItem(
    @DrawableRes imageRes: Int,
    effect: PlayerPreference,
    effectName: String,
    currentEffect: PlayerPreference,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    imagePadding: Dp = 0.dp
) {
    Column(
        modifier = modifier
            .selectable(
                selected = (effect == currentEffect),
                onClick = onClick,
                role = Role.RadioButton
            ),
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(imagePadding)
        )
        SelectEffectButton(
            text = effectName,
            selected = (effect == currentEffect),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun SelectEffectButton(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
            colors = RadioButtonDefaults.colors(
                selectedColor = White
            )
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = White,
            modifier = Modifier.padding(vertical = 5.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EditPlayerScreenPreview() {
    SettingScreen(
        onBackClick = { }
    )
}
