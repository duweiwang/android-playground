package com.example.wangduwei.demos.task.cloud

import com.example.wangduwei.demos.task.BackgroundWorkManager
import com.example.wangduwei.demos.task.BackgroundWorker
import com.example.wangduwei.demos.task.WorkRunner
import com.example.wangduwei.demos.task.cloud.data.CloudBackupRequest
import com.example.wangduwei.demos.task.cloud.data.CloudBackupResult
import com.example.wangduwei.demos.task.cloud.worker.*
import java.io.File

class CloudBackupDataSource(
        private val filesDir: File,
        override val runner: WorkRunner)
    : BackgroundWorkManager<CloudBackupRequest, CloudBackupResult>() {

    override fun createWorkerFromParams(params: CloudBackupRequest,
                                        flushResults: (CloudBackupResult) -> Unit): BackgroundWorker<*> {
        return when (params) {
            is CloudBackupRequest.UploadBackupToDrive -> UploadBackupToDriveWorker(
                    filePath = params.filePath,
                    mDriveServiceHelper = params.mDriveServiceHelper,
                    progressListener = params.progressListener,
                    publishFn = { res -> flushResults(res) }
            )
        }
    }
}