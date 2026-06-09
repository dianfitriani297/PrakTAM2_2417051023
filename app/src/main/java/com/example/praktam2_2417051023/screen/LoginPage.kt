package com.example.praktam2_2417051023.screen

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.praktam2_2417051023.component.ZoopediaImage
import com.example.praktam2_2417051023.data.datastore.UserPreferencesRepository
import com.example.praktam2_2417051023.ui.theme.GreenPrimary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginPage(
    navController: NavController,
    logoUrl: String
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val userRepo = remember { UserPreferencesRepository(context) }
    val snackbar = remember { SnackbarHostState() }

    var namaPanggilan by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var passwordTerlihat by remember { mutableStateOf(false) }
    var sedangLoading by remember { mutableStateOf(false) }

    var errorNamaPanggilan by remember { mutableStateOf("") }
    var errorPassword by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF7FFF7),
                        Color(0xFFEAF8EA),
                        Color(0xFFC8F7CB)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 26.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(38.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(5.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (logoUrl.isNotEmpty()) {
                        ZoopediaImage(
                            imageUrl = logoUrl,
                            contentDescription = "Logo Zoopedia",
                            modifier = Modifier.size(104.dp),
                            contentScale = ContentScale.Fit
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "MASUK AKUN",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        color = GreenPrimary,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    Text(
                        text = "NAMA PANGGILAN",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color(0xFF444444),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp)
                    )

                    OutlinedTextField(
                        value = namaPanggilan,
                        onValueChange = {
                            namaPanggilan = it
                            errorNamaPanggilan = ""
                        },
                        placeholder = {
                            Text(
                                text = "masukkan nama panggilan",
                                color = Color(0xFFBDBDBD),
                                fontSize = 14.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = null,
                                tint = GreenPrimary
                            )
                        },
                        isError = errorNamaPanggilan.isNotEmpty(),
                        supportingText = {
                            if (errorNamaPanggilan.isNotEmpty()) {
                                Text(
                                    text = errorNamaPanggilan,
                                    color = Color.Red,
                                    fontSize = 12.sp
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 56.dp),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                focusManager.moveFocus(FocusDirection.Down)
                            }
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GreenPrimary,
                            unfocusedBorderColor = Color(0xFFCCCCCC),
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "PASSWORD",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = Color(0xFF444444),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp)
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            errorPassword = ""
                        },
                        placeholder = {
                            Text(
                                text = "masukkan password",
                                color = Color(0xFFBDBDBD),
                                fontSize = 14.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Lock,
                                contentDescription = null,
                                tint = GreenPrimary
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    passwordTerlihat = !passwordTerlihat
                                }
                            ) {
                                Icon(
                                    imageVector = if (passwordTerlihat) {
                                        Icons.Filled.Visibility
                                    } else {
                                        Icons.Filled.VisibilityOff
                                    },
                                    contentDescription = null,
                                    tint = Color.Gray
                                )
                            }
                        },
                        visualTransformation = if (passwordTerlihat) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        isError = errorPassword.isNotEmpty(),
                        supportingText = {
                            if (errorPassword.isNotEmpty()) {
                                Text(
                                    text = errorPassword,
                                    color = Color.Red,
                                    fontSize = 12.sp
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 56.dp),
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                            }
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GreenPrimary,
                            unfocusedBorderColor = Color(0xFFCCCCCC),
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            val namaInput = namaPanggilan.trim()
                            val passwordInput = password.trim()
                            var dataValid = true

                            if (namaInput.isEmpty()) {
                                errorNamaPanggilan = "Nama panggilan tidak boleh kosong"
                                dataValid = false
                            } else if (namaInput.length < 3) {
                                errorNamaPanggilan = "Nama panggilan minimal 3 karakter"
                                dataValid = false
                            }

                            if (passwordInput.isEmpty()) {
                                errorPassword = "Password tidak boleh kosong"
                                dataValid = false
                            } else if (passwordInput.length < 6) {
                                errorPassword = "Password minimal 6 karakter"
                                dataValid = false
                            }

                            if (dataValid) {
                                scope.launch {
                                    sedangLoading = true
                                    delay(1200)

                                    val berhasilLogin = userRepo.loginUser(
                                        namaPanggilan = namaInput,
                                        password = passwordInput
                                    )

                                    sedangLoading = false

                                    if (berhasilLogin) {
                                        navController.navigate("home") {
                                            popUpTo("splash") {
                                                inclusive = true
                                            }
                                            launchSingleTop = true
                                        }
                                    } else {
                                        snackbar.showSnackbar(
                                            "Nama panggilan atau password tidak sesuai"
                                        )
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(50.dp),
                        enabled = !sedangLoading,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GreenPrimary,
                            disabledContainerColor = Color(0xFFB8C8CD)
                        )
                    ) {
                        if (sedangLoading) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text = "Memverifikasi...",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        } else {
                            Text(
                                text = "Masuk",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            HorizontalDivider(
                color = Color(0xFFCCCCCC),
                thickness = 0.5.dp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Belum punya akun? ",
                    color = Color.Gray,
                    fontSize = 14.sp
                )

                Text(
                    text = "Daftar di sini",
                    color = GreenPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable {
                        navController.navigate("register")
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        SnackbarHost(
            hostState = snackbar,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        )
    }
}