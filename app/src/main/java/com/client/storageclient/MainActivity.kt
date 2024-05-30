package com.client.storageclient

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.client.storageclient.composables.FileCard
import com.client.storageclient.composables.FilesList
import com.client.storageclient.filesystem.File
import com.client.storageclient.filesystem.Folder
import com.client.storageclient.filesystem.api.downloader.DownloadBroadcastReceiver
import com.client.storageclient.navigation.Navigation
import com.client.storageclient.ui.theme.StorageClientTheme

class MainActivity : ComponentActivity() {
    private var downloadBroadcastReceiver: BroadcastReceiver? = null
    @SuppressLint("MutableCollectionMutableState")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val chunkLocalUris = remember {
                mutableStateOf(mutableListOf<Uri>())
            }
            val totalChunksNumber = remember {
                mutableIntStateOf(0)
            }
            val fileName = remember {
                mutableStateOf("")
            }
            val isEnabled = remember { mutableStateOf(true) }

            val broadcastReceiver = DownloadBroadcastReceiver(fileName, chunkLocalUris, totalChunksNumber, isEnabled)
            downloadBroadcastReceiver = broadcastReceiver

            registerReceiver(
                downloadBroadcastReceiver,
                IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
                RECEIVER_NOT_EXPORTED
            )
            StorageClientTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    Navigation(
                        navController = navController,
                        fileNameState = fileName,
                        chunkLocalUris = chunkLocalUris,
                        totalChunksNumber = totalChunksNumber,
                        isEnabled = isEnabled
                    )
                }

            }
        }
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(downloadBroadcastReceiver!!)
    }
}

@Preview(showBackground = true)
@Composable
fun FilesListPreview() {
    StorageClientTheme {
        val navController = rememberNavController()
//        Navigation(navController = navController)
    }
}

fun fileClick() {
    print("Hello!")
}