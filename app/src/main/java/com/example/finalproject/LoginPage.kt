package com.example.finalproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.finalproject.ui.theme.FinalProjectTheme
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle


class LoginPage : ComponentActivity() {

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FinalProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column (modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        val context = LocalContext.current
                        val accountVM: AccountViewModel by viewModels()
                        val signInModel = viewModel<SignInViewModel>()
                        val state by signInModel.state.collectAsStateWithLifecycle()
                        val db = accountVM.connectToDB()
                        accountVM.dbState.db = db

                        val launcher = rememberLauncherForActivityResult(
                            contract = ActivityResultContracts.StartIntentSenderForResult(),
                            onResult = {result ->
                                if(result.resultCode == RESULT_OK) {
                                    lifecycleScope.launch {
                                        val signInResult = googleAuthUiClient.SignInWithIntent(
                                            intent = result.data ?: return@launch
                                        )
                                        signInModel.onSignInResult(signInResult)
                                    }
                                }
                            }
                        )

                        LaunchedEffect(key1 = state.isSignedInSuccessful) {
                            if (state.isSignedInSuccessful) {
                                Toast.makeText(
                                    applicationContext,
                                    "Sign in Successful",
                                    Toast.LENGTH_LONG
                                ).show()
                                startActivity(Intent(context, MainActivity::class.java))
                                finish()
                            }
                        }
                            SignInForm(context, accountVM)
                            SignInScreen(
                                state = state,
                                onSignInClick = {
                                    lifecycleScope.launch {
                                        val signInIntentSender = googleAuthUiClient.signIn()
                                        launcher.launch(
                                            IntentSenderRequest.Builder(
                                                signInIntentSender ?: return@launch
                                            ).build()
                                        )
                                        Log.d("state", "This is the login state: ${state.isSignedInSuccessful}")
                                    }
                                }
                            )
                        if(state.isSignedInSuccessful) {
                            Log.d("SignIn", " Sign in state is now:  ${state.isSignedInSuccessful}")
                            finish()
                        }
                        CreateAccount(context)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInForm(context: Context, viewModel: AccountViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val login = Intent(context, MainActivity::class.java)

    Column(modifier = Modifier.size(width = 300.dp, height = 500.dp)) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                // Please provide localized description for accessibility services
                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = {passwordVisible = !passwordVisible}){
                    Icon(imageVector  = image, description)
                }

            }
        )

        Button(onClick = {
            UserDataCompanion.email = email
            UserDataCompanion.password = password
            viewModel.onAction(UserAction.LoginUser)
            if(UserDataCompanion.isSignedIn) {
                context.startActivity(login)
            }else {
                Toast.makeText(context, "Wrong Email or Password", Toast.LENGTH_LONG).show()
            }
            Log.d("Login", "This is login with email: $email")
         }, modifier = Modifier
            .fillMaxWidth()
            .padding(end = 16.dp, top = 16.dp)
        ) {
            Text(text = "Login")
        }
        TextButton(
            onClick = { /*TODO*/},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Forgot Password?")
        }
    }
}

@Composable
fun CreateAccount(context: Context) {
    val createAccount = Intent(context, SignUpScreen::class.java)
    Column (modifier = Modifier.size(width = 300.dp, height = 50.dp)){
        OutlinedButton(onClick = { context.startActivity(createAccount) }, modifier = Modifier
            .fillMaxWidth()
            .padding(end = 16.dp, top = 16.dp)
        ) {
            Text(text = "Create an account")
        }
    }
}


@Composable
fun SignInScreen(
    state: SignInState,
    onSignInClick: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = state.signInError){
        state.signInError?.let {error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()

        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Button(onClick = onSignInClick) {
            Text(text = "Login with Google")

        }
    }

}

