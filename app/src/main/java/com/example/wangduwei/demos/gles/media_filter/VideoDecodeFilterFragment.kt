package com.example.wangduwei.demos.gles.media_filter

import android.graphics.SurfaceTexture
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Surface
import android.view.TextureView
import android.view.TextureView.SurfaceTextureListener
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.example.lib_gles.video_filter.core.DecoderOutputSurface
import com.example.lib_gles.video_filter.core.EncoderSurface
import com.example.lib_gles.video_filter.core.bean.Resolution
import com.example.lib_gles.video_filter.core.filter.GlFilter
import com.example.lib_gles.video_filter.core.filter.GlFilterList
import com.example.lib_gles.video_filter.core.filter.GlFilterPeriod
import com.example.lib_gles.video_filter.filter_impl.filter.GlBilateralFilter
import com.example.lib_gles.video_filter.filter_impl.filter.GlBoxBlurFilter
import com.example.lib_gles.video_filter.filter_impl.filter.GlBrightnessFilter
import com.example.lib_gles.video_filter.filter_impl.filter.GlBulgeDistortionFilter
import com.example.lib_gles.video_filter.filter_impl.filter.GlCGAColorspaceFilter
import com.example.lib_gles.video_filter.filter_impl.filter.GlContrastFilter
import com.example.lib_gles.video_filter.filter_impl.filter.GlCrosshatchFilter
import com.example.lib_gles.video_filter.filter_impl.filter.GlExposureFilter
import com.example.lib_gles.video_filter.filter_impl.filter.GlGammaFilter
import com.example.lib_gles.video_filter.filter_impl.filter.GlGaussianBlurFilter
import com.example.lib_gles.video_filter.filter_impl.filter.GlGrayScaleFilter
import com.example.lib_gles.video_filter.filter_impl.filter.GlHalftoneFilter
import com.example.lib_gles.video_filter.filter_impl.filter.GlHazeFilter
import com.example.lib_gles.video_filter.filter_impl.filter.GlHighlightShadowFilter
import com.example.lib_gles.video_filter.filter_impl.filter.GlHueFilter
import com.example.lib_gles.video_filter.filter_impl.filter.GlInvertFilter
import com.example.lib_gles.video_filter.filter_impl.filter.GlLuminanceFilter
import com.example.lib_gles.video_filter.filter_impl.filter.GlLuminanceThresholdFilter
import com.example.lib_gles.video_filter.filter_impl.filter.GlMonochromeFilter
import com.example.lib_gles.video_filter.filter_impl.filter.GlOpacityFilter
import com.example.lib_gles.video_filter.filter_impl.filter.GlPixelationFilter
import com.example.lib_gles.video_filter.filter_impl.filter.GlPosterizeFilter
import com.example.lib_gles.video_filter.filter_impl.filter.GlRGBFilter
import com.example.lib_gles.video_filter.filter_impl.filter.GlSaturationFilter
import com.example.lib_gles.video_filter.filter_impl.filter.GlSepiaFilter
import com.example.lib_gles.video_filter.filter_impl.filter.GlSharpenFilter
import com.example.lib_gles.video_filter.filter_impl.filter.GlSolarizeFilter
import com.example.lib_gles.video_filter.filter_impl.filter.GlSphereRefractionFilter
import com.example.lib_gles.video_filter.filter_impl.filter.GlSwirlFilter
import com.example.lib_gles.video_filter.filter_impl.filter.GlToneFilter
import com.example.lib_gles.video_filter.filter_impl.filter.GlVibranceFilter
import com.example.lib_gles.video_filter.filter_impl.filter.GlVignetteFilter
import com.example.lib_gles.video_filter.filter_impl.filter.GlWeakPixelInclusionFilter
import com.example.lib_gles.video_filter.filter_impl.filter.GlWhiteBalanceFilter
import com.example.lib_gles.video_filter.filter_impl.filter.GlZoomBlurFilter
import com.example.lib_processor.PageInfo
import com.example.wangduwei.demos.R
import com.example.wangduwei.demos.main.BaseSupportFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException


@PageInfo(
    description = "OpenGl视频特效",
    navigationId = R.id.fragment_gl_video,
    title = "OpenGl视频特效TextureView",
    preview = R.drawable.preview_opengl
)
class VideoDecodeFilterFragment : BaseSupportFragment(), SurfaceTextureListener,
    OnPreparedListener {

    private data class FilterOption(
        val name: String,
        val create: () -> GlFilter
    )

    companion object{
        private const val TAG = "wdw-gl"
    }

    private lateinit var mMediaPlayer: MediaPlayer
    private lateinit var mTextureView: TextureView


    private lateinit var mEncoderSurface: EncoderSurface
    private lateinit var mDecoderSurface: DecoderOutputSurface

    @Volatile private var running = false
    @Volatile private var notDestroyed: Boolean = true

    private val handler = Handler(Looper.getMainLooper())
    private var currentPosition = 0L


    private val glFilterList = GlFilterList()
    private lateinit var currentFilterTv: TextView
    private var currentEffectIndex = -1
    private val switchableEffects = listOf(
        FilterOption("Brightness") { GlBrightnessFilter() },
        FilterOption("Contrast") { GlContrastFilter() },
        FilterOption("Exposure") { GlExposureFilter() },
        FilterOption("Gamma") { GlGammaFilter() },
        FilterOption("GrayScale") { GlGrayScaleFilter() },
        FilterOption("CGAColorspace") { GlCGAColorspaceFilter() },
        FilterOption("HighlightShadow") { GlHighlightShadowFilter() },
        FilterOption("Haze") { GlHazeFilter() },
        FilterOption("Hue") { GlHueFilter() },
        FilterOption("Invert") { GlInvertFilter() },
        FilterOption("Luminance") { GlLuminanceFilter() },
        FilterOption("LuminanceThreshold") { GlLuminanceThresholdFilter() },
        FilterOption("Opacity") { GlOpacityFilter() },
        FilterOption("Sepia") { GlSepiaFilter() },
        FilterOption("Solarize") { GlSolarizeFilter() },
        FilterOption("Vignette") { GlVignetteFilter() },
        FilterOption("Vibrance") { GlVibranceFilter() },
        FilterOption("Saturation") { GlSaturationFilter() },
        FilterOption("Posterize") { GlPosterizeFilter() },
        FilterOption("Monochrome") { GlMonochromeFilter() },
        FilterOption("RGB") { GlRGBFilter() },
        FilterOption("Pixelation") { GlPixelationFilter() },
        FilterOption("Sharpen") { GlSharpenFilter() },
        FilterOption("BulgeDistortion") { GlBulgeDistortionFilter() },
        FilterOption("Crosshatch") { GlCrosshatchFilter() },
        FilterOption("Halftone") { GlHalftoneFilter() },
        FilterOption("GaussianBlur") { GlGaussianBlurFilter() },
        FilterOption("BoxBlur") { GlBoxBlurFilter() },
        FilterOption("Bilateral") { GlBilateralFilter() },
        FilterOption("SphereRefraction") { GlSphereRefractionFilter() },
        FilterOption("Swirl") { GlSwirlFilter() },
        FilterOption("Tone") { GlToneFilter() },
        FilterOption("WeakPixelInclusion") { GlWeakPixelInclusionFilter() },
        FilterOption("WhiteBalance") { GlWhiteBalanceFilter() },
        FilterOption("ZoomBlur") { GlZoomBlurFilter() }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_opengl_video, null)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (view is FrameLayout) {
            initAndAddTexture(view)
            addBtn(view)
        }
    }

    private fun addBtn(frameLayout: FrameLayout) {
        val btn = Button(frameLayout.context)
        btn.text = "add filter"
        frameLayout.addView(
            btn,
            FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        )

        currentFilterTv = TextView(frameLayout.context).apply {
            text = "当前特效：无"
            setTextColor(0xFFFFFFFF.toInt())
            textSize = 16f
        }
        frameLayout.addView(
            currentFilterTv,
            FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.END or Gravity.TOP
                topMargin = 16.dp()
                marginEnd = 16.dp()
            }
        )

        btn.setOnClickListener {
            switchEffect()
        }
    }

    private fun switchEffect() {
        currentEffectIndex = (currentEffectIndex + 1) % switchableEffects.size
        val current = switchableEffects[currentEffectIndex]
        glFilterList.clearAddedFilters()
        glFilterList.putGlFilter(GlFilterPeriod(0L, Long.MAX_VALUE, current.create()))
        currentFilterTv.text = "当前特效：${current.name}"
        Log.d(TAG, "switchEffect -> ${current.name}")
    }

    private fun Int.dp(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }


    private fun initAndAddTexture(frameLayout: FrameLayout) {
        mTextureView = TextureView(requireContext()).apply {
            surfaceTextureListener = this@VideoDecodeFilterFragment
            frameLayout.addView(
                this,
                FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
        }
    }

    override fun onSurfaceTextureAvailable(
        surface: SurfaceTexture,
        width: Int,
        height: Int
    ) {
        Log.d(TAG, "onSurfaceTextureAvailable...width = $width, height = $height")

        lifecycleScope.launch(Dispatchers.IO) {

            mEncoderSurface = EncoderSurface(Surface(surface)).apply {
                makeCurrent()
            }

            mDecoderSurface = DecoderOutputSurface(GlFilter(), glFilterList).apply {
                setOutputResolution(Resolution(width, height))
                setInputResolution(Resolution(540, 960))
                setupAll()
            }

            withContext(Dispatchers.Main) {
                initMediaPlayer()
            }

            poll()
        }
    }

    private fun poll() {
        while (notDestroyed) {
            if (running) {
                try {
                    mDecoderSurface.awaitNewImage()
                } catch (ex: java.lang.Exception) {
                    ex.printStackTrace()
                    return
                }
                mDecoderSurface.drawImage(currentPosition * 1000 * 1000)
                mEncoderSurface.setPresentationTime(System.currentTimeMillis())
                mEncoderSurface.swapBuffers()
            } else {
                try {
                    Thread.sleep(20)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
    }


    private fun initMediaPlayer() {
        mMediaPlayer = MediaPlayer().apply {
            setAudioStreamType(AudioManager.STREAM_MUSIC)
            setScreenOnWhilePlaying(true)
            setOnPreparedListener(this@VideoDecodeFilterFragment)
            setLooping(true)
        }

        while (mDecoderSurface.surface == null) {
            try {
                Thread.sleep(30)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

        try {
            val afd = requireContext().assets.openFd("6466608-hd_1080_1920_25fps.mp4")
            mMediaPlayer.setDataSource(afd)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        mMediaPlayer.apply {
            setSurface(mDecoderSurface.getSurface())
            prepareAsync()
        }

        Log.d(TAG, "init media player finished")
    }

    override fun onSurfaceTextureSizeChanged(
        surface: SurfaceTexture,
        width: Int,
        height: Int
    ) {
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        return false
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
    }

    override fun onPrepared(mp: MediaPlayer?) {
        Log.d(TAG, "onPrepared $mp")
        mp?.start()
        running = true
    }


    override fun onResume() {
        super.onResume()
//        resumePlay()
    }

    override fun onPause() {
        super.onPause()
//        pausePlay()
    }


    override fun onDestroy() {
        super.onDestroy()
        notDestroyed = false
        running = false
        mMediaPlayer.release()
        mDecoderSurface.stopRun()
    }


    fun resumePlay() {
        running = true
        if (!mMediaPlayer.isPlaying) {
            mMediaPlayer.start()
        }
    }

    fun pausePlay() {
        running = false
        try {
            mMediaPlayer.pause()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }


}
