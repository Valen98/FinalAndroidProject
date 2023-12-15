package com.example.finalproject

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.HeartBroken
import androidx.compose.material.icons.outlined.Message
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Header(context: Context) {
    val contact = Intent(context, ContactScreen::class.java)
    Column(
        modifier = Modifier.fillMaxWidth().height(50.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Row() {
            Row (
                modifier = Modifier.padding(start = 8.dp, top = 8.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start){
                Text(text = "Instagram", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.End,
                ){
                IconButton(onClick = { /* TODO */ }) {
                    Icon(
                        imageVector = Icons.Outlined.HeartBroken,
                        "Notification",
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(onClick = { context.startActivity(contact) }) {
                    Icon(
                        imageVector = Icons.Outlined.Message,
                        "Message",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}