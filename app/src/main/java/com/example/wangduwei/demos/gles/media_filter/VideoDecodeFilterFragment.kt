package com.example.wangduwei.demos.gles.media_filter

import android.graphics.SurfaceTexture
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.Surface
import android.view.TextureView
import android.view.TextureView.SurfaceTextureListener
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import androidx.lifecycle.lifecycleScope
import com.example.lib_gles.video_filter.core.DecoderOutputSurface
import com.example.lib_gles.video_filter.core.EncoderSurface
import com.example.lib_gles.video_filter.core.bean.Resolution
import com.example.lib_gles.video_filter.core.filter.GlFilter
import com.example.lib_gles.video_filter.core.filter.GlFilterList
import com.example.lib_gles.video_filter.core.filter.GlFilterPeriod
import com.example.lib_gles.video_filter.filter_impl.GlSoulOutFilter
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
    title = "OpenGl视频特效",
    preview = R.drawable.preview_opengl
)
class VideoDecodeFilterFragment : BaseSupportFragment(), SurfaceTextureListener,
    OnPreparedListener {

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
        btn.setOnClickListener {
            glFilterList.putGlFilter(GlFilterPeriod(0L,10 * 1000L, GlSoulOutFilter(frameLayout.context)))
        }
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