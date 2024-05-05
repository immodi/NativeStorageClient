package com.client.storageclient.navigation

sealed class Routes(val route: String) {
    data object FileSystem : Routes("home")
    data object FileCard : Routes("card")
}