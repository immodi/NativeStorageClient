package com.client.storageclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.client.storageclient.filesystem.File
import com.client.storageclient.filesystem.FileSystemObject
import com.client.storageclient.filesystem.Folder
import com.client.storageclient.ui.theme.StorageClientTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StorageClientTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val file1= File(id = 0, name = "havana.mp4", sizeInBytes = 123456789L)
                    val file2 = Folder(id = 1, name = "test")
                    val testArr = arrayOf(file1, file2)
                    FilesList(fileNames = testArr)
                }
            }
        }
    }
}

@Composable
fun FilesList(fileNames: Array<FileSystemObject>, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
        ,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            modifier = Modifier
                .padding(
                    top = 10.dp,
                    bottom = 10.dp,
                    start = 10.dp
                ),
            text = stringResource(R.string.file_system),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        fileNames.forEach {
            Surface(
                onClick = { fileClick() },
                modifier = Modifier
                    .padding(
                        vertical = 2.dp
                    )
                    .drawBehind {
                        val borderSize = Dp.Hairline
                        drawLine(
                            color = Color.White,
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = borderSize.toPx()
                        )
                    }
                    .fillMaxWidth()
                    .height(40.dp),
            ) {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        modifier = Modifier
                            .padding(
                                start = 10.dp,
                                end = 10.dp
                            )
                            .wrapContentHeight(Alignment.CenterVertically),
                        text = it.getIcon() + it.name,
                        textAlign = TextAlign.Start,
                    )
                    if (it is File) {
                        Text(
                            modifier = Modifier
                                .padding(end = 10.dp),
                            text = it.getSizeString()
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FilesListPreview() {
    StorageClientTheme {
        val file1 = File(id = 0, name = "test.mp4", sizeInBytes = 123456789L)
        val file2 = File(id = 1, name = "havana.mp3", sizeInBytes = 2048*6L)
        val testArr = arrayOf<FileSystemObject>(file1, file2)
        FilesList(fileNames = testArr)
    }
}

fun fileClick() {
    print("Hello!")
}