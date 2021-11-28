package com.example.wangduwei.demos.task.cloud.worker

import com.example.wangduwei.demos.task.BackgroundWorker
import com.example.wangduwei.demos.task.ProgressReporter
import com.example.wangduwei.demos.task.cloud.data.CloudBackupResult
import com.example.wangduwei.demos.task.cloud.data.UIMessage
import java.util.*


class UploadBackupToDriveWorker(private val filePath: String,
                                private val mDriveServiceHelper: String,
                                private val progressListener: String?,
                                override val publishFn: (CloudBackupResult) -> Unit)
    : BackgroundWorker<CloudBackupResult.UploadBackupToDrive> {

    override val canBeParallelized = true


    override fun catchException(ex: Exception): CloudBackupResult.UploadBackupToDrive {
        return CloudBackupResult.UploadBackupToDrive.Failure(UIMessage(-1), ex)

    }

    override fun work(reporter: ProgressReporter<CloudBackupResult.UploadBackupToDrive>): CloudBackupResult.UploadBackupToDrive? {
        val result = Result.of {

            //do....
        }

        return when (result) {
            is Result.Success -> {
              //
                CloudBackupResult.UploadBackupToDrive.Success(
                        fileLength = 100,
                        lastModified = Date(),
                        hasOldFile = true,
                        oldFileIds =  listOf()
                )
            }
            is Result.Failure -> catchException(result.error)
        }
    }

    override fun cancel() {
        TODO("not implemented") //To change body of created functions use CRFile | Settings | CRFile Templates.
    }
}