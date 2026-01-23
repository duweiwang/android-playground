package com.example.lib_gles.fbo.fbo_simple

import android.opengl.GLES20.*
import android.opengl.GLSurfaceView
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * 最简单的FBO演示
 */
class FboRenderer : GLSurfaceView.Renderer {

    // 屏幕尺寸
    private var viewW = 0
    private var viewH = 0

    // FBO 尺寸（这里就用和屏幕一致，最简单）
    private var fboW = 0
    private var fboH = 0

    // FBO 相关
    private val fboId = IntArray(1)
    private val fboTexId = IntArray(1)

    // Program：离屏画三角形（纯色）
    private var triProgram = 0
    private var triPosLoc = 0

    // Program：上屏画全屏 quad（采样 fboTex）
    private var quadProgram = 0
    private var quadPosLoc = 0
    private var quadUvLoc = 0
    private var quadSamplerLoc = 0

    // 顶点数据
    private val triVertices: FloatBuffer = fbOf(
        floatArrayOf(
            0f,  0.6f,
            -0.6f,-0.6f,
            0.6f,-0.6f
        )
    )

    // 全屏四边形（NDC）+ UV
    private val quadPos: FloatBuffer = fbOf(
        floatArrayOf(
            -1f, -1f,
            1f, -1f,
            -1f,  1f,
            1f,  1f
        )
    )
    private val quadUv: FloatBuffer = fbOf(
        floatArrayOf(
            0f, 0f,
            1f, 0f,
            0f, 1f,
            1f, 1f
        )
    )

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // 背景色
        glClearColor(0f, 0f, 0f, 1f)

        // 1) 编译 shader/program
        triProgram = buildProgram(
            // aPos -> gl_Position
            """
            attribute vec2 aPos;
            void main() {
                gl_Position = vec4(aPos, 0.0, 1.0);
            }
            """.trimIndent(),
            // 固定颜色
            """
            precision mediump float;
            void main() {
                gl_FragColor = vec4(1.0, 0.2, 0.2, 1.0);
            }
            """.trimIndent()
        )
        triPosLoc = glGetAttribLocation(triProgram, "aPos")

        quadProgram = buildProgram(
            """
            attribute vec2 aPos;
            attribute vec2 aUv;
            varying vec2 vUv;
            void main() {
                vUv = aUv;
                gl_Position = vec4(aPos, 0.0, 1.0);
            }
            """.trimIndent(),
            """
            precision mediump float;
            varying vec2 vUv;
            uniform sampler2D uTex;
            void main() {
                gl_FragColor = texture2D(uTex, vUv);
            }
            """.trimIndent()
        )
        quadPosLoc = glGetAttribLocation(quadProgram, "aPos")
        quadUvLoc = glGetAttribLocation(quadProgram, "aUv")
        quadSamplerLoc = glGetUniformLocation(quadProgram, "uTex")
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        viewW = width
        viewH = height
        fboW = width
        fboH = height

        // 2) 创建/重建 FBO（尺寸依赖这里）
        destroyFbo()
        createFbo(fboW, fboH)
    }

    override fun onDrawFrame(gl: GL10?) {
        // ---------- Pass 1: 渲染到 FBO ----------
        glBindFramebuffer(GL_FRAMEBUFFER, fboId[0])
        glViewport(0, 0, fboW, fboH)
        glClear(GL_COLOR_BUFFER_BIT)

        glUseProgram(triProgram)
        glEnableVertexAttribArray(triPosLoc)
        glVertexAttribPointer(triPosLoc, 2, GL_FLOAT, false, 2 * 4, triVertices)
        glDrawArrays(GL_TRIANGLES, 0, 3)
        glDisableVertexAttribArray(triPosLoc)

        glBindFramebuffer(GL_FRAMEBUFFER, 0) // 回到默认帧缓冲

        // ---------- Pass 2: 把 FBO 纹理画到屏幕 ----------
        glViewport(0, 0, viewW, viewH)
        glClear(GL_COLOR_BUFFER_BIT)

        glUseProgram(quadProgram)

        glActiveTexture(GL_TEXTURE0)//激活0号纹理单元
        glBindTexture(GL_TEXTURE_2D, fboTexId[0])//把fboTexId[0]绑定到0号纹理单元
        glUniform1i(quadSamplerLoc, 0)

        glEnableVertexAttribArray(quadPosLoc)
        glVertexAttribPointer(quadPosLoc, 2, GL_FLOAT, false, 2 * 4, quadPos)

        glEnableVertexAttribArray(quadUvLoc)
        glVertexAttribPointer(quadUvLoc, 2, GL_FLOAT, false, 2 * 4, quadUv)

        glDrawArrays(GL_TRIANGLE_STRIP, 0, 4)

        glDisableVertexAttribArray(quadPosLoc)
        glDisableVertexAttribArray(quadUvLoc)
        glBindTexture(GL_TEXTURE_2D, 0)
    }

    private fun createFbo(fboWidth: Int, fboHeight: Int) {
        // 纹理：作为 color attachment
        glGenTextures(1, fboTexId, 0)
        glBindTexture(GL_TEXTURE_2D, fboTexId[0])
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE)

        // 分配纹理存储（关键：null 数据，只要开辟空间）
        glTexImage2D(
            GL_TEXTURE_2D,
            0,
            GL_RGBA,
            fboWidth,
            fboHeight,
            0,
            GL_RGBA,
            GL_UNSIGNED_BYTE,
            null
        )

        // FBO
        glGenFramebuffers(1, fboId, 0)
        glBindFramebuffer(GL_FRAMEBUFFER, fboId[0])

        glFramebufferTexture2D(
            GL_FRAMEBUFFER,
            GL_COLOR_ATTACHMENT0,
            GL_TEXTURE_2D,
            fboTexId[0],
            0
        )

        val status = glCheckFramebufferStatus(GL_FRAMEBUFFER)
        if (status != GL_FRAMEBUFFER_COMPLETE) {
            throw RuntimeException("FBO incomplete, status = 0x${Integer.toHexString(status)}")
        }

        glBindFramebuffer(GL_FRAMEBUFFER, 0)
        glBindTexture(GL_TEXTURE_2D, 0)
    }

    private fun destroyFbo() {
        if (fboId[0] != 0) {
            glDeleteFramebuffers(1, fboId, 0)
            fboId[0] = 0
        }
        if (fboTexId[0] != 0) {
            glDeleteTextures(1, fboTexId, 0)
            fboTexId[0] = 0
        }
    }

    private fun buildProgram(vs: String, fs: String): Int {
        val v = compileShader(GL_VERTEX_SHADER, vs)
        val f = compileShader(GL_FRAGMENT_SHADER, fs)
        val p = glCreateProgram()
        glAttachShader(p, v)
        glAttachShader(p, f)
        glLinkProgram(p)

        val link = IntArray(1)
        glGetProgramiv(p, GL_LINK_STATUS, link, 0)
        if (link[0] == 0) {
            val log = glGetProgramInfoLog(p)
            glDeleteProgram(p)
            throw RuntimeException("link program failed: $log")
        }
        glDeleteShader(v)
        glDeleteShader(f)
        return p
    }

    private fun compileShader(type: Int, src: String): Int {
        val s = glCreateShader(type)
        glShaderSource(s, src)
        glCompileShader(s)
        val ok = IntArray(1)
        glGetShaderiv(s, GL_COMPILE_STATUS, ok, 0)
        if (ok[0] == 0) {
            val log = glGetShaderInfoLog(s)
            glDeleteShader(s)
            throw RuntimeException("compile shader failed: $log")
        }
        return s
    }

    private fun fbOf(arr: FloatArray): FloatBuffer =
        ByteBuffer.allocateDirect(arr.size * 4).order(ByteOrder.nativeOrder())
            .asFloatBuffer().apply {
                put(arr)
                position(0)
            }
}
