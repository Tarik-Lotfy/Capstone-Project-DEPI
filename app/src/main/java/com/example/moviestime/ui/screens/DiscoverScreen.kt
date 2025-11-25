package com.example.moviestime.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.moviestime.data.remote.Genre
import com.example.moviestime.viewmodel.SearchViewModel

@Composable
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        items(6) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Gray.copy(alpha = 0.3f))
            )
    }
}


@Composable
fun DiscoverScreen(
    navController: NavHostController,
    searchViewModel: SearchViewModel = viewModel()
) {
    val query by searchViewModel.searchQuery.collectAsState()
    val results by searchViewModel.searchResults.collectAsState()
    val genresApi by searchViewModel.genres.collectAsState()
    val selectedGenreId by searchViewModel.selectedGenreId.collectAsState()
    val isSearching = query.length >= 2

    LazyColumn(
    val genresWithRecommended = remember(genresApi) {
        listOf(Genre(id = 0, name = "Recommended")) + genresApi
    }

    val currentResultsTitle = when {
        isRecommended -> "Recommended Movies"
        isGenreSelected -> genresApi.find { it.id == selectedGenreId }?.name + " Movies"
        else -> "Discover"
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            modifier = Modifier.fillMaxWidth().height(48.dp),
            Text(
                "Discover Movies",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                text = if (isSearchTyping) "Searching..." else "Discover",
                fontSize = 28.sp,
                color = Color.White
            )
            OutlinedTextField(
                value = query,
                onValueChange = { searchViewModel.onSearchQueryChanged(it) },
                    Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White.copy(alpha = 0.7f))
                },
                    Text("Search movies, series, genres...", color = Color.White.copy(alpha = 0.5f))
                },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Black.copy(alpha = 0.4f),
                    unfocusedContainerColor = Color.Black.copy(alpha = 0.4f),
                    unfocusedTextColor = Color.White,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = Color.White.copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(16.dp)
        if (results.isNotEmpty()) {
            item {
                Column {
                    Text(
            if (!isSearchTyping) {
                    Icon(
                        Icons.Default.FilterList,
                        contentDescription = "Filter",
                        tint = accentYellow,
                        modifier = Modifier.size(28.dp)
                    )

                        modifier = Modifier.height(300.dp)
                    ) {
                        items(results) { movie ->
                                onClick = {
                }
            }
        }

        Spacer(Modifier.height(18.dp))

        TextField(
            value = query,
                Icon(
                )
            },
            placeholder = {
                Text(
                    fontSize = 16.sp
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White.copy(alpha = 0.1f),
                unfocusedContainerColor = Color.White.copy(alpha = 0.08f),
                cursorColor = accentYellow,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
        )

        if (!isSearchTyping) {
            Spacer(Modifier.height(24.dp))

            Text(
                "Explore by Genre",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
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
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                    ) {
                        Text(
                            genre.name,
                            color = if (isSelected) Color.White else Color.Black,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
        } else {
            Spacer(Modifier.height(24.dp))
        }

        currentResultsTitle?.let { title ->
            Text(
                title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth().weight(1f),
                contentAlignment = Alignment.TopCenter
            ) {
                ShimmerMovieGrid()
            }
        } else if (results.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(results, key = { it.id }) { movie ->
                    MovieGridItem(
                        movie = movie,
                        onClick = { navController.navigate("movie/${movie.id}") },
                        onFavoriteClick = {}
                    )
                }
            }
        } else if (isSearchTyping) {
            Box(
                modifier = Modifier.fillMaxWidth().weight(1f),
                contentAlignment = Alignment.Center
            ) {
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
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxWidth().weight(1f),
                contentAlignment = Alignment.Center
            ) {
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
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}