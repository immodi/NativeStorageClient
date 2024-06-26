package com.client.storageclient.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.client.storageclient.R
import com.client.storageclient.filesystem.File
import com.client.storageclient.filesystem.FileSystemObject
import com.client.storageclient.filesystem.Folder
import com.client.storageclient.filesystem.api.refreshFileSystem
import com.client.storageclient.navigation.Routes

@Composable
fun FilesList(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    dirId: Int
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
        ,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start,
    ) {
        val fileSystemState = remember {
            mutableStateOf(listOf<FileSystemObject>())
        }

        val progressState = remember {
            mutableStateOf(false)
        }

        LaunchedEffect(Unit) {
            refreshFileSystem(fileSystemState, progressState, dirId)
        }

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
        fileSystemState.value.forEach {
            Surface(
                onClick = {
                    if (it is File) {
                        navController.navigate(Routes.FileCard.route + "/${it.name}/${it.getSizeString()}/${it.id}")
                    } else if (it is Folder) {
                        navController.navigate(Routes.FileSystem.route + "/${it.id}")
                    }
                },
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
                        text = it.getIcon() + getFileOrFolderName(it.name, it is File),
                        textAlign = TextAlign.Start,
                    )
                    if (it is File) {
                        Text(
                            modifier = Modifier
                                .padding(end = 10.dp),
                            text = it.getSizeString(),
                        )
                    }
                }
            }
        }
    }
}


fun getFileOrFolderName(name: String, isFile: Boolean): String {
    return if (isFile) {
        if (name.length > 20) {
            name.substring(0, 21) + "..."
        } else {
            name
        }
    } else {
        name.substringAfterLast("/")
    }
}