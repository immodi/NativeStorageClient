package com.client.storageclient.filesystem.api.downloader

interface Downloader {
    fun downloadFile(
        url: String,
    ): Long
}