package com.example.wangduwei.demos.rongcloud.rtm.core.proxy

import android.util.Log
import com.example.wangduwei.demos.rongcloud.rtm.core.RtmCore
import io.rong.imlib.IRongCoreCallback
import io.rong.imlib.IRongCoreEnum

/**
 * @author 杜伟
 * @date 2022/2/19 2:31 PM
 *
 *
 *
 */
class OpListenerProxy : IRongCoreCallback.OperationCallback() {

    override fun onSuccess() {
        Log.d(RtmCore.TAG, "op-Success")

    }

    override fun onError(coreErrorCode: IRongCoreEnum.CoreErrorCode?) {
        Log.d(RtmCore.TAG, "op-error msg = ${coreErrorCode?.msg}")

    }
}