package com.client.storageclient.filesystem.api

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SearchByName() {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val nameState = remember {
            mutableStateOf("")
        }

        val ageState = remember {
             mutableIntStateOf(0)
        }

        val progressState = remember {
            mutableStateOf(false)
        }


        TextField (
            value = nameState.value,
            onValueChange = { nameState.value = it },
            textStyle = TextStyle(
                fontSize = 20.sp
            )
        )

        Text(text = "Entered Name: " + nameState.value, fontSize = 20.sp)

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            sendRequest(nameState, ageState, progressState)
        }) {
            Text(text = "Guess Age", fontSize = 40.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (progressState.value) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(80.dp),
                color = Color.Magenta,
                strokeWidth = 10.dp
            )
        } else {
            Text(
                text = "Result Age: " + ageState.intValue,
                fontSize = 40.sp
            )
        }
    }
}