package com.duwei.alive


import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri

class AccountProvider : ContentProvider() {

    companion object {
        const val AUTHORITY = "com.demo.keep_alive.sync_account.AccountProvider"
        private const val CONTENT_URI_BASE = "content://$AUTHORITY"
        private const val TABLE_NAME = "data"
        val CONTENT_URI: Uri = Uri.parse("$CONTENT_URI_BASE/$TABLE_NAME")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun query(
            uri: Uri,
            projection: Array<out String>?,
            selection: String?,
            selectionArgs: Array<out String>?,
            sortOrder: String?
    ): Cursor? {
        return null
    }

    override fun onCreate(): Boolean {
        return false
    }

    override fun update(
            uri: Uri,
            values: ContentValues?,
            selection: String?,
            selectionArgs: Array<out String>?
    ): Int {
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        return 0
    }

    override fun getType(uri: Uri): String? {
        return null
    }
}
