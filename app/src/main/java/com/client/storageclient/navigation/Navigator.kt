package com.client.storageclient.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.client.storageclient.composables.FileCard
import com.client.storageclient.composables.FilesList
import com.client.storageclient.filesystem.File
import com.client.storageclient.filesystem.Folder

@Composable
fun Navigation(navController: NavHostController){
    NavHost(navController = navController, startDestination = Routes.FileSystem.route + "/1") {
        composable(
            route = Routes.FileSystem.route + "/{dirId}",
            arguments = listOf(
                navArgument("dirId") {type = NavType.IntType; defaultValue = 1}
            )
        ) {backStackEntry ->
            val dirId: Int = backStackEntry.arguments?.getInt("dirId") ?: 1
            Log.d("Test", dirId.toString())
            FilesList(navController = navController, dirId = dirId)
        }
        composable(
            route = Routes.FileCard.route + "/{fileName}/{fileSize}/{fileId}",
            arguments = listOf(
                navArgument("fileName") {},
                navArgument("fileSize") {},
                navArgument("fileId") {type = NavType.IntType}
            )
        ) {backStackEntry ->
            val fileId: Int = backStackEntry.arguments?.getInt("fileId") ?: 0
            val fileName: String = backStackEntry.arguments?.getString("fileName") ?: ""
            val fileSize: String = backStackEntry.arguments?.getString("fileSize") ?: ""
            FileCard(fileId = fileId, fileName = fileName, fileSize = fileSize)
        }
    }
}