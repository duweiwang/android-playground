package com.duwei.alive

import android.accounts.Account
import android.accounts.AccountManager
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.os.Build
import android.os.Bundle


class AuthenticatorActivity : Activity() {
    companion object {
        const val ACCOUNT_TYPE = "com.demo.type"
        fun getSyncIntervalTime() : Long {
            val oneMinute = 60L
            return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                oneMinute
            } else {
                oneMinute * 60
            }
        }
    }

    private lateinit var accountManager: AccountManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authenticator)

        accountManager = getSystemService(Context.ACCOUNT_SERVICE) as AccountManager
        val accounts = accountManager.getAccountsByType(ACCOUNT_TYPE)
        if (accounts.isNotEmpty()) {
            finish()
        } else {
            val account = Account(getString(R.string.app_name), ACCOUNT_TYPE)
            accountManager.addAccountExplicitly(account, null, null)

            val bundle = Bundle()
            ContentResolver.setIsSyncable(account, AccountProvider.AUTHORITY, 1)
            ContentResolver.setSyncAutomatically(account, AccountProvider.AUTHORITY, true)
            ContentResolver.addPeriodicSync(account, AccountProvider.AUTHORITY, bundle, getSyncIntervalTime())
            finish()
        }
    }


}