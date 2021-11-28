package com.example.wangduwei.demos.task.cloud.data

sealed class CloudBackupRequest{


    data class UploadBackupToDrive(val filePath: String,
                                   val mDriveServiceHelper: String,
                                   val progressListener: String?): CloudBackupRequest()


}