package com.client.storageclient.filesystem.api

data class FileSystemResponse(
    val dirsArray: List<DirsArray>,
    val filesArray: List<FilesArray>
)

data class DirsArray(
    val dirId: Int,
    val dirPath: String
)

data class FilesArray(
    val fileId: Int,
    val fileName: String,
    val fileSize: Long
)