package com.client.storageclient.filesystem.api

import android.util.Log
import com.client.storageclient.fileClick
import com.client.storageclient.filesystem.File
import com.client.storageclient.filesystem.FileSystemObject
import com.client.storageclient.filesystem.Folder
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface FileSystemApi {
    @Headers(
        "Accept: application/json"
    )

    @GET("/")
    fun getFileSystemData(
        @Query("dirId") dirId: Int
    ): Call<FileSystemResponse?>?

    @GET("/file")
    fun getFileData(
        @Query("fileId") fileId: Int
    ): Call<FileData?>?

    @GET("/download")
    fun getChunkUrl(
        @Query("chunkId") chunkId: Int,
        @Query("isLink") isLink: Boolean
    ): Call<ChunkUrl?>?
}