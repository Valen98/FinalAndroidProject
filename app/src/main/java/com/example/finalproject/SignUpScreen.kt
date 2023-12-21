package com.example.finalproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finalproject.ui.theme.FinalProjectTheme
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpScreen : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FinalProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: AccountViewModel by viewModels()
                    val db = viewModel.connectToDB()
                    viewModel.dbState.db = db

                    Column(modifier = Modifier.fillMaxSize()) {
                        Row(modifier = Modifier
                            .height(40.dp)
                            .padding(top = 8.dp)) {
                            IconButton(onClick = {finish()}){
                                Icon(imageVector  = Icons.Filled.ArrowBack, "Back",  modifier = Modifier.size(20.dp))
                            }
                        }
                        Row(modifier = Modifier.height(734.dp)) {
                            Username(viewModel,)
                        }
                        Column (modifier = Modifier.height(50.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally){
                            TextButton(onClick = { finish() }, modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                            ) {
                                Text(text = "Already have an account")
                            }
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, DelicateCoroutinesApi::class)
@Composable
fun Username(viewModel: AccountViewModel) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    val uContext = LocalContext.current

    var isEnabled by remember { mutableStateOf(false) }

    Column() {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp)) {
            Text(text = "What's your name?", fontSize=24.sp, fontWeight = FontWeight.Bold)
        }
        Row() {
            OutlinedTextField(
                value = fullName,
                onValueChange = {fullName = it},
                label = { Text("Full name") },
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                    .fillMaxWidth()
            )
        }
        Row() {
            OutlinedTextField(
                value = email,
                onValueChange = {email = it},
                label = { Text("Email") },
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                    .fillMaxWidth()
            )
        }

        isEnabled = fullName != "" && email != ""
        Button(onClick = {
            //If user with the username exist. Do not create the user.
            UserDataCompanion.username = fullName
            UserDataCompanion.email = email
            GlobalScope.launch(Dispatchers.Main) {
                checkUserExist(viewModel, uContext)
                Log.d("SignUp", "UserNotExist ${UserDataCompanion.userNotExist}")

            }
        }, enabled = isEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
        ) {
            Text(text = "Next")

        }


        OutlinedButton(onClick = { /*TODO*/ }, modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp)
        ) {
            Text(text = "Not now")
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
suspend fun checkUserExist(viewModel: AccountViewModel, uContext: Context) {
    val createPassword = Intent(uContext, PasswordScreen::class.java)
    val value = GlobalScope.async {
        withContext(Dispatchers.Default) {
            viewModel.checkUser(viewModel.connectToDB())
        }
    }
    println(value.await())
    if(UserDataCompanion.userNotExist) {
        uContext.startActivity(createPassword)
    }else {
        //Toast.makeText(uContext, "Account with that name already exists", Toast.LENGTH_LONG).show()
    }
}