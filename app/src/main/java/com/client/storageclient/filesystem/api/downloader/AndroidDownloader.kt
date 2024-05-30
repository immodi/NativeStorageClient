package com.client.storageclient.filesystem.api.downloader

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.net.toUri

class AndroidDownloader(
    context: Context,
    private val chunkName: String
): Downloader {
    val downloadManager: DownloadManager = context.getSystemService(DownloadManager::class.java)

    override fun downloadFile(url: String): Long {
        val request = DownloadManager.Request(url.toUri())
            .setMimeType("application/octet-stream")
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            .setTitle("Downloading Chunk....")
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, chunkName)
        return downloadManager.enqueue(request)
    }

}