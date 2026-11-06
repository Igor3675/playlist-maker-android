package ru.yandex.practicum.playlistmaker.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.yandex.practicum.playlistmaker.R
import ru.yandex.practicum.playlistmaker.domain.model.Track
import ru.yandex.practicum.playlistmaker.ui.view_model.SearchState
import ru.yandex.practicum.playlistmaker.ui.view_model.SearchViewModel

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = viewModel(factory = SearchViewModel.getViewModelFactory()),
    onBackClick: () -> Unit = {},
    onTrackClick: (Track) -> Unit = {}
) {
    val screenState by viewModel.searchScreenState.collectAsState()
    var text by rememberSaveable { mutableStateOf("") }

    val textGray = colorResource(id = R.color.yp_text_gray)
    val lightGray = colorResource(id = R.color.yp_light_gray)
    val ypBlack = colorResource(id = R.color.yp_black)
    val ypBlue = colorResource(id = R.color.yp_blue)
    val standardPadding = dimensionResource(id = R.dimen.padding_standard)

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
            Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding_large)))
            Text(
                text = stringResource(id = R.string.search),
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
                if (it.isBlank()) {
                    viewModel.clearSearch()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = standardPadding,
                    vertical = dimensionResource(id = R.dimen.padding_medium)
                ),
            placeholder = {
                Text(
                    text = stringResource(id = R.string.search_placeholder),
                    color = textGray
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(id = R.string.search),
                    modifier = Modifier.clickable {
                        if (text.isNotBlank()) {
                            viewModel.search(text)
                        }
                    },
                    tint = textGray
                )
            },
            trailingIcon = {
                if (text.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            text = ""
                            viewModel.clearSearch()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = stringResource(id = R.string.clear_search),
                            tint = textGray
                        )
                    }
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    if (text.isNotBlank()) {
                        viewModel.search(text)
                    }
                }
            ),
            shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius_medium)),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = lightGray,
                unfocusedContainerColor = lightGray,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = ypBlack,
                unfocusedTextColor = ypBlack
            )
        )

        when (val state = screenState) {
            is SearchState.Initial -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.search_initial_hint),
                        color = textGray
                    )
                }
            }

            is SearchState.Searching -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = ypBlue)
                }
            }

            is SearchState.Success -> {
                val tracks = state.list
                if (tracks.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.nothing_found),
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                } else {
                    val dividerThickness = dimensionResource(id = R.dimen.divider_thickness)
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(tracks.size) { index ->
                            TrackListItem(
                                track = tracks[index],
                                onClick = { onTrackClick(tracks[index]) }
                            )
                            HorizontalDivider(
                                thickness = dividerThickness,
                                color = lightGray
                            )
                        }
                    }
                }
            }

            is SearchState.Fail -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${stringResource(id = R.string.search_error)}: ${state.error}",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
