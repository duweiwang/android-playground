package com.example.wangduwei.demos.task.cloud.data

import java.util.*

sealed class CloudBackupResult{

    sealed class UploadBackupToDrive : CloudBackupResult() {
        data class Success(val fileLength: Long, val lastModified: Date, val hasOldFile: Boolean, val oldFileIds: List<String>): UploadBackupToDrive()
        data class Progress(val progress: Int): UploadBackupToDrive()
        data class Failure(val message: UIMessage,
                           val exception: Exception?): UploadBackupToDrive()
    }
}