package org.jetbrains.research.tasktracker.tracking

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import java.io.File

fun String.getVirtualFile(): VirtualFile {
    val file = File(this)
    ApplicationManager.getApplication().runWriteAction {
        if (file.isDirectory) {
            file.mkdirs()
        } else {
            file.createNewFile()
        }
    }
    return LocalFileSystem.getInstance().refreshAndFindFileByIoFile(file) ?: error("File $this must exist")
}
