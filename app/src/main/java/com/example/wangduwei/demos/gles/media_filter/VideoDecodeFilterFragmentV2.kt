package com.example.wangduwei.demos.gles.media_filter

import android.graphics.SurfaceTexture
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener
import android.os.Bundle
import android.opengl.GLSurfaceView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.lib_gles.video_filter.core.DecoderOutputSurface
import com.example.lib_gles.video_filter.core.bean.Resolution
import com.example.lib_gles.video_filter.core.filter.GlFilter
import com.example.lib_gles.video_filter.core.filter.GlFilterList
import com.example.lib_processor.PageInfo
import com.example.wangduwei.demos.R
import com.example.wangduwei.demos.main.BaseSupportFragment
import java.io.IOException
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


@PageInfo(
    description = "OpenGl视频特效",
    navigationId = R.id.fragment_gl_video,
    title = "OpenGl视频特效V2",
    preview = R.drawable.preview_opengl
)
class VideoDecodeFilterFragmentV2 : BaseSupportFragment(), OnPreparedListener {

    companion object{
        private const val TAG = "wdw-gl"
    }

    private lateinit var mMediaPlayer: MediaPlayer
    private lateinit var mGlSurfaceView: GLSurfaceView
    private lateinit var mDecoderSurface: DecoderOutputSurface

    @Volatile private var running = false
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
        }
    }


    private fun initAndAddTexture(frameLayout: FrameLayout) {
        mGlSurfaceView = GLSurfaceView(requireContext()).apply {
            setEGLContextClientVersion(2)
            setRenderer(VideoRenderer())
            renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
            frameLayout.addView(
                this,
                FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
        }
    }

    private inner class VideoRenderer : GLSurfaceView.Renderer {
        private var isSetup = false

        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            Log.d(TAG, "onSurfaceCreated")
            mDecoderSurface = object : DecoderOutputSurface(GlFilter(), glFilterList) {
                override fun onFrameAvailable(st: SurfaceTexture) {
                    super.onFrameAvailable(st)
                    if (running) {
                        mGlSurfaceView.requestRender()
                    }
                }
            }
        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            Log.d(TAG, "onSurfaceChanged...width = $width, height = $height")
            if (!isSetup) {
                mDecoderSurface.setOutputResolution(Resolution(width, height))
                mDecoderSurface.setInputResolution(Resolution(540, 960))
                mDecoderSurface.setupAll()
                isSetup = true
                initMediaPlayer()
            }
        }

        override fun onDrawFrame(gl: GL10?) {
            if (!running) {
                return
            }
            try {
                mDecoderSurface.awaitNewImage()
            } catch (ex: java.lang.Exception) {
                ex.printStackTrace()
                return
            }
            currentPosition = mMediaPlayer.currentPosition.toLong()
            mDecoderSurface.drawImage(currentPosition * 1000 * 1000)
        }
    }


    private fun initMediaPlayer() {
        mMediaPlayer = MediaPlayer().apply {
            setAudioStreamType(AudioManager.STREAM_MUSIC)
            setScreenOnWhilePlaying(true)
            setOnPreparedListener(this@VideoDecodeFilterFragmentV2)
            setLooping(true)
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

    override fun onPrepared(mp: MediaPlayer?) {
        Log.d(TAG, "onPrepared $mp")
        mp?.start()
        running = true
    }


    override fun onResume() {
        super.onResume()
        mGlSurfaceView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mGlSurfaceView.onPause()
    }


    override fun onDestroy() {
        super.onDestroy()
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
