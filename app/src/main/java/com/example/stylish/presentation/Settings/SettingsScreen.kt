package com.example.stylish.presentation.Settings
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.stylish.R
import com.example.stylish.domain.models.UserProfile


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavHostController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    // State for selected profile image
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Gallery launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri : Uri? ->
    uri?.let {
        selectedImageUri = it
        Toast.makeText(context, "Profile picture updated", Toast.LENGTH_SHORT).show()
    }
    }
    // Auto-populate email from Firebase Auth
    LaunchedEffect(state.userProfile.emailAddress) {
        if (state.userProfile.emailAddress.isNotEmpty()) {
            // Email is automatically loaded from Firebase Auth
        }
    }

    // Local state for form fields
    var password by remember { mutableStateOf("") }
    var pincode by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var stateName by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var bankAccountNumber by remember { mutableStateOf("") }
    var accountHolderName by  remember { mutableStateOf("") }
    var ifscCode by remember { mutableStateOf("") }

    // Show success/error messages
    LaunchedEffect(state.saveSuccess) {
        if (state.saveSuccess){
            Toast.makeText(context,"Profile saved successfully!", Toast.LENGTH_SHORT).show()
            viewModel.resetSaveSuccess()
        }
    }

    LaunchedEffect(state.error) {
        state.error?.let { error ->
            Toast.makeText(context,error, Toast.LENGTH_SHORT).show()
            viewModel.clearError()
        }
    }

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Checkout",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp()}) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ){ paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
            ){
                Spacer(modifier = Modifier.height(16.dp))

                // Profile Image with Edit Icon
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(80.dp)
                        .clickable{ galleryLauncher.launch("image/*")}
                ){
                    // Display priority: Google profile photo > Selected image > Default avatar
                    when {
                        state.profilePhotoUrl != null -> {
                            // Show Google profile photo
                            AsyncImage(
                                model = state.profilePhotoUrl,
                                contentDescription = "Profile Picture",
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, Color(0xFFE0E0E0), CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                        selectedImageUri != null -> {
                            // Show selected image from gallery
                            AsyncImage(
                                model = selectedImageUri,
                                contentDescription = "Profile Picture",
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .border(2.dp,Color(0xFFE0E0E0), CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                        else -> {
                            // Show default avatar
                            Image(
                                painter = painterResource(id = R.drawable.ic_profile_avatar),
                                contentDescription = "Profile Picture",
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .border(2.dp,Color(0xFFE0E0E0),CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    // Edit icon badge
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(24.dp)
                            .background(Color(0xFFF83758),CircleShape)
                            .clickable{ galleryLauncher.launch("input/*")},
                        contentAlignment = Alignment.Center
                    ){
                        Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit Profile",
                            tint =Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Personal Details Section
                SectionHeader("Personal Details")
                Spacer(modifier = Modifier.height(12.dp))

                SettingsTextField(
                    label = "Email Address",
                    value = state.userProfile.emailAddress,
                    onValueChange = { }, // Read-only, auto-populated from Firebase Auth
                    enabled = false
                )

                Spacer(modifier = Modifier.height(12.dp))

                SettingsTextField(
                    label = "Password",
                    value  = password,
                    onValueChange = { password = it},
                    isPassword = true,
                    trailingText = "Change Password"
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Business Address Details Section
                SectionHeader("Business Address Details")
                Spacer(modifier = Modifier.height(12.dp))

                SettingsTextField(
                    label = "Pincode",
                    value = pincode,
                    onValueChange = { pincode = it }
                )

                Spacer(modifier = Modifier.height(12.dp))

                SettingsTextField(
                    label = "Address",
                    value = address,
                    onValueChange = { address = it }
                )

                Spacer(modifier = Modifier.height(12.dp))

                SettingsTextField(
                    label = "City",
                    value = city,
                    onValueChange = { city = it }
                )

                Spacer(modifier = Modifier.height(12.dp))

                SettingsTextField(
                    label = "State",
                    value = stateName,
                    onValueChange = { stateName = it }
                )

                Spacer(modifier = Modifier.height(12.dp))

                SettingsTextField(
                    label = "Country",
                    value = country,
                    onValueChange = { country = it}
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Bank Account Details Section
                SectionHeader("Bank Account Details")
                Spacer(modifier = Modifier.height(12.dp))

                SettingsTextField(
                    label = "Bank Account Number",
                    value = bankAccountNumber,
                    onValueChange = { bankAccountNumber = it }
                )

                Spacer(modifier = Modifier.height(12.dp))

                SettingsTextField(
                    label = "Account Holder's Name",
                    value  = accountHolderName,
                    onValueChange = { accountHolderName = it }
                )

                Spacer(modifier = Modifier.height(12.dp))

                SettingsTextField(
                    label = " IFSC Code",
                    value = ifscCode,
                    onValueChange = { ifscCode = it }
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Save Button
                Button(
                    onClick = {
                        val userProfile = UserProfile(
                            emailAddress = state.userProfile.emailAddress, // Use email from Firebase Auth
                            password = password,
                            pincode = pincode,
                            address = address,
                            city = city,
                            state = stateName,
                            country = country,
                            bankAccountNumber = bankAccountNumber,
                            accountHolderName = accountHolderName,
                            ifscCode = ifscCode
                        )
                        viewModel.updateUserProfile(userProfile)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF83578)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    enabled = !state.isSaving
                ){
                    if(state.isSaving){
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Save",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
@Composable
fun SectionHeader(title: String){
    Text(
        text = title,
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
        color = Color.Black
    )
}

@Composable
fun SettingsTextField(
    label: String,
    value : String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    trailingText: String? = null,
    enabled: Boolean = true
){
    Column {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
            trailingIcon = {
                if (trailingText != null){
                    Text(
                        text = trailingText,
                        fontSize = 12.sp,
                        color = Color(0xFFF83578),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color(0xFFF5F5F5),
                focusedIndicatorColor = Color(0xFFE0E0E0),
                unfocusedIndicatorColor = Color(0xFFE0E0E0),
                disabledIndicatorColor = Color(0xFFE0E0E0),
                disabledTextColor = Color.Gray
            ),
            shape = RoundedCornerShape(8.dp)
        )
    }
}