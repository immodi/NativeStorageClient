package com.client.storageclient.composables

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.client.storageclient.filesystem.File
import com.client.storageclient.filesystem.Folder
import com.client.storageclient.ui.theme.StorageClientTheme

@Composable
fun FileCard(fileId: Int, fileName: String, fileSize: String) {
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
                .padding(20.dp)
        ) {
            val fontSize = 20.sp;
            Text(
                modifier = Modifier
                    .padding(
                        top = 10.dp,
                        bottom = 20.dp
                    ),
                text = "Name: $fileName",
                fontSize = fontSize
            )

            Text(
                modifier = Modifier
                    .padding(
                        top = 10.dp,
                        bottom = 20.dp
                    ),
                text = "Size: $fileSize",
                fontSize = fontSize
            )
        }

        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ButtonComposable{ Log.d("tag", fileId.toString()) }
        }
    }
}

@Composable
fun ButtonComposable(onClick: () -> Unit) {
    Button (
        modifier = Modifier
            .width(300.dp)
            .height(50.dp),
        shape = MaterialTheme.shapes.medium,
        onClick = { onClick() },
    ) {
        Text(
            modifier = Modifier,
            text = "Download",
            fontSize = 18.sp,
            color = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FileCardPreview() {
    StorageClientTheme {
        val file1 = File(id = 0, name = "havana.mp4", sizeInBytes = 123456789L)
        FileCard(fileId = 1, fileName = file1.name, fileSize = file1.getSizeString() )
    }
}