package com.client.storageclient.filesystem.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface FileSystemApi {
    @Headers(
        "Accept: application/json"
    )

    @GET("/")
    fun getFileSystemData(
//        @Query("name") name: String
    ): Call<FileSystemResponse?>?
}