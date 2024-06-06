package com.client.storageclient

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import androidx.navigation.compose.rememberNavController
import com.client.storageclient.filesystem.api.downloader.DownloadBroadcastReceiver
import com.client.storageclient.navigation.Navigation
import com.client.storageclient.ui.theme.StorageClientTheme
import java.io.File


class MainActivity : ComponentActivity() {
    private var downloadBroadcastReceiver: BroadcastReceiver? = null
    @SuppressLint("MutableCollectionMutableState")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createNotificationChannel(this)
        if (!Environment.isExternalStorageManager()) {
            val permissionIntent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
            startActivity(permissionIntent)
        }
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
            val currentProgress = remember { mutableFloatStateOf(0f) }
            val isLoading = remember { mutableStateOf(false) }

            val broadcastReceiver = DownloadBroadcastReceiver(fileName, chunkLocalUris, totalChunksNumber, currentProgress, isLoading)
            downloadBroadcastReceiver = broadcastReceiver

            registerReceiver(
                downloadBroadcastReceiver,
                IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
                RECEIVER_EXPORTED
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
                        currentProgress = currentProgress,
                        isLoading = isLoading
                    )
                }

            }
        }
    }

//    override fun onStop() {
//        super.onStop()
//        unregisterReceiver(downloadBroadcastReceiver!!)
//    }
}

private fun createNotificationChannel(context: Context) {
    val name = "notification_channel"
    val descriptionText = "notification channel"
    val importance = NotificationManager.IMPORTANCE_DEFAULT
    val channel = NotificationChannel("notification_channel", name, importance).apply {
        description = descriptionText
    }
    val notificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
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
