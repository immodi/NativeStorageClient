package com.client.storageclient.filesystem.api

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

    fun getFileSystemData(response: Response<FileSystemResponse?>): MutableList<FileSystemObject> {
        val newFiles = response.body()!!.filesArray
        val acceptedFilesArray =  mutableListOf<File>()
        for (file in newFiles) {
            acceptedFilesArray.add(File(file.fileId, file.fileName, file.fileSize))
        }

        val newDirs = response.body()!!.dirsArray
        val acceptedFoldersArray =  mutableListOf<Folder>()
        for (folder in newDirs) {
            acceptedFoldersArray.add(Folder(folder.dirId, folder.dirPath))
        }

        val finalFileSystem = mutableListOf<FileSystemObject>()
        acceptedFilesArray.forEach {
            finalFileSystem.add(it)
        }
        acceptedFoldersArray.forEach {
            finalFileSystem.add(it)
        }

        return finalFileSystem
    }
}