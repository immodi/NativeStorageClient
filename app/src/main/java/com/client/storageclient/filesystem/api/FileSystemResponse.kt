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

data class FileData(
    val chunksIds: List<ChunksId>,
    val fileFullPath: String,
    val fileId: Int,
    val fileMimeType: String,
    val fileName: String
)

data class ChunksId(
    val chunkId: Int,
    val chunkName: String
)

data class ChunkUrl(
    val url: String
)