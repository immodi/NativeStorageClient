package com.client.storageclient.navigation

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
    NavHost(navController = navController, startDestination = Routes.FileSystem.route) {
        composable(Routes.FileSystem.route) {
            val file1 = File(id = 0, name = "havana.mp4", sizeInBytes = 123456789L)
            val file3 = File(id = 0, name = "WHAT??!.gif", sizeInBytes = 1024/2L)
            val file2 = Folder(id = 1, name = "test")
            val testArr = arrayOf(file1, file2, file3)
            val sortedArray = testArr.sortedArrayWith(compareBy { it is Folder })
            FilesList(fileNames = sortedArray, navController = navController)
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
            FileCard(fileName = fileName, fileSize = fileSize)
        }
    }
}