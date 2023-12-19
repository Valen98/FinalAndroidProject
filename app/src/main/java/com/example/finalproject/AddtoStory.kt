package com.example.finalproject

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun AddToStory (){
    val context = LocalContext.current
    val newStory = Intent(context, NewStoryScreen::class.java)

    Column {
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)){
            IconButton(
                modifier = Modifier,
                onClick = {context.startActivity(newStory)},
                content = { Image(painter = painterResource(id = R.drawable.addtostory) , contentDescription = "Add to story button" ) }
            )
        }
        Row{
            Text(text = "Add to Story")
        }
        Spacer(modifier = Modifier.size(10.dp))
    }
}


@Preview
@Composable
fun PrevAddToStory (){
    AddToStory()
}