package com.example.wangduwei.demos.gles.media

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import android.net.Uri
import android.provider.Settings
import com.example.lib_gles.video_filter.composer.Mp4Composer
import com.example.lib_gles.video_filter.core.filter.GlFilterList
import com.example.lib_gles.video_filter.core.filter.GlFilterPeriod
import com.example.lib_gles.video_filter.filter_impl.GlSoulOutFilter
import com.example.lib_processor.PageInfo
import com.example.wangduwei.demos.R
import com.example.wangduwei.demos.main.BaseSupportFragment
import java.io.File


@PageInfo(
    description = "视频编辑输出",
    navigationId = R.id.fragment_gl_export,
    title = "OpenGl视频特效",
    preview = R.drawable.media_codec
)
class MediaEditFragment: BaseSupportFragment() {

    private val videoPath = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
        "6466608-hd_1080_1920_25fps.mp4"
    ).absolutePath

    private val audioPath = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
        "file_example_MP3_1MG.mp3"
    ).absolutePath


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gl_export, null)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val filterOutput = view.findViewById<TextView>(R.id.video_plus_filter)
        val audioOutput = view.findViewById<TextView>(R.id.video_plus_audio)


        filterOutput.setOnClickListener {
            if (hasStoragePermission().not()) {
                return@setOnClickListener
            }

            val filterList = GlFilterList()

            filterList.putGlFilter(GlFilterPeriod(0L,3 * 1000L, GlSoulOutFilter(view.context)))

            val defaultText = "视频加滤镜输出"
            filterOutput.isEnabled = false
            filterOutput.text = "处理中 0%"
            val outFile = File(requireContext().filesDir, "clip_filter_${System.currentTimeMillis()}.mp4")
            Mp4Composer(videoPath, outFile.absolutePath)
                .clip(0, 3_000)
                .filterList(filterList)
                .listener(object : Mp4Composer.Listener {
                    override fun onProgress(progress: Double) {
                        val percent = (progress * 100).toInt().coerceIn(0, 100)
                        activity?.runOnUiThread {
                            filterOutput.text = "处理中 ${percent}%"
                        }
                    }

                    override fun onCompleted() {
                        activity?.runOnUiThread {
                            filterOutput.isEnabled = true
                            filterOutput.text = defaultText
                            Toast.makeText(requireContext(), "输出完成: ${outFile.absolutePath}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCanceled() {
                        activity?.runOnUiThread {
                            filterOutput.isEnabled = true
                            filterOutput.text = defaultText
                            Toast.makeText(requireContext(), "已取消", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailed(exception: Exception) {
                        activity?.runOnUiThread {
                            filterOutput.isEnabled = true
                            filterOutput.text = defaultText
                            Toast.makeText(requireContext(), "输出失败: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
                .start()
        }



        audioOutput.setOnClickListener {
            if (hasStoragePermission().not()) {
                return@setOnClickListener
            }
            val defaultText = "视频加音频输出"
            audioOutput.isEnabled = false
            audioOutput.text = "处理中 0%"
            val outFile = File(requireContext().filesDir, "mix_audio_${System.currentTimeMillis()}.mp4")
            Mp4Composer(videoPath, outFile.absolutePath)
                .audioPath(audioPath)
                .audioMode(Mp4Composer.AudioMode.MIX)
                .listener(object : Mp4Composer.Listener {
                    override fun onProgress(progress: Double) {
                        val percent = (progress * 100).toInt().coerceIn(0, 100)
                        activity?.runOnUiThread {
                            audioOutput.text = "处理中 ${percent}%"
                        }
                    }

                    override fun onCompleted() {
                        activity?.runOnUiThread {
                            audioOutput.isEnabled = true
                            audioOutput.text = defaultText
                            Toast.makeText(requireContext(), "输出完成: ${outFile.absolutePath}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCanceled() {
                        activity?.runOnUiThread {
                            audioOutput.isEnabled = true
                            audioOutput.text = defaultText
                            Toast.makeText(requireContext(), "已取消", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailed(exception: Exception) {
                        activity?.runOnUiThread {
                            audioOutput.isEnabled = true
                            audioOutput.text = defaultText
                            Toast.makeText(requireContext(), "输出失败: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
                .start()
        }

    }


    private fun hasStoragePermission(): Boolean {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R &&
            !Environment.isExternalStorageManager()
        ) {
            Toast.makeText(
                requireContext(),
                "请开启 MANAGE_EXTERNAL_STORAGE 权限",
                Toast.LENGTH_SHORT
            ).show()
            val intent = android.content.Intent(
                Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
                Uri.parse("package:${requireContext().packageName}")
            )
            startActivity(intent)
            return false
        }


        return true
    }



}
