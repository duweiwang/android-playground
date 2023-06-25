package com.duwei.alive

import android.accounts.Account
import android.app.Service
import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.util.Log

class SyncService : Service() {

    private val mSyncAdapterLock = Any()

    lateinit var mSyncAdapter: SyncAdapter

    override fun onCreate() {
        super.onCreate()
        synchronized(mSyncAdapterLock) {
            mSyncAdapter = SyncAdapter(applicationContext, true)
        }
    }

    override fun onBind(p0: Intent?): IBinder {
        return mSyncAdapter.syncAdapterBinder
    }

    class SyncAdapter(context: Context, autoInitialize: Boolean) :
        AbstractThreadedSyncAdapter(context, autoInitialize) {
        companion object {
            const val TAG = "SyncAdapter"

            @JvmStatic
//            private var sLaunched = false
            private var sLaunched = true

            @JvmStatic
            fun launch() {
                Log.i(TAG, "launch")
                sLaunched = true
            }
        }

        override fun onPerformSync(
            account: Account, extras: Bundle, authority: String,
            provider: ContentProviderClient, syncResult: SyncResult
        ) {
            try {
                context.contentResolver.notifyChange(AccountProvider.CONTENT_URI, null, false)
            } catch (e: Exception) {
                Log.e(TAG, "perfoem sync exception: $e")
            }
            Log.i(TAG, "SyncAdapter onPerformSync --> sLaunched = $sLaunched")
            if (sLaunched) {
                try {
                    val awakeIntent = Intent("com.demo.keep_alive.ExecutionAction")
                        .setClass(context, ExecutionReceiver::class.java)
                    context.sendBroadcast(awakeIntent)
                } catch (e: Exception) {
                    Log.e(TAG, "sync error: $e")
                }
            }
        }
    }
}