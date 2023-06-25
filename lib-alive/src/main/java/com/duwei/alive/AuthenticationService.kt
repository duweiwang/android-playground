package com.duwei.alive


import android.accounts.AbstractAccountAuthenticator
import android.accounts.Account
import android.accounts.AccountAuthenticatorResponse
import android.accounts.AccountManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder


class AuthenticationService : Service() {

    private lateinit var mAccountAuthenticator: AccountAuthenticator

    private fun getAccountAuthenticator(): AccountAuthenticator {
        mAccountAuthenticator = AccountAuthenticator(this)
        return mAccountAuthenticator
    }

    override fun onBind(intent: Intent?): IBinder? {
        return getAccountAuthenticator().iBinder
    }

    class AccountAuthenticator(private val context: Context) : AbstractAccountAuthenticator(context) {
        override fun getAuthToken(
                response: AccountAuthenticatorResponse?,
                account: Account?,
                authTokenType: String?,
                options: Bundle?
        ): Bundle? {
            return null
        }

        override fun getAuthTokenLabel(authTokenType: String?): String? {
            return null
        }

        override fun confirmCredentials(
                response: AccountAuthenticatorResponse?,
                account: Account?,
                options: Bundle?
        ): Bundle? {
            return null
        }

        override fun updateCredentials(
                response: AccountAuthenticatorResponse?,
                account: Account?,
                authTokenType: String?,
                options: Bundle?
        ): Bundle? {
            return null
        }

        override fun hasFeatures(
                response: AccountAuthenticatorResponse?,
                account: Account?,
                features: Array<out String>?
        ): Bundle? {
            return null
        }

        override fun editProperties(
                response: AccountAuthenticatorResponse?,
                accountType: String?
        ): Bundle? {
            return null
        }

        override fun addAccount(
                response: AccountAuthenticatorResponse?,
                accountType: String?,
                authTokenType: String?,
                requiredFeatures: Array<out String>?,
                options: Bundle?
        ): Bundle? {
            val bundle = Bundle()
            val intent = Intent(context, AuthenticatorActivity::class.java)
            intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response)
            bundle.putParcelable(AccountManager.KEY_INTENT, intent)
            return bundle
        }
    }

}
