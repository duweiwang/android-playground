package com.duwei.alive

import android.accounts.Account
import android.accounts.AccountManager
import android.content.ContentResolver
import android.content.Context
import android.os.Bundle

class SyncAccountHelper {

    companion object {
        fun addAndOpenSyncAccount(context: Context) {
            addSyncAccounts(context)
            openSyncAccount(context)
        }

        private fun addSyncAccounts(context: Context) {
            val accountManager = context.getSystemService(Context.ACCOUNT_SERVICE) as AccountManager
            val accounts =
                    accountManager.getAccountsByType(AuthenticatorActivity.ACCOUNT_TYPE)
            if (accounts.isEmpty()) {
                val account = Account(context.getString(R.string.app_name), 
                        AuthenticatorActivity.ACCOUNT_TYPE)
                accountManager.addAccountExplicitly(account, null, null)

                val bundle = Bundle()
                ContentResolver.setIsSyncable(account, AccountProvider.AUTHORITY, 1)
                ContentResolver.setSyncAutomatically(account, AccountProvider.AUTHORITY, true)
                ContentResolver.addPeriodicSync(account,
                        AccountProvider.AUTHORITY, bundle, AuthenticatorActivity.getSyncIntervalTime())
            }
        }

        private fun openSyncAccount(context: Context) {
            val account = Account(context.getString(R.string.app_name), AuthenticatorActivity.ACCOUNT_TYPE)
            if (ContentResolver.getMasterSyncAutomatically()) {
                if (!ContentResolver.getSyncAutomatically(account, AccountProvider.AUTHORITY)) {
                    ContentResolver.setSyncAutomatically(account, AccountProvider.AUTHORITY, true)
                }
            } else {
                ContentResolver.setMasterSyncAutomatically(true)
                if (!ContentResolver.getSyncAutomatically(account, AccountProvider.AUTHORITY)) {
                    ContentResolver.setSyncAutomatically(account, AccountProvider.AUTHORITY, true)
                }
            }
        }
    }
}