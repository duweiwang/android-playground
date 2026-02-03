package com.example.lib_gles.video_filter.core;

import static android.opengl.GLES20.GL_COLOR_ATTACHMENT0;
import static android.opengl.GLES20.GL_DEPTH_ATTACHMENT;
import static android.opengl.GLES20.GL_DEPTH_COMPONENT16;
import static android.opengl.GLES20.GL_FRAMEBUFFER;
import static android.opengl.GLES20.GL_FRAMEBUFFER_BINDING;
import static android.opengl.GLES20.GL_FRAMEBUFFER_COMPLETE;
import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_MAX_RENDERBUFFER_SIZE;
import static android.opengl.GLES20.GL_MAX_TEXTURE_SIZE;
import static android.opengl.GLES20.GL_NEAREST;
import static android.opengl.GLES20.GL_RENDERBUFFER;
import static android.opengl.GLES20.GL_RENDERBUFFER_BINDING;
import static android.opengl.GLES20.GL_RGBA;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_BINDING_2D;
import static android.opengl.GLES20.GL_UNSIGNED_BYTE;

import android.opengl.GLES20;
import android.util.Log;

import com.example.lib_gles.video_filter.utils.EglUtil;

/**
 * 这是一个FBO，绑定了GL_DEPTH_ATTACHMENT和GL_COLOR_ATTACHMENT0
 */
public class EFramebufferObject {

    private static final String TAG = "EFramebufferObject";
    private int width;
    private int height;
    public int framebufferName;
    private int renderbufferName;
    private int texName;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getTexName() {
        return texName;
    }

    public void setup(final int width, final int height) {
        final int[] args = new int[1];
        Log.d(TAG, "setup: ....");
        GLES20.glGetIntegerv(GL_MAX_TEXTURE_SIZE, args, 0);
        //获取GPU支持的最大2D纹理尺寸，硬件限制，返回4096-> 则4096*4096
        if (width > args[0] || height > args[0]) {
            throw new IllegalArgumentException("GL_MAX_TEXTURE_SIZE " + args[0]);
        }
        //GPU 支持的最大 Renderbuffer 尺寸,确保FBO 中的 depth/stencil/renderbuffer 不超过硬件限制
        GLES20.glGetIntegerv(GL_MAX_RENDERBUFFER_SIZE, args, 0);
        if (width > args[0] || height > args[0]) {
            throw new IllegalArgumentException("GL_MAX_RENDERBUFFER_SIZE " + args[0]);
        }
        //当前绑定的 Framebuffer 对象 ID, 0 → 默认帧缓冲（屏幕）,>0 → 自定义 FBO（离屏渲染）
        GLES20.glGetIntegerv(GL_FRAMEBUFFER_BINDING, args, 0);
        final int saveFramebuffer = args[0];
        //当前绑定的 Renderbuffer ID
        GLES20.glGetIntegerv(GL_RENDERBUFFER_BINDING, args, 0);
        final int saveRenderbuffer = args[0];
        //当前绑定到 GL_TEXTURE_2D 的纹理 ID
        GLES20.glGetIntegerv(GL_TEXTURE_BINDING_2D, args, 0);
        final int saveTexName = args[0];
        Log.d(TAG, "setup: saveFramebuffer:"+saveFramebuffer+", saveTexName:"+saveTexName);

        release();

        try {
            this.width = width;
            this.height = height;
            //创建并绑定 Framebuffer
            GLES20.glGenFramebuffers(args.length, args, 0);
            framebufferName = args[0];
            GLES20.glBindFramebuffer(GL_FRAMEBUFFER, framebufferName);
            //---------------------------------------------------------------
            //创建深度 Renderbuffer（Depth Buffer）
            GLES20.glGenRenderbuffers(args.length, args, 0);
            renderbufferName = args[0];
            GLES20.glBindRenderbuffer(GL_RENDERBUFFER, renderbufferName);
            //分配深度存储空间
            GLES20.glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT16, width, height);
            //绑定为 FBO 的深度附件
            GLES20.glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, renderbufferName);
            //---------------------------------------------------------------
            GLES20.glGenTextures(args.length, args, 0);
            texName = args[0];  // 这个纹理作为framebuffer的颜色缓冲区(GL_COLOR_ATTACHMENT0), 也就是视频流输出
            GLES20.glBindTexture(GL_TEXTURE_2D, texName);

            EglUtil.setupSampler(GL_TEXTURE_2D, GL_LINEAR, GL_NEAREST);

            GLES20.glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, null);
            GLES20.glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texName, 0);
            //---------------------------------------------------------------
            final int status = GLES20.glCheckFramebufferStatus(GL_FRAMEBUFFER);
            if (status != GL_FRAMEBUFFER_COMPLETE) {
                throw new RuntimeException("Failed to initialize framebuffer object " + status);
            }
        } catch (final RuntimeException e) {
            release();
            throw e;
        }

        GLES20.glBindFramebuffer(GL_FRAMEBUFFER, saveFramebuffer);
        GLES20.glBindRenderbuffer(GL_RENDERBUFFER, saveRenderbuffer);
        GLES20.glBindTexture(GL_TEXTURE_2D, saveTexName);
    }

    public void release() {
        final int[] args = new int[1];
        args[0] = texName;
        GLES20.glDeleteTextures(args.length, args, 0);
        texName = 0;
        args[0] = renderbufferName;
        GLES20.glDeleteRenderbuffers(args.length, args, 0);
        renderbufferName = 0;
        args[0] = framebufferName;
        GLES20.glDeleteFramebuffers(args.length, args, 0);
        framebufferName = 0;
    }

    public void enable() {
        GLES20.glBindFramebuffer(GL_FRAMEBUFFER, framebufferName);
    }


}
