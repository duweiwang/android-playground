package com.example.wangduwei.demos.gles.glsl

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RawRes
import com.example.lib_gles.ShaderHelper
import com.example.lib_gles.TextResourceReader
import com.example.wangduwei.demos.R
import com.example.wangduwei.demos.main.BaseSupportFragment
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class ShaderPreviewFragment : BaseSupportFragment() {

    private lateinit var glSurfaceView: GLSurfaceView

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
        glSurfaceView.setRenderer(ShaderRenderer(requireContext(), shaderResId))
        glSurfaceView.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
    }

    override fun onResume() {
        super.onResume()
        glSurfaceView.onResume()
    }

    override fun onPause() {
        glSurfaceView.onPause()
        super.onPause()
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

        private var surfaceWidth: Int = 1
        private var surfaceHeight: Int = 1
        private var startTimeMs: Long = 0L

        override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
            val vertexShader = """
                attribute vec2 aPosition;
                varying vec2 vUv;

                void main() {
                    vUv = (aPosition + 1.0) * 0.5;
                    gl_Position = vec4(aPosition, 0.0, 1.0);
                }
            """.trimIndent()

            val fragmentShader = TextResourceReader.readTextFileFromResource(context, shaderResId)

            programId = ShaderHelper.buildProgram(vertexShader, fragmentShader)
            positionHandle = GLES20.glGetAttribLocation(programId, "aPosition")
            resolutionHandle = firstValidUniformLocation(programId, "uResolution", "iResolution")
            timeHandle = firstValidUniformLocation(programId, "uTime", "iTime")
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
    }

    companion object {
        const val ARG_SHADER_RES_ID = "arg_shader_res_id"
        const val ARG_EFFECT_NAME = "arg_effect_name"
    }
}
