package com.example.wangduwei.demos.task.cloud.data

import java.util.*

data class CloudBackupData(val hasCloudBackup: Boolean,
                           val autoBackupFrequency: Int,
                           val useWifiOnly: Boolean,
                           val lastModified: Date?,
                           val fileSize: Long)