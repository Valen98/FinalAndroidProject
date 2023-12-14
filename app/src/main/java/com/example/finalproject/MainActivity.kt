package com.example.finalproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.finalproject.ui.theme.FinalProjectTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

//Things to do in "MainScreen":
// 1. USER ACTIONS:
//      - Add to story button -> takes you to media selection screen (media selection screen (capture from device) -> then to a share screen to post to story
//      - Messages button -> takes you to messages screen
//2. SUGGESTED FOLLOWS:
//      - Should be a side-scrollable list of contacts from the phone
//3. FEED SECTION:
//      - vertically scrollable list of posts from users that have been followed (or random users if no follows)
//      - Like button should be pressable for users scrolling the feed
//4. QUICK ACTION TOOLBAR:
//      - Should have 3 buttons avaialble to all screens on the app
//          - Home Button (takes you back to main page)
//          - Create new post (similar to 'add a story' but for the feed)
//          - Profile button (takes users to the their profile page.)
//

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FinalProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //Db connection
                    val viewModel: AccountViewModel by viewModels()
                    val db = viewModel.connectToDB()
                    viewModel.dbState.db = db

                    auth = FirebaseAuth.getInstance()
                    val uContext = LocalContext.current
                    val login = Intent(uContext, LoginPage::class.java)
                    val user = auth.currentUser
                    if(user == null) {
                        uContext.startActivity(login)
                    }else {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            //TODO: Should not be able to get into this activity when you are not signed in.

                            Button(
                                onClick = {
                                    viewModel.onAction(UserAction.Logout)
                                    uContext.startActivity(login)
                                }, modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(end = 16.dp, top = 16.dp)
                            ) {
                                Text(text = "Logout")
                            }

                            //TODO: Fill this with more post, story and recommended follower and so on.
                            Text(text = "Welcome")

                            //TODO: Need to make a profile page with the correct data from the database.
                        }
                    }
                }
            }
        }
    }
}



