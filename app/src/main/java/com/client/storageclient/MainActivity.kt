package com.client.storageclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.client.storageclient.composables.FileCard
import com.client.storageclient.composables.FilesList
import com.client.storageclient.filesystem.File
import com.client.storageclient.filesystem.Folder
import com.client.storageclient.filesystem.api.SearchByName
import com.client.storageclient.navigation.Navigation
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
                    val navController = rememberNavController()
//                    Navigation(navController = navController)
                    SearchByName()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FilesListPreview() {
    StorageClientTheme {
        val navController = rememberNavController()
        Navigation(navController = navController)
    }
}

fun fileClick() {
    print("Hello!")
}