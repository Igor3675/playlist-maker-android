package ru.yandex.practicum.playlistmaker.ui.setting

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import ru.yandex.practicum.playlistmaker.R
import ru.yandex.practicum.playlistmaker.ui.theme.PlaylistMakerTheme

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current
    var darkThemeEnabled by rememberSaveable { mutableStateOf(false) }

    val shareMessage = stringResource(id = R.string.share_message)
    val supportEmail = stringResource(id = R.string.support_email)
    val supportSubject = stringResource(id = R.string.support_subject)
    val supportBody = stringResource(id = R.string.support_body)
    val offerUrl = stringResource(id = R.string.offer_url)

    val standardPadding = dimensionResource(id = R.dimen.padding_standard)
    val largePadding = dimensionResource(id = R.dimen.padding_large)
    val ypBlue = colorResource(id = R.color.yp_blue)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.toolbar_height))
                .padding(horizontal = standardPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = stringResource(id = R.string.back),
                modifier = Modifier
                    .size(dimensionResource(id = R.dimen.icon_size))
                    .clickable(onClick = onBackClick),
                tint = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.width(largePadding))
            Text(
                text = stringResource(id = R.string.settings),
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(modifier = Modifier.height(largePadding))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.settings_item_height))
                .padding(horizontal = standardPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.dark_theme),
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Switch(
                checked = darkThemeEnabled,
                onCheckedChange = { darkThemeEnabled = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = ypBlue,
                    checkedTrackColor = ypBlue.copy(alpha = 0.4f)
                )
            )
        }

        SettingsActionItem(
            title = stringResource(id = R.string.share_app),
            iconRes = R.drawable.ic_share,
            onClick = {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, shareMessage)
                }
                try {
                    context.startActivity(Intent.createChooser(shareIntent, null))
                } catch (_: ActivityNotFoundException) {
                }
            }
        )

        SettingsActionItem(
            title = stringResource(id = R.string.write_to_support),
            iconRes = R.drawable.ic_support,
            onClick = {
                val supportIntent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:")
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(supportEmail))
                    putExtra(Intent.EXTRA_SUBJECT, supportSubject)
                    putExtra(Intent.EXTRA_TEXT, supportBody)
                }
                try {
                    context.startActivity(supportIntent)
                } catch (_: ActivityNotFoundException) {
                }
            }
        )

        SettingsActionItem(
            title = stringResource(id = R.string.user_agreement),
            iconRes = R.drawable.ic_chevron_right,
            onClick = {
                val offerIntent = Intent(Intent.ACTION_VIEW, Uri.parse(offerUrl))
                try {
                    context.startActivity(offerIntent)
                } catch (_: ActivityNotFoundException) {
                }
            }
        )
    }
}

@Composable
fun SettingsActionItem(
    title: String,
    @DrawableRes iconRes: Int,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.settings_item_height))
            .clickable(onClick = onClick)
            .padding(horizontal = dimensionResource(id = R.dimen.padding_standard)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground
        )
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = title,
            modifier = Modifier.size(dimensionResource(id = R.dimen.icon_size)),
            tint = colorResource(id = R.color.yp_text_gray)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    PlaylistMakerTheme {
        SettingsScreen()
    }
}
