package com.example.wallet.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.wallet.R
import com.example.wallet.model.repository.DataStorePreferenceRepository
import com.example.wallet.model.viewmodel.login.LoginViewModel
import com.example.wallet.model.viewmodel.login.LoginViewModelFactory
import com.example.wallet.requests.LoginRequest
import com.example.wallet.ui.theme.BackgroundColor
import com.example.wallet.ui.theme.ButtonColor
import com.example.wallet.ui.theme.TextFieldColor
import com.example.wallet.ui.theme.TextFieldTextColor

@Composable
fun LoginScreen(
    navController: NavHostController,
    dataStorePreferenceRepository: DataStorePreferenceRepository
){
    val viewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(DataStorePreferenceRepository(LocalContext.current)))
    var username = ""
    var password = ""



    Surface(modifier = Modifier.fillMaxSize(),color = Color.White){
        Column (modifier = Modifier.fillMaxSize(),verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally){
            WelcomeText()
            PurposeImage()
            text_field(InputType = KeyboardType.Text,"E-mail address",IconImage = painterResource(id = R.drawable.ic_email), viewModel)
            SignIn(viewModel, navController)
            ForgotPasswordText()
        }
    }
}
@Composable
fun WelcomeText(){
    Text(text = "Welcome",
        color = Color.White,
        fontSize = 25.sp,
        modifier = Modifier.padding(top = 40.dp)
    )
}
@Composable
fun PurposeImage(){
    Image(painter = painterResource(id = R.drawable.logo_white), contentDescription = "LocationPin",
        modifier = Modifier.size(300.dp))
}
@Composable
fun text_field(InputType : KeyboardType,placeholder : String,IconImage : Painter, viewModel: LoginViewModel){
    var TextFieldUsernameState = remember{mutableStateOf("")}

    TextField(value = TextFieldUsernameState.value,
        onValueChange = { newInput -> TextFieldUsernameState.value = newInput
                        viewModel.updateViewModelFieldState("username", TextFieldUsernameState.value)},
        leadingIcon = {Image(painter = painterResource(id = R.drawable.ic_email), contentDescription = "email")},
        label = {Text(text = "E-mail address",color = MaterialTheme.colors.TextFieldTextColor)},
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        modifier = Modifier
            .padding(top = 0.dp)
            .background(color = MaterialTheme.colors.TextFieldColor)
    )
    var TextFieldPasswordState = remember{mutableStateOf("")}
    TextField(value = TextFieldPasswordState.value,
        onValueChange = { newInput -> TextFieldPasswordState.value = newInput
                        viewModel.updateViewModelFieldState("password", TextFieldPasswordState.value)},
        leadingIcon = {Image(painter = painterResource(id = R.drawable.ic_password), contentDescription = "password")},
        label = {Text(text = "Password",color = MaterialTheme.colors.TextFieldTextColor)},
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        modifier = Modifier
            .padding(top = 25.dp)
            .background(color = MaterialTheme.colors.TextFieldColor)
    )
}
@Composable
fun SignIn(viewModel: LoginViewModel, navController: NavHostController){
    Button(onClick = {
        viewModel.login(LoginRequest(username = viewModel.username, password = viewModel.password))
        navController.navigate("expanses")
                     },modifier = Modifier
        .padding(top = 25.dp)
        .requiredWidth(277.dp),
    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.ButtonColor)){
        Text(text = "Sign In")
    }
}
@Composable
fun ForgotPasswordText(){
    Text(text = "Forgot Password ?",color = MaterialTheme.colors.TextFieldTextColor,
        modifier = Modifier.padding(top = 70.dp))
}
