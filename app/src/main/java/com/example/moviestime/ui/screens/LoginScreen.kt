package com.example.moviestime.ui.screens

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moviestime.R
import com.example.moviestime.viewmodel.AuthViewModel
// imports Google
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
// imports Facebook
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
// imports Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider

private val AccentColor = Color(0xFFC93A4D)

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var isRegisterMode by remember { mutableStateOf(false) }

    val uiState by authViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val activity = context as? Activity

    // --- Google Sign In Setup ---
    val token = stringResource(R.string.default_web_client_id)
    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(token)
            .requestEmail()
            .build()
    }
    val googleSignInClient = remember { GoogleSignIn.getClient(context, gso) }
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            authViewModel.signInWithGoogleToken(account.idToken!!)
        } catch (e: ApiException) {
            Log.w("LoginScreen", "Google sign in failed", e)
        }
    }

    val callbackManager = remember { CallbackManager.Factory.create() }
    val facebookLauncher = rememberLauncherForActivityResult(
        contract = LoginManager.getInstance().createLogInActivityResultContract(callbackManager, null)
    ) {
    }

    DisposableEffect(Unit) {
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                authViewModel.signInWithFacebookToken(result.accessToken.token)
            }
            override fun onCancel() { Log.d("LoginScreen", "Facebook login cancelled") }
            override fun onError(error: FacebookException) {
                Log.e("LoginScreen", "Facebook login error", error)
                authViewModel.onExternalSignInFailure(error.message ?: "Facebook Error")
            }
        })
        onDispose { LoginManager.getInstance().unregisterCallback(callbackManager) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.95f)),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.backkkkkk),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .offset(y = (-50).dp)
                .clip(RoundedCornerShape(bottomStart = 50.dp, bottomEnd = 50.dp)),
            alpha = 0.2f
        )

        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = if (isRegisterMode) "Create Account" else "Welcome Back",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 8.dp),
                textAlign = TextAlign.Center
            )

            Text(
                text = if (isRegisterMode) "Join MovieTime and explore!" else "Login to continue your movie world.",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 28.dp),
                textAlign = TextAlign.Center
            )

            if (isRegisterMode) {
                NeonTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = "Full Name",
                    icon = Icons.Default.Person
                )
                Spacer(Modifier.height(16.dp))
            }

            NeonTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                icon = Icons.Default.Email,
                keyboardType = KeyboardType.Email
            )

            Spacer(Modifier.height(16.dp))

            NeonTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                icon = Icons.Default.Lock,
                keyboardType = KeyboardType.Password,
                isPassword = true
            )

            if (uiState.error != null) {
                Spacer(Modifier.height(10.dp))
                Text(
                    text = uiState.error!!,
                    color = Color(0xFFFF5252),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(Modifier.height(26.dp))

            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(40.dp), color = AccentColor)
            } else {
                Button(
                    onClick = {
                        if (isRegisterMode) authViewModel.register(email, password, name)
                        else authViewModel.login(email, password)
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = MaterialTheme.shapes.large,
                    colors = ButtonDefaults.buttonColors(containerColor = AccentColor, contentColor = Color.White),
                    elevation = ButtonDefaults.buttonElevation(10.dp)
                ) {
                    Text(
                        text = if (isRegisterMode) "Sign Up" else "Login",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            TextButton(onClick = { isRegisterMode = !isRegisterMode }) {
                Text(
                    text = if (isRegisterMode) "Already have an account? Login" else "Don't have an account? Register",
                    color = AccentColor,
                    fontSize = 15.sp
                )
            }

            Spacer(Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Divider(modifier = Modifier.weight(1f), color = Color.White.copy(alpha = 0.3f))
                Text(
                    "  Or connect with  ",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
                Divider(modifier = Modifier.weight(1f), color = Color.White.copy(alpha = 0.3f))
            }

            Spacer(Modifier.height(20.dp))

            // --- أزرار الأيقونات (Icons) ---
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {

                // Google Icon Button
                IconButton(
                    onClick = {
                        googleSignInClient.signOut().addOnCompleteListener {
                            googleSignInLauncher.launch(googleSignInClient.signInIntent)
                        }
                    },
                    modifier = Modifier.size(50.dp) // حجم الزر
                ) {
                    Image(
                        // هام: تأكد أن لديك صورة باسم ic_google في مجلد drawable
                        painter = painterResource(id = R.drawable.icons8_google__1_),
                        contentDescription = "Google Login",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(Modifier.width(24.dp))

                // Facebook Icon Button
                IconButton(
                    onClick = {
                        facebookLauncher.launch(listOf("email", "public_profile"))
                    },
                    modifier = Modifier.size(50.dp)
                ) {
                    Image(
                        // هام: تأكد أن لديك صورة باسم ic_facebook في مجلد drawable
                        painter = painterResource(id = R.drawable.facebook),
                        contentDescription = "Facebook Login",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(Modifier.width(24.dp))

                //  Icon Button
                IconButton(
                    onClick = {
                        activity?.let { act ->
                            val provider = OAuthProvider.newBuilder("github.com")
                            provider.addCustomParameter("allow_signup", "false")
                            FirebaseAuth.getInstance()
                                .startActivityForSignInWithProvider(act, provider.build())
                                .addOnSuccessListener {
                                    authViewModel.onExternalSignInSuccess()
                                }
                                .addOnFailureListener { e ->
                                    authViewModel.onExternalSignInFailure(e.message ?: "GitHub Error")
                                }
                        }
                    },
                    modifier = Modifier.size(50.dp)
                ) {
                    Image(
                        // هام: تأكد أن لديك صورة باسم ic_github في مجلد drawable
                        painter = painterResource(id = R.drawable.icons8_linkedin),
                        contentDescription = "GitHub Login",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    }
}

@Composable
fun NeonTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = AccentColor.copy(alpha = 0.9f)) },
        leadingIcon = { Icon(icon, contentDescription = null, tint = AccentColor) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.Black.copy(alpha = 0.7f),
            unfocusedContainerColor = Color.Black.copy(alpha = 0.5f),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            cursorColor = AccentColor,
            focusedBorderColor = AccentColor,
            unfocusedBorderColor = Color.White.copy(alpha = 0.3f)
        )
    )
}