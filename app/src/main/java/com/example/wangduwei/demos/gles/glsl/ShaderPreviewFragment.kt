package com.example.wangduwei.demos.gles.glsl

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import android.os.Bundle
import android.os.SystemClock
import android.view.MotionEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RawRes
import com.example.lib_gles.ShaderHelper
import com.example.lib_gles.TextResourceReader
import com.example.wangduwei.demos.R
import com.example.wangduwei.demos.main.BaseSupportFragment
import android.graphics.BitmapFactory
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class ShaderPreviewFragment : BaseSupportFragment() {

    private lateinit var glSurfaceView: GLSurfaceView
    private lateinit var renderer: ShaderRenderer

    @RawRes
    private var shaderResId: Int = 0
    private var effectName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shaderResId = requireArguments().getInt(ARG_SHADER_RES_ID)
        require(shaderResId != 0) { "Missing fragment shader resource id." }
        effectName = requireArguments().getString(ARG_EFFECT_NAME).orEmpty()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_glsl_shader_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (effectName.isNotEmpty()) {
            activity?.title = effectName
        }
        glSurfaceView = view.findViewById(R.id.glsl_preview_surface)
        glSurfaceView.setEGLContextClientVersion(2)
        renderer = ShaderRenderer(requireContext(), shaderResId)
        glSurfaceView.setRenderer(renderer)
        glSurfaceView.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
        glSurfaceView.setOnTouchListener { _, event ->
            handleTouch(event)
            true
        }
    }

    override fun onResume() {
        super.onResume()
        glSurfaceView.onResume()
    }

    override fun onPause() {
        glSurfaceView.onPause()
        super.onPause()
    }

    private fun handleTouch(event: MotionEvent) {
        val width = glSurfaceView.width
        val height = glSurfaceView.height
        if (width <= 0 || height <= 0) {
            return
        }
        val normalizedY = height - event.y
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                renderer.updateMouse(event.x, normalizedY, 1f)
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                renderer.updateMouse(event.x, normalizedY, 0f)
            }
        }
    }

    private class ShaderRenderer(
        private val context: Context,
        @RawRes private val shaderResId: Int
    ) : GLSurfaceView.Renderer {

        private val vertexBuffer: FloatBuffer = ByteBuffer
            .allocateDirect(VERTEX_COORDS.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .apply {
                put(VERTEX_COORDS)
                position(0)
            }

        private var programId: Int = 0
        private var positionHandle: Int = -1
        private var resolutionHandle: Int = -1
        private var timeHandle: Int = -1
        private var mouse4Handle: Int = -1
        private var mouse2Handle: Int = -1
        private var color3Handle: Int = -1
        private var colorA3Handle: Int = -1
        private var colorB3Handle: Int = -1
        private var channel0Handle: Int = -1
        private var channel0TextureId: Int = 0

        private var surfaceWidth: Int = 1
        private var surfaceHeight: Int = 1
        private var startTimeMs: Long = 0L
        @Volatile
        private var mouseX: Float = 0f
        @Volatile
        private var mouseY: Float = 0f
        @Volatile
        private var mouseDown: Float = 0f

        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            val vertexShader = """
                attribute vec2 aPosition;
                varying vec2 vUv;
                varying vec3 v_position;

                void main() {
                    vUv = (aPosition + 1.0) * 0.5;
                    v_position = vec3(aPosition, 0.0);
                    gl_Position = vec4(aPosition, 0.0, 1.0);
                }
            """.trimIndent()

            val fragmentShader = TextResourceReader.readTextFileFromResource(context, shaderResId)

            programId = ShaderHelper.buildProgram(vertexShader, fragmentShader)
            positionHandle = GLES20.glGetAttribLocation(programId, "aPosition")
            resolutionHandle = firstValidUniformLocation(
                programId,
                "uResolution",
                "iResolution",
                "u_resolution"
            )
            timeHandle = firstValidUniformLocation(programId, "uTime", "iTime", "u_time")
            mouse4Handle = firstValidUniformLocation(programId, "uMouse", "iMouse")
            mouse2Handle = firstValidUniformLocation(programId, "u_mouse")
            color3Handle = firstValidUniformLocation(programId, "uColor", "u_color")
            colorA3Handle = firstValidUniformLocation(programId, "uColorA", "u_color_a")
            colorB3Handle = firstValidUniformLocation(programId, "uColorB", "u_color_b")
            channel0Handle = firstValidUniformLocation(programId, "uChannel0", "iChannel0")
            channel0TextureId = createChannel0Texture()
            startTimeMs = SystemClock.elapsedRealtime()

            GLES20.glClearColor(0f, 0f, 0f, 1f)
        }

        override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
            surfaceWidth = if (width == 0) 1 else width
            surfaceHeight = if (height == 0) 1 else height
            GLES20.glViewport(0, 0, surfaceWidth, surfaceHeight)
        }

        override fun onDrawFrame(gl: GL10?) {
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
            GLES20.glUseProgram(programId)

            val elapsedSeconds = (SystemClock.elapsedRealtime() - startTimeMs) / 1000f
            if (resolutionHandle >= 0) {
                GLES20.glUniform2f(
                    resolutionHandle,
                    surfaceWidth.toFloat(),
                    surfaceHeight.toFloat()
                )
            }
            if (timeHandle >= 0) {
                GLES20.glUniform1f(timeHandle, elapsedSeconds)
            }
            if (mouse4Handle >= 0) {
                GLES20.glUniform4f(mouse4Handle, mouseX, mouseY, mouseDown, 0f)
            }
            if (mouse2Handle >= 0) {
                GLES20.glUniform2f(mouse2Handle, mouseX, mouseY)
            }
            if (color3Handle >= 0) {
                GLES20.glUniform3f(color3Handle, 1f, 0f, 0f)
            }
            if (colorA3Handle >= 0) {
                GLES20.glUniform3f(colorA3Handle, 1f, 0f, 0f)
            }
            if (colorB3Handle >= 0) {
                GLES20.glUniform3f(colorB3Handle, 1f, 1f, 0f)
            }
            if (channel0Handle >= 0 && channel0TextureId != 0) {
                GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, channel0TextureId)
                GLES20.glUniform1i(channel0Handle, 0)
            }

            GLES20.glEnableVertexAttribArray(positionHandle)
            vertexBuffer.position(0)
            GLES20.glVertexAttribPointer(
                positionHandle,
                2,
                GLES20.GL_FLOAT,
                false,
                0,
                vertexBuffer
            )
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)
            GLES20.glDisableVertexAttribArray(positionHandle)
        }

        companion object {
            private val VERTEX_COORDS = floatArrayOf(
                -1f, -1f,
                1f, -1f,
                -1f, 1f,
                1f, 1f
            )
        }

        private fun firstValidUniformLocation(programId: Int, vararg names: String): Int {
            for (name in names) {
                val location = GLES20.glGetUniformLocation(programId, name)
                if (location >= 0) {
                    return location
                }
            }
            return -1
        }

        fun updateMouse(x: Float, y: Float, down: Float) {
            mouseX = x
            mouseY = y
            mouseDown = down
        }

        private fun createChannel0Texture(): Int {
            val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.img) ?: return 0
            val textures = IntArray(1)
            GLES20.glGenTextures(1, textures, 0)
            val textureId = textures[0]
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
            GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MIN_FILTER,
                GLES20.GL_LINEAR
            )
            GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_MAG_FILTER,
                GLES20.GL_LINEAR
            )
            GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_WRAP_S,
                GLES20.GL_CLAMP_TO_EDGE
            )
            GLES20.glTexParameteri(
                GLES20.GL_TEXTURE_2D,
                GLES20.GL_TEXTURE_WRAP_T,
                GLES20.GL_CLAMP_TO_EDGE
            )
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
            bitmap.recycle()
            return textureId
        }
    }

    companion object {
        const val ARG_SHADER_RES_ID = "arg_shader_res_id"
        const val ARG_EFFECT_NAME = "arg_effect_name"
    }
}
