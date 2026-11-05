package ru.yandex.practicum.playlistmaker.ui.search

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import ru.yandex.practicum.playlistmaker.R
import ru.yandex.practicum.playlistmaker.domain.model.Track
import ru.yandex.practicum.playlistmaker.ui.view_model.SearchState
import ru.yandex.practicum.playlistmaker.ui.view_model.SearchViewModel

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = viewModel(factory = SearchViewModel.getViewModelFactory()),
    onTrackClick: (Track) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val screenState by viewModel.searchScreenState.collectAsState()
    val historyList by viewModel.historyList.collectAsState()
    var searchText by rememberSaveable { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    val ypBlack = colorResource(id = R.color.yp_black)
    val ypBlue = colorResource(id = R.color.yp_blue)
    val ypWhite = colorResource(id = R.color.yp_white)
    val textGray = colorResource(id = R.color.yp_text_gray)
    val lightGray = colorResource(id = R.color.yp_light_gray)

    val smallPadding = dimensionResource(id = R.dimen.padding_small)
    val mediumPadding = dimensionResource(id = R.dimen.padding_medium)
    val regularPadding = dimensionResource(id = R.dimen.padding_regular)
    val standardPadding = dimensionResource(id = R.dimen.padding_standard)
    val searchIconSize = dimensionResource(id = R.dimen.search_icon_size)

    LaunchedEffect(Unit) {
        viewModel.getHistoryList()
    }

    LaunchedEffect(searchText) {
        if (searchText.isNotBlank()) {
            delay(1000L)
            viewModel.search(searchText)
        }
    }

    LaunchedEffect(screenState) {
        if (screenState is SearchState.Success) {
            focusManager.clearFocus()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = smallPadding, vertical = mediumPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = stringResource(R.string.back),
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.width(regularPadding))
            Text(
                text = stringResource(R.string.search),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = standardPadding, vertical = mediumPadding)
                .background(
                    color = lightGray,
                    shape = RoundedCornerShape(dimensionResource(id = R.dimen.corner_radius_medium))
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.search_box_height))
                    .padding(horizontal = regularPadding),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.search),
                        tint = textGray,
                        modifier = Modifier
                            .size(searchIconSize)
                            .clickable {
                                if (searchText.isNotEmpty()) {
                                    viewModel.search(searchText)
                                }
                            }
                    )

                    Spacer(modifier = Modifier.width(mediumPadding))

                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (searchText.isEmpty()) {
                            Text(
                                text = stringResource(R.string.search_placeholder),
                                color = textGray,
                                fontSize = 16.sp
                            )
                        }

                        BasicTextField(
                            value = searchText,
                            onValueChange = { newText ->
                                searchText = newText
                                if (newText.isEmpty()) {
                                    viewModel.clearSearch()
                                    viewModel.loadHistory()
                                }
                            },
                            singleLine = true,
                            textStyle = TextStyle(
                                color = ypBlack,
                                fontSize = 16.sp
                            ),
                            cursorBrush = SolidColor(ypBlue),
                            modifier = Modifier
                                .fillMaxWidth()
                                .onFocusChanged { focusState ->
                                    isFocused = focusState.isFocused
                                    if (focusState.isFocused) {
                                        viewModel.loadHistory()
                                    }
                                }
                        )
                    }

                    if (searchText.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = stringResource(R.string.clear_search),
                            tint = textGray,
                            modifier = Modifier
                                .size(searchIconSize)
                                .clickable {
                                    searchText = ""
                                    viewModel.clearSearch()
                                    viewModel.loadHistory()
                                }
                        )
                    }
                }
            }

            if (isFocused && searchText.isEmpty() && historyList.isNotEmpty()) {
                HistoryRequests(
                    historyList = historyList,
                    onClick = { item ->
                        searchText = item
                        viewModel.search(item)
                    }
                )
            }
        }

        when (val state = screenState) {
            is SearchState.Initial -> {
                if (!isFocused || historyList.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.search_initial_hint),
                            fontSize = 16.sp,
                            color = textGray
                        )
                    }
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
                val tracks = state.foundList
                if (tracks.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.nothing_found),
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                } else {
                    val dividerThickness = dimensionResource(id = R.dimen.divider_thickness)
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(tracks) { track ->
                            TrackListItem(
                                track = track,
                                onClick = { onTrackClick(track) }
                            )
                            HorizontalDivider(
                                thickness = dividerThickness,
                                color = lightGray,
                                modifier = Modifier.padding(horizontal = standardPadding)
                            )
                        }
                    }
                }
            }

            is SearchState.Fail -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(standardPadding),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.search_error),
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(mediumPadding))
                    Text(
                        text = state.error,
                        fontSize = 14.sp,
                        color = textGray
                    )
                    Spacer(modifier = Modifier.height(standardPadding))
                    Button(
                        onClick = { viewModel.retryLastSearch() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ypBlack,
                            contentColor = ypWhite
                        )
                    ) {
                        Text(text = stringResource(R.string.retry))
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryRequests(
    historyList: List<String>,
    onClick: (String) -> Unit
) {
    val textGray = colorResource(id = R.color.yp_text_gray)
    val ypBlack = colorResource(id = R.color.yp_black)
    val mediumPadding = dimensionResource(id = R.dimen.padding_medium)
    val regularPadding = dimensionResource(id = R.dimen.padding_regular)
    val verticalItemPadding = dimensionResource(id = R.dimen.padding_vertical_item)
    val historyIconSize = dimensionResource(id = R.dimen.history_icon_size)

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        HorizontalDivider(
            thickness = dimensionResource(id = R.dimen.divider_thickness),
            color = textGray,
            modifier = Modifier.padding(horizontal = mediumPadding)
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = dimensionResource(id = R.dimen.history_max_height))
        ) {
            items(historyList) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onClick(item) }
                        .padding(horizontal = regularPadding, vertical = verticalItemPadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_history),
                        contentDescription = item,
                        modifier = Modifier.size(historyIconSize)
                    )
                    Spacer(modifier = Modifier.width(mediumPadding))
                    Text(
                        text = item,
                        fontSize = 16.sp,
                        color = ypBlack
                    )
                }
            }
        }
    }
}
