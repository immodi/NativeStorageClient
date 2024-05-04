package com.client.storageclient.filesystem

import kotlin.math.log10
import kotlin.math.pow

class File(override var id: Int = 0, override var name: String, private var sizeInBytes: Long): FileSystemObject {
    public fun getSizeString(): String {
        if (this.sizeInBytes <= 0) return "0 B"
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (log10(this.sizeInBytes.toDouble()) / log10(1024.0)).toInt()
        return String.format("%.1f %s", this.sizeInBytes / 1024.0.pow(digitGroups.toDouble()), units[digitGroups])
    }

    override fun getIcon(): String {
        return "\uD83D\uDCC4 "
    }
}