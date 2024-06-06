package com.client.storageclient.filesystem.api

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.core.net.toUri
import com.client.storageclient.filesystem.File
import com.client.storageclient.filesystem.FileSystemObject
import com.client.storageclient.filesystem.Folder
import com.client.storageclient.filesystem.api.downloader.AndroidDownloader
import com.client.storageclient.filesystem.api.downloader.numberAwareAlphabeticalSort
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//var apiUrl = "http://192.168.1.10:8000"
var apiUrl = "https://freetelebot.pythonanywhere.com"

fun refreshFileSystem(
    rStateFileSystem: MutableState<List<FileSystemObject>>,
    rStateProgress: MutableState<Boolean>,
    mainDirId: Int
) {
    rStateProgress.value = true

    val retrofit = Retrofit.Builder()
        .baseUrl(apiUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api = retrofit.create(FileSystemApi::class.java)
    val call: Call<FileSystemResponse?>? = api.getFileSystemData(mainDirId)

    call!!.enqueue(
        object : Callback<FileSystemResponse?> {
            override fun onResponse(call: Call<FileSystemResponse?>, response: Response<FileSystemResponse?>) {
                if (response.isSuccessful) {
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

                    rStateProgress.value = false
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

fun getFileData(
    context: Context,
    fileId: Int,
    chunkLocalUris: MutableState<MutableList<Uri>>,
    totalChunksNumber: MutableState<Int>,
) {
    val retrofit = Retrofit.Builder()
        .baseUrl(apiUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api = retrofit.create(FileSystemApi::class.java)
    val call: Call<FileData?>? = api.getFileData(fileId)

    call!!.enqueue(
        object : Callback<FileData?> {
            override fun onResponse(call: Call<FileData?>, response: Response<FileData?>) {
                if (response.isSuccessful) {
                    val fileData = response.body()!!
                    totalChunksNumber.value = fileData.chunksIds.size

//                    val testList = mutableListOf<Uri>()
                    for (chunkId in fileData.chunksIds) {
                        chunkLocalUris.value.add("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}/${chunkId.chunkName}".toUri())
                        downloadChunk(context, chunkId.chunkId, chunkId.chunkName)
//                        testList.add(chunkId.chunkName.toUri())
                    }
//                    val regex = Regex("""\d+""")
//                    val sortedUris = testList.sortedBy { filepath ->
//                        // Extract the part after "Download/"
//                        val filename = filepath.toSt111111111111111111111111111111111111111ring().substringAfter(".")
//                        // Find the numeric part in the filename
//                        regex.find(filename)?.value?.toInt() ?: 0
//                    }
//                    println("Sorted List: " + sortedUris)
                }
            }

            override fun onFailure(call: Call<FileData?>, response: Throwable) {
                Log.d("TAG", response.message!!.toString())
            }
        }
    )
}

fun downloadChunk(
    context: Context,
    chunkId: Int,
    chunkName: String,
) {
    val retrofit = Retrofit.Builder()
        .baseUrl(apiUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api = retrofit.create(FileSystemApi::class.java)
    val call: Call<ChunkUrl?>? = api.getChunkUrl(chunkId, true)

    call!!.enqueue(
        object : Callback<ChunkUrl?> {
            override fun onResponse(call: Call<ChunkUrl?>, response: Response<ChunkUrl?>) {
                if (response.isSuccessful) {
                    val downloader = AndroidDownloader(context, chunkName)
                    val chunkDownloadManagerId = downloader.downloadFile(response.body()!!.url)
                    try {
                        val uri = downloader.downloadManager.getUriForDownloadedFile(chunkDownloadManagerId)
                        Log.d("URI", uri.toString())
                    } catch (e: Exception) {Log.d("URI", e.toString())}
                }
            }

            override fun onFailure(call: Call<ChunkUrl?>, response: Throwable) {
                Log.d("TAG", response.message!!.toString())
            }
        }
    )
}