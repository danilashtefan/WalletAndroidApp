package com.example.wallet.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.wallet.R
import com.example.wallet.helpers.Strings
import com.example.wallet.model.repository.DataStorePreferenceRepository
import com.example.wallet.model.viewmodel.login.LoginViewModel
import com.example.wallet.model.viewmodel.login.LoginViewModelFactory
import com.example.wallet.requests.LoginRequest
import com.example.wallet.requests.RegisterRequest
import com.example.wallet.ui.theme.ButtonColor
import com.example.wallet.ui.theme.TextFieldColor
import com.example.wallet.ui.theme.TextFieldTextColor

@Composable
fun LoginScreen(
    navController: NavHostController,
    dataStorePreferenceRepository: DataStorePreferenceRepository
) {
    val viewModel: LoginViewModel =
        viewModel(factory = LoginViewModelFactory(DataStorePreferenceRepository(LocalContext.current)))
    var username = ""
    var password = ""


    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        Column(
            modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var showAlertDialog = viewModel.showAlertDialog.value
            if (showAlertDialog) {
                OneButtonAlertDialogComponent(
                    bodyText = { Text(viewModel.dialogText.value, color = Color.White) },
                    buttonText = "DISMISS",
                    onDismiss = { viewModel.showAlertDialog.value = false })
            }
            WelcomeText()
            PurposeImage()
            text_field(
                InputType = KeyboardType.Text,
                "E-mail address",
                IconImage = painterResource(id = R.drawable.ic_email),
                viewModel
            )
            SignIn(viewModel, navController)
        }
    }
}


@Composable
fun WelcomeText() {
    Text(
        text = "Welcome",
        color = Color.White,
        fontSize = 25.sp,
        modifier = Modifier.padding(top = 40.dp)
    )
}


@Composable
fun OneButtonAlertDialogComponent(
    onDismiss: () -> Unit,
    bodyText: @Composable () -> Unit,
    buttonText: String,
    testTag:String = " "
) {
    val context = LocalContext.current
    AlertDialog(
        modifier = Modifier.testTag(testTag),
        onDismissRequest = onDismiss,
        title = { Text(text = "Wallet", color = Color.White) },

        text = bodyText,
        confirmButton = {
            TextButton(
                onClick = onDismiss
            ) {

                Text(buttonText, color = Color.White)
            }
        },
        backgroundColor = colorResource(id = R.color.purple_200),
        contentColor = Color.White
    )
}


@Composable
fun PurposeImage() {
    Image(
        painter = painterResource(id = R.drawable.logo_white), contentDescription = "LocationPin",
        modifier = Modifier.size(300.dp)
    )
}

@Composable
fun text_field(
    InputType: KeyboardType,
    placeholder: String,
    IconImage: Painter,
    viewModel: LoginViewModel
) {
    var TextFieldUsernameState = remember { mutableStateOf("") }

    TextField(
        value = TextFieldUsernameState.value,
        onValueChange = { newInput ->
            TextFieldUsernameState.value = newInput
            viewModel.updateViewModelFieldState("username", TextFieldUsernameState.value)
        },
        leadingIcon = {
            Image(
                painter = painterResource(id = R.drawable.ic_email),
                contentDescription = "email"
            )
        },
        label = { Text(text = "Login", color = MaterialTheme.colors.TextFieldTextColor) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        modifier = Modifier
            .padding(top = 0.dp)
            .background(color = MaterialTheme.colors.TextFieldColor)
            .testTag(Strings.USERNAME_TEXTFIELD_TAG)
    )
    var TextFieldPasswordState = remember { mutableStateOf("") }
    var passwordVisibility: Boolean by remember { mutableStateOf(false) }
    TextField(
        value = TextFieldPasswordState.value,
        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
        onValueChange = { newInput ->
            TextFieldPasswordState.value = newInput
            viewModel.updateViewModelFieldState("password", TextFieldPasswordState.value)
        },
        leadingIcon = {
            Image(
                painter = painterResource(id = R.drawable.ic_password),
                contentDescription = "password"
            )
        },
        trailingIcon = {
            val image = if (passwordVisibility)
                Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff

            // Please provide localized description for accessibility services
            val description = if (passwordVisibility) "Hide password" else "Show password"

            IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                Icon(imageVector = image, description)
            }
        },
        label = { Text(text = "Password", color = MaterialTheme.colors.TextFieldTextColor) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        modifier = Modifier
            .padding(top = 25.dp)
            .background(color = MaterialTheme.colors.TextFieldColor)
            .testTag(Strings.PASSWORD_TEXTFIELD_TAG)
    )
}

@Composable
fun SignIn(viewModel: LoginViewModel, navController: NavHostController) {
    Column {
        Button(
            onClick = {
                var result = viewModel.login(
                    LoginRequest(
                        username = viewModel.username,
                        password = viewModel.password
                    )
                )
                if (result) {
                    navController.navigate("expanses") {
                        popUpTo("login") { inclusive = true }
                    }
                }

            }, modifier = Modifier
                .padding(top = 25.dp)
                .requiredWidth(277.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.ButtonColor)
        ) {
            Text(text = "Sign In")
        }

        Button(
            onClick = {
                viewModel.register(
                    RegisterRequest(
                        name = viewModel.username,
                        username = viewModel.username,
                        password = viewModel.password
                    )
                )
            }, modifier = Modifier
                .padding(top = 25.dp)
                .requiredWidth(277.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.ButtonColor)
        ) {
            Text(text = "Register")
        }
    }
}

