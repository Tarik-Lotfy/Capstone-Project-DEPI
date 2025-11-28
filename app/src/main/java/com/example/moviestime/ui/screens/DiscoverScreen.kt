package com.example.moviestime.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.moviestime.data.remote.Genre
import com.example.moviestime.ui.components.MovieRowCard
import com.example.moviestime.viewmodel.SearchViewModel
import kotlin.math.ceil
import com.example.moviestime.ui.theme.Inter
import com.example.moviestime.ui.theme.PlayFair

@Composable
fun ShimmerMovieGrid() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(6) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray.copy(alpha = 0.3f))
            )
        }
    }
}

@Composable
fun DiscoverScreen(
    searchViewModel: SearchViewModel = viewModel(),
    onMovieClick: (Int) -> Unit
) {
    val query by searchViewModel.searchQuery.collectAsState()
    val results by searchViewModel.searchResults.collectAsState()
    val genresApi by searchViewModel.genres.collectAsState()
    val selectedGenreId by searchViewModel.selectedGenreId.collectAsState()
    val isLoading by searchViewModel.isLoading.collectAsState()

    val isSearchTyping = query.isNotEmpty()
    val isSearching = query.length >= 2

    val isRecommended = selectedGenreId == null && !isSearching
    val isGenreSelected = selectedGenreId != null && !isSearching

    val genresWithRecommended = remember(genresApi) {
        listOf(Genre(id = 0, name = "Recommended")) + genresApi
    }

    val currentResultsTitle = when {
        isSearching -> "Search Results"
        isRecommended -> "Recommended Movies"
        isGenreSelected -> genresApi.find { it.id == selectedGenreId }?.name + " Movies"
        else -> "Discover"
    }

    val accentYellow = Color(0xFFF1C40F)
    val accentBurgundy = Color(0xFF6A0F1C)

    val resultRowCount = remember(results.size) {
        if (results.isEmpty()) 0 else ceil(results.size / 2.0).toInt()
    }
    val rowHeightWithSpacing = 250.dp + 16.dp
    val minEmptyHeight = 350.dp

    val gridContentHeight = remember(resultRowCount, isLoading) {
        when {
            isLoading -> 450.dp
            resultRowCount > 0 -> (resultRowCount * rowHeightWithSpacing.value).dp
            else -> minEmptyHeight
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
        contentPadding = PaddingValues(bottom = 100.dp)
    ) {
        item {
            Spacer(Modifier.height(20.dp))
        }

        item {
            TextField(
                value = query,
                onValueChange = { searchViewModel.onSearchQueryChanged(it) },
                textStyle = TextStyle(
                    fontFamily = Inter,
                    fontSize = 16.sp
                ),
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.White.copy(0.7f)
                    )
                },
                placeholder = {
                    Text(
                        "Search for films, series, genres...",
                        fontFamily = Inter,
                        color = Color.White.copy(alpha = 0.5f),
                        fontSize = 16.sp
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White.copy(alpha = 0.1f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.08f),
                    cursorColor = accentYellow,
                    focusedIndicatorColor = accentYellow,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            )
        }

        if (!isSearchTyping) {
            item {
                Spacer(Modifier.height(24.dp))

                Text(
                    "Genre",
                    fontFamily = PlayFair,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 15.dp)

                )

                Spacer(Modifier.height(12.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(genresWithRecommended, key = { it.id }) { genre ->
                        val isSelected = if (genre.id == 0) selectedGenreId == null else genre.id == selectedGenreId

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(
                                    if (isSelected)
                                        Color(0xFF6A0F1C)
                                    else
                                        Color(0xFFF1C40F)
                                )
                                .clickable { searchViewModel.onGenreSelected(genre.id) }
                                .padding(horizontal = 8.dp, vertical = 8.dp),
                        ) {
                            Text(
                                genre.name,
                                fontFamily = Inter,
                                color = if (isSelected) Color.White else Color.Black,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))
            }
        } else {
            item {
                Spacer(Modifier.height(24.dp))
            }
        }

        if (isLoading || results.isNotEmpty() || isSearchTyping || isGenreSelected || isRecommended) {

            item {
                Box(
                    modifier = Modifier.fillMaxWidth().height(gridContentHeight),
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        isLoading -> ShimmerMovieGrid()
                        results.isNotEmpty() -> {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(results, key = { it.id }) { movie ->
                                    MovieRowCard(
                                        movie = movie,
                                        onMovieClick = { selectedMovie ->
                                            onMovieClick(selectedMovie.id)
                                        }
                                    )
                                }
                            }
                        }
                        isSearchTyping -> {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Default.Search,
                                    contentDescription = "No results",
                                    tint = Color.White.copy(alpha = 0.3f),
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    "No films matched your search.",
                                    fontFamily = Inter,
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                        else -> {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.AutoMirrored.Filled.Sort,
                                    contentDescription = "No films in category",
                                    tint = Color.White.copy(alpha = 0.3f),
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    "No movies available in this category.",
                                    fontFamily = Inter,
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
