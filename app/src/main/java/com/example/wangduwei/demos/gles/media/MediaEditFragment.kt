package com.example.wangduwei.demos.gles.media

import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.lib_gles.video_filter.composer.Mp4Composer
import com.example.lib_gles.video_filter.core.filter.GlFilter
import com.example.lib_gles.video_filter.core.filter.GlFilterGroup
import com.example.lib_gles.video_filter.core.filter.GlFilterList
import com.example.lib_gles.video_filter.core.filter.GlFilterPeriod
import com.example.lib_gles.video_filter.core.filter.TimeScaleFilter
import com.example.lib_gles.video_filter.filter_impl.GlDynamicMosaicFilter
import com.example.lib_gles.video_filter.filter_impl.GlMosaicShiftCascadeFilter
import com.example.lib_gles.video_filter.filter_impl.GlPulseVerticalScaleFilter
import com.example.lib_gles.video_filter.filter_impl.GlPulseZoomFilter
import com.example.lib_gles.video_filter.filter_impl.GlRadialSpreadColorFilter
import com.example.lib_gles.video_filter.filter_impl.GlSoulOutFilter
import com.example.lib_gles.video_filter.filter_impl.GlWatermarkFilter
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
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
        "/Camera/PXL_20260302_053006835.mp4"
    ).absolutePath

    private val videoPath2 = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
        "ForBiggerEscapes.mp4"
    ).absolutePath


    private val duration: Long = 10000;

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
        val filterEffectOutput = view.findViewById<TextView>(R.id.video_plus_filter_effect)

        val videoEffect1 = view.findViewById<TextView>(R.id.video_effect1)
        val videoEffect3 = view.findViewById<TextView>(R.id.video_effect3)
        val videoEffect4 = view.findViewById<TextView>(R.id.video_effect4)


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
                .size(720, 1280)
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
            Mp4Composer(videoPath2, outFile.absolutePath)
                .size(720, 1280)
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

        filterEffectOutput.setOnClickListener {
            if (hasStoragePermission().not()) {
                return@setOnClickListener
            }

            if (!File(videoPath).exists()) {
                Toast.makeText(requireContext(), "视频不存在", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!File(audioPath).exists()) {
                Toast.makeText(requireContext(), "音频不存在", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val watermarkBitmap = BitmapFactory.decodeResource(resources, R.drawable.guide_enjoy_haha)
            if (watermarkBitmap == null) {
                Toast.makeText(requireContext(), "水印资源加载失败", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val filterGroup = GlFilterGroup(
                GlWatermarkFilter(watermarkBitmap,
                    24f,
                    0.8f,
                    1.0f,
                    GlWatermarkFilter.Position.RIGHT_BOTTOM),
                GlSoulOutFilter(view.context)
            )

            val defaultText = "原视频加水印加特效加音频混合输出"
            filterEffectOutput.isEnabled = false
            filterEffectOutput.text = "处理中 0%"
            val outFile = File(requireContext().filesDir, "watermark_soulout_audio_mix_${System.currentTimeMillis()}.mp4")

            val glFilterList = GlFilterList()
            glFilterList.putGlFilter(GlFilterPeriod(0L,7 * 1000L, filterGroup))
            Mp4Composer(videoPath, outFile.absolutePath)
                .size(720, 1280)
                .filterList(glFilterList)
                .audioPath(audioPath)
                .clip(0, 7_000)
                .audioMode(Mp4Composer.AudioMode.MIX)
                .listener(object : Mp4Composer.Listener {
                    override fun onProgress(progress: Double) {
                        val percent = (progress * 100).toInt().coerceIn(0, 100)
                        activity?.runOnUiThread {
                            filterEffectOutput.text = "处理中 ${percent}%"
                        }
                    }

                    override fun onCompleted() {
                        activity?.runOnUiThread {
                            filterEffectOutput.isEnabled = true
                            filterEffectOutput.text = defaultText
                            Toast.makeText(requireContext(), "输出完成: ${outFile.absolutePath}", Toast.LENGTH_SHORT).show()

                            Log.d("wdw-gl","path = ${outFile.absolutePath}")
                        }
                    }

                    override fun onCanceled() {
                        activity?.runOnUiThread {
                            filterEffectOutput.isEnabled = true
                            filterEffectOutput.text = defaultText
                            Toast.makeText(requireContext(), "已取消", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailed(exception: Exception) {
                        activity?.runOnUiThread {
                            filterEffectOutput.isEnabled = true
                            filterEffectOutput.text = defaultText
                            Toast.makeText(requireContext(), "输出失败: ${exception.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
                .start()
        }


        videoEffect1.setOnClickListener { onClickEffect1(videoEffect1) }
        videoEffect3.setOnClickListener { onClickEffect3(videoEffect3) }
        videoEffect4.setOnClickListener { onClickEffect4(videoEffect4) }

    }

    private fun onClickEffect1(textView: TextView) {
        val dynamicMosaicFilter = GlDynamicMosaicFilter()
            .setRange(2f, 40f) // 最小/最大马赛克块大小(px)
            .setDurationMs(1000f) // 一个变化周期
            .setLoop(true) // 循环
            .setPingPong(true)

        val shiftMosaicFilter = GlMosaicShiftCascadeFilter()
            .setHoldMs(1000f)
            .setStepMs(200f)
            .setShiftX(0.30f)
            .setMosaicLevels(40f, 20f, 10f, 0f)

        val filterGroup = GlFilterGroup(
            GlFilterPeriod(1000L,Long.MAX_VALUE, dynamicMosaicFilter),
            GlFilterPeriod(1000L,Long.MAX_VALUE, shiftMosaicFilter),
            GlFilterPeriod(4000L,8000L, TimeScaleFilter(0.5)),
        )

        val outFile = File(requireContext().externalCacheDir, "特效一_${System.currentTimeMillis()}.mp4")
        compose(filterGroup, textView, outFile)
    }

    private fun onClickEffect3(textView: TextView) {

        val radialColorFilter = GlRadialSpreadColorFilter()
            .setCycleDurationSec(1.6f)
            .setMaxIntensity(0.55f)
            .setSpreadSoftness(0.10f)
            .setColorList(arrayListOf<Int>(
                Color.RED,
                Color.YELLOW,
                Color.DKGRAY,
                Color.LTGRAY,
            ))


        val zoomFilter = GlPulseZoomFilter(2f)
            .setZoomInDurationMs(500f)
            .setZoomOutDurationMs(500f)

        val verticalScalefilter1 = GlPulseVerticalScaleFilter()
            .setTargetScaleY(0.7f)
            .setShrinkDurationMs(200f)
            .setExpandDurationMs(200f)
            .setIntervalMs(3000f)

        val filterGroup = GlFilterGroup(
            GlFilterPeriod(0,Long.MAX_VALUE, radialColorFilter),
            GlFilterPeriod(0,Long.MAX_VALUE, zoomFilter),
            GlFilterPeriod(0,Long.MAX_VALUE, verticalScalefilter1),
        )

        val outFile = File(requireContext().externalCacheDir, "特效三_${System.currentTimeMillis()}.mp4")
        compose(filterGroup, textView, outFile)
    }

    private fun onClickEffect4(textView: TextView) {
        val shiftMosaicFilter = GlMosaicShiftCascadeFilter()
            .setHoldMs(1000f)
            .setStepMs(200f)
            .setShiftX(0.30f)
            .setMosaicLevels(40f, 20f, 10f, 0f)

        val filterGroup = GlFilterGroup(
            GlFilterPeriod(1000L,Long.MAX_VALUE, shiftMosaicFilter),
            GlFilterPeriod(4000L,8000L, TimeScaleFilter(0.5)),
        )

        val outFile = File(requireContext().externalCacheDir, "特效四_${System.currentTimeMillis()}.mp4")
        compose(filterGroup, textView, outFile)
    }


    private fun compose(filter: GlFilter, textView: TextView, outFile: File) {
        val glFilterList = GlFilterList()
        glFilterList.putGlFilter(GlFilterPeriod(0, Long.MAX_VALUE, filter))
        val defaultText = textView.text;
        Mp4Composer(videoPath, outFile.absolutePath)
            .size(720, 1280)
            .clip(0, duration)
            .filterList(glFilterList)
            .listener(object : Mp4Composer.Listener {
                override fun onProgress(progress: Double) {
                    val percent = (progress * 100).toInt().coerceIn(0, 100)
                    activity?.runOnUiThread {
                        textView.text = "处理中 ${percent}%"
                    }
                }

                override fun onCompleted() {
                    activity?.runOnUiThread {
                        textView.isEnabled = true
                        textView.text = defaultText
                        Toast.makeText(requireContext(), "输出完成: ${outFile.absolutePath}", Toast.LENGTH_SHORT).show()

                        Log.d("wdw-gl","path = ${outFile.absolutePath}")
                    }
                }

                override fun onCanceled() {
                    activity?.runOnUiThread {
                        textView.isEnabled = true
                        textView.text = defaultText
                        Toast.makeText(requireContext(), "已取消", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailed(exception: Exception) {
                    activity?.runOnUiThread {
                        textView.isEnabled = true
                        textView.text = defaultText
                        Toast.makeText(requireContext(), "输出失败: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            })
            .start()
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
