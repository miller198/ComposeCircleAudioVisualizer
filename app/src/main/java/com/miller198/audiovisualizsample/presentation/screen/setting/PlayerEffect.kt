package com.miller198.audiovisualizsample.presentation.screen.setting

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.miller198.audiovisualizsample.R
import com.miller198.audiovisualizsample.domain.PlayerPreference

data class PlayerEffect(
    val preference: PlayerPreference,
    @StringRes val name: Int,
    @DrawableRes val drawable: Int,
    val drawablePadding: Dp
)

enum class PlayerEffectType(val effect: PlayerEffect) {
    NONE(
        PlayerEffect(
            preference = PlayerPreference.NONE,
            name = R.string.sound_effect_none,
            drawable = R.drawable.soundeffectnone,
            drawablePadding = 0.dp
        )
    ),
    BAR(
        PlayerEffect(
            preference = PlayerPreference.BAR,
            name = R.string.sound_effect_bar,
            drawable = R.drawable.soundeffectbar,
            drawablePadding = 0.dp
        )
    ),
    FILL(
        PlayerEffect(
            preference = PlayerPreference.FILL,
            name = R.string.sound_effect_wave_fill,
            drawable = R.drawable.soundeffectfill,
            drawablePadding = 15.dp
        )
    ),
    STROKE(
        PlayerEffect(
            preference = PlayerPreference.STROKE,
            name = R.string.sound_effect_wave_stroke,
            drawable = R.drawable.soundeffectstroke,
            drawablePadding = 15.dp
        )
    ),
}
