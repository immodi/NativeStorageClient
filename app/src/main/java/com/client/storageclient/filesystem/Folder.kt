package com.client.storageclient.filesystem

class Folder(override var id: Int = 0, override var name: String): FileSystemObject {
    override fun getIcon(): String {
        return "\uD83D\uDCC1 "
    }
}