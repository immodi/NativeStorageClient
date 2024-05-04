package com.client.storageclient.filesystem

interface FileSystemObject {
    val id: Int
    val name: String
    fun getIcon(): String
}