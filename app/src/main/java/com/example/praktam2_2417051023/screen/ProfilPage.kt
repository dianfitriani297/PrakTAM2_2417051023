package com.example.praktam2_2417051023.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.praktam2_2417051023.component.BottomNavBar
import com.example.praktam2_2417051023.data.datastore.UserPreferencesRepository
import com.example.praktam2_2417051023.ui.theme.GreenPrimary
import com.example.praktam2_2417051023.ui.theme.GreenSoft
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ProfilePage(
    navController: NavController
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val userRepo = remember { UserPreferencesRepository(context) }
    val snackbar = remember { SnackbarHostState() }

    var namaLengkap by remember { mutableStateOf("") }
    var namaPanggilan by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var isEditMode by remember { mutableStateOf(false) }

    var tempNamaLengkap by remember { mutableStateOf("") }
    var tempNamaPanggilan by remember { mutableStateOf("") }
    var tempPassword by remember { mutableStateOf("") }
    var tempKonfirmasiPassword by remember { mutableStateOf("") }

    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }

    var logoutClickCount by remember { mutableIntStateOf(0) }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        namaLengkap = userRepo.getNamaLengkap()
        namaPanggilan = userRepo.getNamaPanggilan()
        password = userRepo.getPassword()

        tempNamaLengkap = namaLengkap
        tempNamaPanggilan = namaPanggilan
        tempPassword = password
        tempKonfirmasiPassword = password
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        bottomBar = {
            BottomNavBar(navController)
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbar,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(bottom = paddingValues.calculateBottomPadding())
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 0.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        navController.popBackStack()
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Kembali",
                        tint = GreenPrimary
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "Profile",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    color = GreenPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .background(color = GreenSoft, shape = CircleShape)
                        .border(2.dp, GreenPrimary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    val inisial = if (namaLengkap.isNotBlank()) {
                        val kata = namaLengkap.trim().split("\\s+".toRegex())
                        if (kata.size >= 2) {
                            "${kata[0].take(1)}${kata[1].take(1)}".uppercase()
                        } else {
                            namaLengkap.take(2).uppercase()
                        }
                    } else {
                        "DF"
                    }

                    Text(
                        text = inisial,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        color = GreenPrimary
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = namaLengkap,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "@${namaPanggilan.lowercase()}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(3.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    ProfileItemRow(
                        icon = Icons.Filled.Person,
                        label = "Nama Lengkap",
                        value = namaLengkap,
                        editValue = tempNamaLengkap,
                        isEditing = isEditMode,
                        onValueChange = {
                            tempNamaLengkap = it
                        }
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = Color(0xFFF5F5F5)
                    )

                    ProfileItemRow(
                        icon = Icons.Filled.Person,
                        label = "Nama Panggilan",
                        value = namaPanggilan,
                        editValue = tempNamaPanggilan,
                        isEditing = isEditMode,
                        onValueChange = {
                            tempNamaPanggilan = it
                        }
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = Color(0xFFF5F5F5)
                    )

                    ProfileItemRow(
                        icon = Icons.Filled.Lock,
                        label = "Kata Sandi",
                        value = "••••••",
                        editValue = tempPassword,
                        isEditing = isEditMode,
                        isPassword = true,
                        showConfirmationField = isEditMode,
                        confirmationValue = tempKonfirmasiPassword,
                        isPasswordVisible = isPasswordVisible,
                        isConfirmPasswordVisible = isConfirmPasswordVisible,
                        onTogglePasswordVisibility = {
                            isPasswordVisible = !isPasswordVisible
                        },
                        onToggleConfirmPasswordVisibility = {
                            isConfirmPasswordVisible = !isConfirmPasswordVisible
                        },
                        onConfirmationValueChange = {
                            tempKonfirmasiPassword = it
                        },
                        onValueChange = {
                            tempPassword = it
                        }
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = Color(0xFFF5F5F5)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (!isEditMode) {
                        Button(
                            onClick = {
                                tempNamaLengkap = namaLengkap
                                tempNamaPanggilan = namaPanggilan
                                tempPassword = password
                                tempKonfirmasiPassword = password
                                isPasswordVisible = false
                                isConfirmPasswordVisible = false
                                isEditMode = true
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .height(44.dp),
                            shape = RoundedCornerShape(50.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GreenPrimary
                            )
                        ) {
                            Text(
                                text = "Edit Profil",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    } else {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    isEditMode = false
                                },
                                modifier = Modifier
                                    .height(44.dp)
                                    .weight(1f),
                                shape = RoundedCornerShape(50.dp),
                                border = BorderStroke(1.5.dp, Color.Gray),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color.Gray
                                )
                            ) {
                                Text(
                                    text = "Batal",
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Button(
                                onClick = {
                                    val namaLengkapBaru = tempNamaLengkap.trim()
                                    val namaPanggilanBaru = tempNamaPanggilan.trim()
                                    val passwordBaru = tempPassword.trim()
                                    val konfirmasiPasswordBaru = tempKonfirmasiPassword.trim()

                                    when {
                                        namaLengkapBaru.length < 3 -> {
                                            scope.launch {
                                                snackbar.showSnackbar("Nama lengkap minimal 3 karakter")
                                            }
                                        }

                                        namaPanggilanBaru.length < 3 -> {
                                            scope.launch {
                                                snackbar.showSnackbar("Nama panggilan minimal 3 karakter")
                                            }
                                        }

                                        passwordBaru.length < 6 -> {
                                            scope.launch {
                                                snackbar.showSnackbar("Password minimal 6 karakter")
                                            }
                                        }

                                        passwordBaru != konfirmasiPasswordBaru -> {
                                            scope.launch {
                                                snackbar.showSnackbar("Konfirmasi kata sandi tidak cocok")
                                            }
                                        }

                                        else -> {
                                            scope.launch {
                                                userRepo.updateNamaLengkap(namaLengkapBaru)
                                                userRepo.updateNamaPanggilan(namaPanggilanBaru)
                                                userRepo.updatePassword(passwordBaru)

                                                namaLengkap = namaLengkapBaru
                                                namaPanggilan = namaPanggilanBaru
                                                password = passwordBaru

                                                tempNamaLengkap = namaLengkapBaru
                                                tempNamaPanggilan = namaPanggilanBaru
                                                tempPassword = passwordBaru
                                                tempKonfirmasiPassword = passwordBaru

                                                isEditMode = false
                                                snackbar.showSnackbar("Profil berhasil diperbarui")
                                            }
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .height(44.dp)
                                    .weight(1f),
                                shape = RoundedCornerShape(50.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = GreenPrimary
                                )
                            ) {
                                Text(
                                    text = "Simpan",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    TextButton(
                        onClick = {
                            scope.launch {
                                logoutClickCount++

                                if (logoutClickCount == 1) {
                                    snackbar.showSnackbar("Klik sekali lagi untuk keluar")
                                    delay(3000)
                                    logoutClickCount = 0
                                } else {
                                    isLoading = true
                                    delay(1000)
                                    userRepo.logout()
                                    isLoading = false

                                    navController.navigate("splash") {
                                        popUpTo(0) {
                                            inclusive = true
                                        }
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.Red,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = "Keluar",
                                tint = Color.Red,
                                modifier = Modifier.size(20.dp)
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = "Keluar dari Akun",
                                color = Color.Red,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ProfileItemRow(
    icon: ImageVector,
    label: String,
    value: String,
    editValue: String,
    isEditing: Boolean,
    isPassword: Boolean = false,
    showConfirmationField: Boolean = false,
    confirmationValue: String = "",
    isPasswordVisible: Boolean = false,
    isConfirmPasswordVisible: Boolean = false,
    onTogglePasswordVisibility: () -> Unit = {},
    onToggleConfirmPasswordVisibility: () -> Unit = {},
    onConfirmationValueChange: (String) -> Unit = {},
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = GreenPrimary,
                modifier = Modifier.size(22.dp)
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Text(
                    text = label,
                    fontSize = 11.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(2.dp))

                if (!isEditing) {
                    Text(
                        text = value,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                }
            }
        }

        if (isEditing) {
            Spacer(modifier = Modifier.height(6.dp))

            OutlinedTextField(
                value = editValue,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 36.dp),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                visualTransformation = if (isPassword && !isPasswordVisible) {
                    PasswordVisualTransformation()
                } else {
                    VisualTransformation.None
                },
                trailingIcon = {
                    if (isPassword) {
                        IconButton(
                            onClick = onTogglePasswordVisibility
                        ) {
                            val iconEye = if (isPasswordVisible) {
                                Icons.Filled.VisibilityOff
                            } else {
                                Icons.Filled.Visibility
                            }

                            Icon(
                                imageVector = iconEye,
                                contentDescription = "Lihat sandi",
                                tint = Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GreenPrimary,
                    unfocusedBorderColor = Color(0xFFDDDDDD)
                )
            )

            if (isPassword && showConfirmationField) {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Konfirmasi Kata Sandi",
                    fontSize = 11.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(start = 36.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))

                OutlinedTextField(
                    value = confirmationValue,
                    onValueChange = onConfirmationValueChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 36.dp),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    visualTransformation = if (!isConfirmPasswordVisible) {
                        PasswordVisualTransformation()
                    } else {
                        VisualTransformation.None
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = onToggleConfirmPasswordVisibility
                        ) {
                            val iconEyeConfirm = if (isConfirmPasswordVisible) {
                                Icons.Filled.VisibilityOff
                            } else {
                                Icons.Filled.Visibility
                            }

                            Icon(
                                imageVector = iconEyeConfirm,
                                contentDescription = "Lihat konfirmasi sandi",
                                tint = Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GreenPrimary,
                        unfocusedBorderColor = Color(0xFFDDDDDD)
                    )
                )
            }
        }
    }
}