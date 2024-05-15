package com.client.storageclient.filesystem.api

import android.util.Log
import androidx.compose.runtime.MutableState
import com.client.storageclient.filesystem.File
import com.client.storageclient.filesystem.FileSystemObject
import com.client.storageclient.filesystem.Folder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun sendRequest(
    rStateFileSystem: MutableState<List<FileSystemObject>>,
    rStateProgress: MutableState<Boolean>,
    mainDirId: Int
) {
    rStateProgress.value = true

    val retrofit = Retrofit.Builder()
//        .baseUrl("http://192.168.1.13:8000")
        .baseUrl("https://freetelebot.pythonanywhere.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api = retrofit.create(FileSystemApi::class.java)
    val call: Call<FileSystemResponse?>? = api.getFileSystemData(mainDirId)

    call!!.enqueue(
        object : Callback<FileSystemResponse?> {
            override fun onResponse(call: Call<FileSystemResponse?>, response: Response<FileSystemResponse?>) {
                if (response.isSuccessful) {
                    Log.d("TAG", "" + response.body())
                    rStateProgress.value = false
//                    val finalFileSystem: MutableList<FileSystemObject> = api.getFileSystemData(response)
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

                    rStateFileSystem.value = finalFileSystem
                }
            }

            override fun onFailure(call: Call<FileSystemResponse?>, response: Throwable) {
                Log.d("TAG", response.message!!.toString())
                rStateProgress.value = false
            }
        }
    )
}