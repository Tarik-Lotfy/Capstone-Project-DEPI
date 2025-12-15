package com.example.moviestime.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.moviestime.R
import com.example.moviestime.ui.theme.Inter
import com.example.moviestime.ui.theme.PlayFair
import com.example.moviestime.viewmodel.ProfileViewModel

@Composable
fun EditProfileScreenContent(
    profileViewModel: ProfileViewModel,
    onBackClick: () -> Unit,
    onNavigateToProfile: () -> Unit = onBackClick
) {
    val uiState by profileViewModel.uiState.collectAsState()
    val context = LocalContext.current

    var selectedPhotoUri by remember { mutableStateOf<String?>(null) }
    var initialName by remember { mutableStateOf<String?>(null) }
    var initialBio by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(uiState.email, uiState.name, uiState.bio) {
        if (initialName == null || initialBio == null) {
            initialName = uiState.name
            initialBio = uiState.bio
        }
    }

    val colorScheme = MaterialTheme.colorScheme
    val backgroundColor = colorScheme.background
    val primaryColor = colorScheme.primary
    val cardColor = colorScheme.surface
    val textColor = colorScheme.onBackground
    val mutedColor = textColor.copy(alpha = 0.7f)
    val borderColor = colorScheme.outline.copy(alpha = 0.6f)
    val goldColor = colorScheme.secondary

    val successMsg = stringResource(R.string.profile_updated_success)
    LaunchedEffect(Unit) {
        profileViewModel.resetSavedFlag()
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        selectedPhotoUri = uri?.toString()
        profileViewModel.updatePhotoUri(selectedPhotoUri)
    }

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) {
            Toast.makeText(context, successMsg, Toast.LENGTH_SHORT).show()
            onNavigateToProfile()
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }

    val maxBioLength = 300

    Scaffold(
        containerColor = backgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 8.dp)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))

            AvatarSection(
                initials = if (uiState.name.isNotEmpty()) uiState.name.take(1).uppercase()
                else stringResource(R.string.profile_initial_placeholder),
                imageUrl = selectedPhotoUri ?: uiState.photoUrl,
                goldColor = goldColor,
                primaryColor = primaryColor,
                textColor = textColor,
                onImageClick = { imagePickerLauncher.launch("image/*") }
            )

            Spacer(Modifier.height(32.dp))

            TextButton(onClick = { imagePickerLauncher.launch("image/*") }) {
                Text(
                    text = stringResource(R.string.edit_photo),
                    fontFamily = Inter,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .imePadding(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                EditProfileTextField(
                    value = uiState.email,
                    onValueChange = { },
                    label = stringResource(R.string.email),
                    placeholder = stringResource(R.string.email),
                    supportingText = stringResource(R.string.email_read_only),
                    cardColor = cardColor,
                    textColor = mutedColor,
                    mutedColor = mutedColor,
                    borderColor = borderColor,
                    readOnly = true
                )

                EditProfileTextField(
                    value = uiState.name,
                    onValueChange = { profileViewModel.updateName(it) },
                    label = stringResource(R.string.name),
                    placeholder = stringResource(R.string.name_placeholder),
                    supportingText = stringResource(R.string.display_name_desc),
                    cardColor = cardColor,
                    textColor = textColor,
                    mutedColor = mutedColor,
                    borderColor = borderColor
                )

                EditProfileBioField(
                    value = uiState.bio,
                    onValueChange = { if (it.length <= maxBioLength) profileViewModel.updateBio(it) },
                    label = stringResource(R.string.bio),
                    placeholder = stringResource(R.string.bio_placeholder),
                    maxBioLength = maxBioLength,
                    cardColor = cardColor,
                    textColor = textColor,
                    mutedColor = mutedColor,
                    borderColor = borderColor
                )

                val isDirty =
                    (initialName != null && uiState.name != initialName) ||
                    (initialBio != null && uiState.bio != initialBio) ||
                    (selectedPhotoUri != null)
                val isSaveEnabled =
                    (uiState.name.isNotBlank() || selectedPhotoUri != null) && isDirty
                val isSaving = uiState.isLoading

                Button(
                    onClick = { profileViewModel.save() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryColor,
                        contentColor = textColor
                    ),
                    enabled = isSaveEnabled && !isSaving
                ) {
                    
                    if (isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = textColor,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.save_changes),
                            fontFamily = Inter,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = textColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileScreenTopBar(title: String, onBackClick: () -> Unit) {
    val textColor = MaterialTheme.colorScheme.onBackground

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp)
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(44.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.back_button_cd),
                tint = textColor,
                modifier = Modifier.size(24.dp)
            )
        }

        Text(
            text = title,
            fontFamily = PlayFair,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = textColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun AvatarSection(
    initials: String,
    imageUrl: String?,
    goldColor: Color,
    primaryColor: Color,
    textColor: Color,
    onImageClick: () -> Unit = {}
) {
    Box(modifier = Modifier.size(100.dp)) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .border(2.dp, goldColor, CircleShape)
                .padding(4.dp)
                .clip(CircleShape)
                .background(primaryColor)
                .clickable { onImageClick() },
            contentAlignment = Alignment.Center
        ) {
            if (imageUrl != null) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = stringResource(R.string.profile_image_cd),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            } else {
                Text(
                    text = initials,
                    fontFamily = Inter,
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp,
                    color = textColor
                )
            }
        }
    }
}

@Composable
fun EditProfileTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    supportingText: String?,
    cardColor: Color,
    textColor: Color,
    mutedColor: Color,
    borderColor: Color,
    readOnly: Boolean = false
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontFamily = Inter,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = textColor,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = value, onValueChange = onValueChange, singleLine = true,
            readOnly = readOnly,
            placeholder = { Text(text = placeholder, fontFamily = Inter, color = mutedColor) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = cardColor,
                unfocusedContainerColor = cardColor,
                focusedBorderColor = if (readOnly) borderColor.copy(alpha = 0.2f) else borderColor,
                unfocusedBorderColor = borderColor.copy(alpha = 0.5f),
                cursorColor = textColor,
                focusedTextColor = textColor,
                unfocusedTextColor = textColor
            ),
            supportingText = {
                Text(
                    text = supportingText ?: "",
                    fontFamily = Inter,
                    fontSize = 12.sp,
                    color = mutedColor,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        )
    }
}

@Composable
fun EditProfileBioField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    maxBioLength: Int,
    cardColor: Color,
    textColor: Color,
    mutedColor: Color,
    borderColor: Color
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontFamily = Inter,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = textColor,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = value, onValueChange = onValueChange, minLines = 3, maxLines = 5,
            placeholder = { Text(text = placeholder, fontFamily = Inter, color = mutedColor) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = cardColor,
                unfocusedContainerColor = cardColor,
                focusedBorderColor = borderColor,
                unfocusedBorderColor = borderColor.copy(alpha = 0.5f),
                cursorColor = textColor,
                focusedTextColor = textColor,
                unfocusedTextColor = textColor
            ),
            supportingText = {
                Text(
                    text = stringResource(R.string.bio_char_count, value.length, maxBioLength),
                    fontFamily = Inter,
                    fontSize = 12.sp,
                    color = mutedColor,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        )
    }
}
