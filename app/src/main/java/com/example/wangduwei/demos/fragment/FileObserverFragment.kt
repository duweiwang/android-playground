package com.example.wangduwei.demos.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.ScrollView
import androidx.appcompat.widget.Toolbar
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.lib_processor.PageInfo
import com.example.wangduwei.demos.R
import com.example.wangduwei.demos.main.BaseSupportFragment
import com.example.wangduwei.demos.service.FileObserverService
import kotlinx.android.synthetic.main.fragment_fileobserver.*
import java.io.File
import java.io.FileNotFoundException

/**
 * @author 杜伟
 * @date 2022/4/25 6:53 PM
 *
 * todo 权限
 *
 */
@PageInfo(description = "FileObserver", navigationId = R.id.fragment_fileobserver)
class FileObserverFragment : BaseSupportFragment(), Toolbar.OnMenuItemClickListener {

    private val handle = Handler()
    private var mPendingAction = -1

    /**
     * 通过广播接收Service消息
     */
    private val mReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, p1: Intent) {
            if (p1.action == FileObserverService.ACTION_EVENT) {
                val string = p1.getStringExtra(FileObserverService.EXTRA_EVENT_DUMP)!!
                handle.post {
                    addToEventDump(string)
                }
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        val id = item.itemId
        mPendingAction = id
        handleAction()
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fileobserver, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragment_toolbar.setOnMenuItemClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            mReceiver, IntentFilter(FileObserverService.ACTION_EVENT)
        )
        requestEventDump()
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(mReceiver)
    }

    fun addToEventDump(text: String) {
        event_dump.text = text
        scroll_event_dump.fullScroll(ScrollView.FOCUS_DOWN)
    }

    private fun appendToCommandDump(text: String) {
        output_text.append(text)
        scroll_command_dump.fullScroll(ScrollView.FOCUS_DOWN)
    }

    private fun requestEventDump() {
        requireContext().startService(
            Intent(
                requireContext(), FileObserverService::class.java
            ).apply {
                action = FileObserverService.ACTION_COMMAND_DUMP
            }
        )
    }

    private fun handleAction() {
        when (mPendingAction) {
            R.id.action_clear_output -> clearOutput()
            R.id.action_create_file -> createFile()
            R.id.action_write_file -> writeFile()
            R.id.action_read_file -> readFile()
            R.id.action_delete_file -> deleteFile()
            R.id.action_make_directory -> makeDirectory()
            R.id.action_start_observing -> startObserving()
            R.id.action_stop_observing -> stopObserving()
            else -> assert(false)
        }
    }

    private fun stopObserving() {
        requireContext().startService(Intent(
            requireContext(), FileObserverService::class.java
        ).apply {
            action = FileObserverService.ACTION_COMMAND_STOP
        })
    }

    private fun startObserving() {
        requireContext().startService(Intent(
            requireContext(), FileObserverService::class.java
        ).apply {
            action = FileObserverService.ACTION_COMMAND_START
            putExtra(FileObserverService.EXTRA_FILE_PATH, file_path.text.toString())
        })
    }

    private fun makeDirectory() {
        val file = File(file_path.text.toString())
        if (file.mkdirs()) {
            appendToCommandDump("MKDIR: OK: Made: " + file.absolutePath + "\n")
        } else {
            appendToCommandDump("MKDIR: ERROR: Cannot make: " + file.absolutePath + "\n")
        }
    }

    private fun deleteFile() {
        val file = File(file_path.text.toString());
        if (file.delete()) {
            appendToCommandDump("DELETE: OK: Deleted: " + file.absolutePath + "\n");
        } else {
            appendToCommandDump("DELETE: ERROR: Cannot delete: " + file.absolutePath + "\n");
        }
    }

    private fun readFile() {
        try {
            val text = File(file_path.text.toString()).readText()
            appendToCommandDump(text)
        } catch (e: FileNotFoundException) {
            appendToCommandDump("FileNotFoundException")
        }

    }

    private fun writeFile() {
        File(file_path.text.toString()).writeText("hello" + System.currentTimeMillis())

    }

    private fun createFile() {
        val result = File(file_path.text.toString()).createNewFile()
        appendToCommandDump("create file result = $result\n")
    }

    private fun clearOutput() {
        output_text.text = ""
        requireContext().startService(
            Intent(
                requireContext(), FileObserverService::class.java
            )
        )
    }

}