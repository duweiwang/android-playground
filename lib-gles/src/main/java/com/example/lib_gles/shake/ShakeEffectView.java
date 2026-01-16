package com.example.lib_gles.shake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.AttributeSet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Shake Effect View - 抖动特效View
 * 通过缩放和RGB通道偏移实现抖动效果
 */
public class ShakeEffectView extends GLSurfaceView {

    private ShakeEffectRenderer mRenderer;

    public ShakeEffectView(Context context) {
        this(context, null);
    }

    public ShakeEffectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(3);
        mRenderer = new ShakeEffectRenderer(context);
        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    /**
     * 设置要显示的图片资源ID
     */
    public void setImageResource(int resId) {
        if (mRenderer != null) {
            mRenderer.setImageResource(resId);
        }
    }

    /**
     * 设置动画帧数（默认60帧）
     */
    public void setMaxFrames(int maxFrames) {
        if (mRenderer != null) {
            mRenderer.setMaxFrames(maxFrames);
        }
    }

    /**
     * 设置延迟帧数（默认30帧）
     */
    public void setSkipFrames(int skipFrames) {
        if (mRenderer != null) {
            mRenderer.setSkipFrames(skipFrames);
        }
    }

    private static class ShakeEffectRenderer implements GLSurfaceView.Renderer {
        
        private Context mContext;
        private int mProgramId;
        private int mTextureId;
        
        // Attribute locations
        private int mPositionHandle;
        private int mTextureCoordHandle;
        
        // Uniform locations
        private int mMvpMatrixHandle;
        private int mTextureHandle;
        private int mTextureCoordOffsetHandle;
        
        private FloatBuffer mVertexBuffer;
        private FloatBuffer mTexCoordBuffer;
        
        private float[] mScaleMatrix = new float[16];
        
        // 抖动动画参数
        private int mFrames = 0;
        private int mMaxFrames = 60;  // 动画总帧数
        private int mSkipFrames = 30;  // 跳过帧数（延迟）
        private float mProgress = 0.0f;
        
        private int mImageResId = 0;
        private Bitmap mBitmap;
        private boolean mTextureNeedsUpdate = false;
        
        // 顶点坐标（矩形，两个三角形）
        private final float[] VERTEX_DATA = {
            -1.0f, -1.0f,  // 左下
             1.0f, -1.0f,  // 右下
            -1.0f,  1.0f,  // 左上
             1.0f,  1.0f   // 右上
        };
        
        // 纹理坐标
        private final float[] TEXTURE_DATA = {
            0.0f, 1.0f,  // 左下
            1.0f, 1.0f,  // 右下
            0.0f, 0.0f,  // 左上
            1.0f, 0.0f   // 右上
        };

        public ShakeEffectRenderer(Context context) {
            mContext = context;
            
            // 初始化顶点缓冲
            mVertexBuffer = ByteBuffer.allocateDirect(VERTEX_DATA.length * 4)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer()
                    .put(VERTEX_DATA);
            mVertexBuffer.position(0);
            
            // 初始化纹理坐标缓冲
            mTexCoordBuffer = ByteBuffer.allocateDirect(TEXTURE_DATA.length * 4)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer()
                    .put(TEXTURE_DATA);
            mTexCoordBuffer.position(0);
        }
        
        public void setImageResource(int resId) {
            mImageResId = resId;
            if (mBitmap != null && !mBitmap.isRecycled()) {
                mBitmap.recycle();
            }
            mBitmap = BitmapFactory.decodeResource(mContext.getResources(), resId);
            mTextureNeedsUpdate = true;
        }
        
        public void setMaxFrames(int maxFrames) {
            this.mMaxFrames = maxFrames;
        }
        
        public void setSkipFrames(int skipFrames) {
            this.mSkipFrames = skipFrames;
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            
            android.util.Log.d("ShakeEffectView", "onSurfaceCreated");
            
            // 加载着色器程序
            String vertexShader = loadShaderFromAssets("soulout_v.glsl");
            String fragmentShader = loadShaderFromAssets("shakeeffect_f.glsl");
            mProgramId = createProgram(vertexShader, fragmentShader);
            
            if (mProgramId == 0) {
                throw new RuntimeException("Failed to create shader program");
            }
            
            // 获取属性位置
            mPositionHandle = GLES30.glGetAttribLocation(mProgramId, "position");
            mTextureCoordHandle = GLES30.glGetAttribLocation(mProgramId, "inputTextureCoordinate");
            
            android.util.Log.d("ShakeEffectView", "Position handle: " + mPositionHandle + ", TexCoord handle: " + mTextureCoordHandle);
            
            // 获取uniform位置
            mMvpMatrixHandle = GLES30.glGetUniformLocation(mProgramId, "uMvpMatrix");
            mTextureHandle = GLES30.glGetUniformLocation(mProgramId, "inputImageTexture");
            mTextureCoordOffsetHandle = GLES30.glGetUniformLocation(mProgramId, "uTextureCoordOffset");
            
            android.util.Log.d("ShakeEffectView", "MVP handle: " + mMvpMatrixHandle + ", Texture handle: " + mTextureHandle + ", Offset handle: " + mTextureCoordOffsetHandle);
            
            // 创建纹理
            mTextureId = createTexture();
            
            // 如果有设置的图片，加载它；否则创建默认渐变纹理
            if (mImageResId != 0 && mBitmap != null) {
                android.util.Log.d("ShakeEffectView", "Loading user bitmap");
                loadTexture(mBitmap);
            } else {
                // 创建默认的彩色渐变纹理
                android.util.Log.d("ShakeEffectView", "Loading default texture");
                loadDefaultTexture();
            }
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES30.glViewport(0, 0, width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);
            
            // 如果纹理需要更新
            if (mTextureNeedsUpdate && mBitmap != null) {
                loadTexture(mBitmap);
                mTextureNeedsUpdate = false;
            }
            
            // 使用着色器程序
            GLES30.glUseProgram(mProgramId);
            
            // 计算动画进度
            updateProgress();
            
            // 计算缩放：1.0 -> 1.2
            float scale = 1.0f + 0.2f * mProgress;
            Matrix.setIdentityM(mScaleMatrix, 0);
            Matrix.scaleM(mScaleMatrix, 0, scale, scale, 1.0f);
            
            // 计算纹理坐标偏移量
            float textureCoordOffset = 0.01f * mProgress;
            
            // 设置uniform变量
            GLES30.glUniformMatrix4fv(mMvpMatrixHandle, 1, false, mScaleMatrix, 0);
            GLES30.glUniform1f(mTextureCoordOffsetHandle, textureCoordOffset);
            
            // 设置顶点属性
            GLES30.glEnableVertexAttribArray(mPositionHandle);
            GLES30.glVertexAttribPointer(mPositionHandle, 2, GLES30.GL_FLOAT, false, 0, mVertexBuffer);
            
            GLES30.glEnableVertexAttribArray(mTextureCoordHandle);
            GLES30.glVertexAttribPointer(mTextureCoordHandle, 2, GLES30.GL_FLOAT, false, 0, mTexCoordBuffer);
            
            // 绑定纹理
            GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, mTextureId);
            GLES30.glUniform1i(mTextureHandle, 0);
            
            // 绘制
            GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP, 0, 4);
            
            // 清理
            GLES30.glDisableVertexAttribArray(mPositionHandle);
            GLES30.glDisableVertexAttribArray(mTextureCoordHandle);
        }
        
        private void updateProgress() {
            // 计算动画进度：0 -> 1 -> 暂停 -> 0 -> 1...
            mProgress = (float) mFrames / mMaxFrames;
            if (mProgress > 1.0f) {
                mProgress = 0.0f;
            }
            
            mFrames++;
            if (mFrames > mMaxFrames + mSkipFrames) {
                mFrames = 0;
            }
        }
        
        private String loadShaderFromAssets(String filename) {
            StringBuilder sb = new StringBuilder();
            try {
                InputStream is = mContext.getAssets().open(filename);
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                reader.close();
                android.util.Log.d("ShakeEffectView", "Loaded shader: " + filename);
            } catch (IOException e) {
                android.util.Log.e("ShakeEffectView", "Failed to load shader: " + filename, e);
                e.printStackTrace();
            }
            return sb.toString();
        }
        
        private int createProgram(String vertexSource, String fragmentSource) {
            int vertexShader = loadShader(GLES30.GL_VERTEX_SHADER, vertexSource);
            if (vertexShader == 0) {
                android.util.Log.e("ShakeEffectView", "Failed to load vertex shader");
                return 0;
            }
            
            int fragmentShader = loadShader(GLES30.GL_FRAGMENT_SHADER, fragmentSource);
            if (fragmentShader == 0) {
                android.util.Log.e("ShakeEffectView", "Failed to load fragment shader");
                return 0;
            }
            
            int program = GLES30.glCreateProgram();
            GLES30.glAttachShader(program, vertexShader);
            GLES30.glAttachShader(program, fragmentShader);
            GLES30.glLinkProgram(program);
            
            int[] linkStatus = new int[1];
            GLES30.glGetProgramiv(program, GLES30.GL_LINK_STATUS, linkStatus, 0);
            if (linkStatus[0] == 0) {
                String log = GLES30.glGetProgramInfoLog(program);
                android.util.Log.e("ShakeEffectView", "Program link error: " + log);
                GLES30.glDeleteProgram(program);
                return 0;
            }
            
            android.util.Log.d("ShakeEffectView", "Program created successfully");
            return program;
        }
        
        private int loadShader(int type, String shaderSource) {
            int shader = GLES30.glCreateShader(type);
            GLES30.glShaderSource(shader, shaderSource);
            GLES30.glCompileShader(shader);
            
            int[] compiled = new int[1];
            GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, compiled, 0);
            if (compiled[0] == 0) {
                String log = GLES30.glGetShaderInfoLog(shader);
                android.util.Log.e("ShakeEffectView", "Shader compile error: " + log);
                GLES30.glDeleteShader(shader);
                return 0;
            }
            
            return shader;
        }
        
        private int createTexture() {
            int[] textures = new int[1];
            GLES30.glGenTextures(1, textures, 0);
            
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textures[0]);
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR);
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE);
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE);
            
            return textures[0];
        }
        
        private void loadTexture(Bitmap bitmap) {
            if (bitmap != null && !bitmap.isRecycled()) {
                GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, mTextureId);
                android.opengl.GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bitmap, 0);
            }
        }
        
        private void loadDefaultTexture() {
            // 创建一个512x512的渐变纹理
            int width = 512;
            int height = 512;
            Bitmap defaultBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            
            // 绘制彩色渐变效果
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    // 计算颜色渐变
                    float fx = (float) x / width;
                    float fy = (float) y / height;
                    
                    int r = (int) (fx * 255);
                    int g = (int) (fy * 255);
                    int b = (int) ((1.0f - fx) * 255);
                    int a = 255;
                    
                    int color = (a << 24) | (r << 16) | (g << 8) | b;
                    defaultBitmap.setPixel(x, y, color);
                }
            }
            
            loadTexture(defaultBitmap);
            defaultBitmap.recycle();
        }
    }
}
