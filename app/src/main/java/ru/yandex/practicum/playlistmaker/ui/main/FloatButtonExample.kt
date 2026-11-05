package ru.yandex.practicum.playlistmaker.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import ru.yandex.practicum.playlistmaker.R

@Composable
fun FloatButtonExample() {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(id = R.string.main_screen_content),
            modifier = Modifier.align(Alignment.Center),
            fontSize = 20.sp
        )

        FloatingActionButton(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_xlarge))
                .align(Alignment.BottomEnd),
            onClick = { },
            containerColor = colorResource(id = R.color.yp_text_gray),
            contentColor = colorResource(id = R.color.yp_white),
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = stringResource(id = R.string.new_playlist)
            )
        }
    }
}
