package com.client.storageclient.filesystem.api.downloader

import android.app.DownloadManager
import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableState
import androidx.core.app.NotificationCompat
import com.client.storageclient.R
import java.io.File

class DownloadBroadcastReceiver(
        private val fileName: MutableState<String>,
        private val chunkLocalUris: MutableState<MutableList<Uri>>,
        private val totalChunksNumber: MutableState<Int>,
        private val currentProgress: MutableFloatState,
        private val isLoading: MutableState<Boolean>
    ) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
//        val downloadManager: DownloadManager = context.getSystemService(DownloadManager::class.java)
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (intent.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id != -1L) {
                totalChunksNumber.value -= 1
                currentProgress.floatValue = 1 - (totalChunksNumber.value.toFloat() / chunkLocalUris.value.size)
                println(
                    "totalChunksNumber.value == " + totalChunksNumber.value
                )
            }
            if (totalChunksNumber.value <= 0) {
                mergeFile(
                    context,
                    fileName,
                    chunkLocalUris,
                    isLoading,
                    notificationManager
                )
            }
        }
    }
}


fun mergeFile(
    context: Context,
    fileName: MutableState<String>,
    chunkLocalUris: MutableState<MutableList<Uri>>,
    isLoading: MutableState<Boolean>,
    notificationManager: NotificationManager
    ) {
    try {
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(path, fileName.value)

        val regex = Regex("""\d+""")
        val sortedUris = chunkLocalUris.value.sortedBy { filepath ->
            // Extract the part after "Download/"
            val filename = filepath.toString().substringAfter(".")
            // Find the numeric part in the filename
            regex.find(filename)?.value?.toInt() ?: 0
        }

        for ((index, chunkUri) in sortedUris.withIndex()) {
            println("Custom: " + chunkUri.path)
            val bytes = File(chunkUri.path!!).readBytes()
            file.appendBytes(bytes)
            println("CurrentMergeProgress: " + (index+1.toFloat() / sortedUris.size))
        }
        for (chunkUri in chunkLocalUris.value) {
            File(chunkUri.path!!).delete()
        }

        isLoading.value = false
        println("Done Merging")

        notificationManager.notify(1, getNotification(context, fileName.value))

    } catch (e: Exception) {
        println("CustomError: " + e.toString())
    }

}

fun getNotification(context: Context, fileName: String): Notification {
    return NotificationCompat.Builder(context, "notification_channel")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("Download Complete!")
        .setContentText("'$fileName' has been successfully downloaded!")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()
}

// Our helper function which looks for numbers at the end of a string:
fun extractIntegerFromEndOfString(input: Uri): Int? {
    val stringInput = input.toString()
    val words = stringInput.split(" ")
    val lastWord = words.lastOrNull()

    return if (lastWord != null && lastWord.all { it.isDigit() }) {
        lastWord.toInt()
    } else {
        null
    }
}

// Our actual sorting function:
fun numberAwareAlphabeticalSort(item1: Uri, item2: Uri): Int {
    val item1String = item1.toString()
    val item2String = item2.toString()
    val number1 = extractIntegerFromEndOfString(item1)
    val number2 = extractIntegerFromEndOfString(item2)

    return if (number1 != null && number2 != null) {
        // Slice off the number from the end of both strings:
        //   - if the remaining portions of each string are identical, then compare the two integer values that
        //     you extracted
        //   - otherwise, simply compare the two remaining portions alphabetically
        val remaining1 = item1String.substring(0, item1String.length - number1.toString().length).trim()
        val remaining2 = item2String.substring(0, item2String.length - number2.toString().length).trim()

        if (remaining1 == remaining2) {
            number1.compareTo(number2)
        } else {
            remaining1.compareTo(remaining2)
        }
    } else {
        // Otherwise, one or both items don't have numbers at the end, so just compare them alphabetically:
        item1.compareTo(item2)
    }
}