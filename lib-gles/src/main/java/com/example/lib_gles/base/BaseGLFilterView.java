package com.example.lib_gles.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;

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
 * OpenGL 滤镜 View 基类
 * 参考 GPUImageFilter 设计，封装通用逻辑
 */
public abstract class BaseGLFilterView extends GLSurfaceView {

    private static final String TAG = "BaseGLFilterView";
    
    protected BaseRenderer mRenderer;

    public BaseGLFilterView(Context context) {
        this(context, null);
    }

    public BaseGLFilterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(3);
        mRenderer = createRenderer(context);
        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    /**
     * 子类实现：创建具体的 Renderer
     */
    protected abstract BaseRenderer createRenderer(Context context);

    /**
     * 设置图片资源
     */
    public void setImageResource(int resId) {
        if (mRenderer != null) {
            mRenderer.setImageResource(resId);
        }
    }

    /**
     * 设置动画帧数
     */
    public void setMaxFrames(int maxFrames) {
        if (mRenderer != null) {
            mRenderer.setMaxFrames(maxFrames);
        }
    }

    /**
     * 设置延迟帧数
     */
    public void setSkipFrames(int skipFrames) {
        if (mRenderer != null) {
            mRenderer.setSkipFrames(skipFrames);
        }
    }

    /**
     * OpenGL Renderer 基类
     */
    protected abstract static class BaseRenderer implements GLSurfaceView.Renderer {
        
        protected Context mContext;
        protected int mProgramId;
        protected int mTextureId;
        
        // Attribute locations
        protected int mPositionHandle;
        protected int mTextureCoordHandle;
        
        // Uniform locations
        protected int mMvpMatrixHandle;
        protected int mTextureHandle;
        
        protected FloatBuffer mVertexBuffer;
        protected FloatBuffer mTexCoordBuffer;
        
        // 帧动画参数
        protected int mFrames = 0;
        protected int mMaxFrames = 60;
        protected int mSkipFrames = 30;
        protected float mProgress = 0.0f;
        
        protected int mImageResId = 0;
        protected Bitmap mBitmap;
        protected boolean mTextureNeedsUpdate = false;
        
        // 标准顶点坐标
        protected static final float[] VERTEX_DATA = {
            -1.0f, -1.0f,  // 左下
             1.0f, -1.0f,  // 右下
            -1.0f,  1.0f,  // 左上
             1.0f,  1.0f   // 右上
        };
        
        // 标准纹理坐标
        protected static final float[] TEXTURE_DATA = {
            0.0f, 1.0f,  // 左下
            1.0f, 1.0f,  // 右下
            0.0f, 0.0f,  // 左上
            1.0f, 0.0f   // 右上
        };

        public BaseRenderer(Context context) {
            mContext = context;
            initBuffers();
        }
        
        /**
         * 初始化顶点和纹理坐标缓冲
         */
        protected void initBuffers() {
            mVertexBuffer = ByteBuffer.allocateDirect(VERTEX_DATA.length * 4)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer()
                    .put(VERTEX_DATA);
            mVertexBuffer.position(0);
            
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
            
            Log.d(TAG, "onSurfaceCreated: " + getClass().getSimpleName());
            
            // 加载着色器
            onInit();
            
            // 初始化完成回调
            onInitialized();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES30.glViewport(0, 0, width, height);
            onInputSizeChanged(width, height);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);
            
            // 更新纹理
            if (mTextureNeedsUpdate && mBitmap != null) {
                loadTexture(mBitmap);
                mTextureNeedsUpdate = false;
            }
            
            // 使用着色器程序
            GLES30.glUseProgram(mProgramId);
            
            // 更新帧动画进度
            updateProgress();
            
            // 绘制前准备
            onDrawPrepare();
            
            // 设置顶点属性
            GLES30.glEnableVertexAttribArray(mPositionHandle);
            GLES30.glVertexAttribPointer(mPositionHandle, 2, GLES30.GL_FLOAT, false, 0, mVertexBuffer);
            
            GLES30.glEnableVertexAttribArray(mTextureCoordHandle);
            GLES30.glVertexAttribPointer(mTextureCoordHandle, 2, GLES30.GL_FLOAT, false, 0, mTexCoordBuffer);
            
            // 绑定纹理
            GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, mTextureId);
            GLES30.glUniform1i(mTextureHandle, 0);
            
            // 绘制前设置（子类实现特效参数）
            onDrawArraysPre();
            
            // 执行绘制
            onDrawArrays();
            
            // 绘制后清理（子类实现）
            onDrawArraysAfter();
            
            // 清理顶点属性
            GLES30.glDisableVertexAttribArray(mPositionHandle);
            GLES30.glDisableVertexAttribArray(mTextureCoordHandle);
        }
        
        /**
         * 初始化着色器和程序
         */
        protected void onInit() {
            String vertexShader = loadShaderFromAssets(getVertexShader());
            String fragmentShader = loadShaderFromAssets(getFragmentShader());
            mProgramId = createProgram(vertexShader, fragmentShader);
            
            if (mProgramId == 0) {
                throw new RuntimeException("Failed to create shader program");
            }
            
            // 获取通用属性位置
            mPositionHandle = GLES30.glGetAttribLocation(mProgramId, "position");
            mTextureCoordHandle = GLES30.glGetAttribLocation(mProgramId, "inputTextureCoordinate");
            
            Log.d(TAG, "Position: " + mPositionHandle + ", TexCoord: " + mTextureCoordHandle);
            
            // 获取通用uniform位置
            mMvpMatrixHandle = GLES30.glGetUniformLocation(mProgramId, "uMvpMatrix");
            mTextureHandle = GLES30.glGetUniformLocation(mProgramId, "inputImageTexture");
            
            Log.d(TAG, "MVP: " + mMvpMatrixHandle + ", Texture: " + mTextureHandle);
            
            // 创建纹理
            mTextureId = createTexture();
            
            // 加载纹理
            if (mImageResId != 0 && mBitmap != null) {
                Log.d(TAG, "Loading user bitmap");
                loadTexture(mBitmap);
            } else {
                Log.d(TAG, "Loading default texture");
                loadDefaultTexture();
            }
        }
        
        /**
         * 初始化完成回调
         */
        protected void onInitialized() {
            // 子类可以覆盖
        }
        
        /**
         * 尺寸变化回调
         */
        protected void onInputSizeChanged(int width, int height) {
            // 子类可以覆盖
        }
        
        /**
         * 绘制前准备
         */
        protected void onDrawPrepare() {
            // 子类可以覆盖
        }
        
        /**
         * 绘制前设置（设置特效参数）
         */
        protected abstract void onDrawArraysPre();
        
        /**
         * 执行绘制
         */
        protected void onDrawArrays() {
            GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP, 0, 4);
        }
        
        /**
         * 绘制后清理
         */
        protected void onDrawArraysAfter() {
            // 子类可以覆盖
        }
        
        /**
         * 获取顶点着色器文件名
         */
        protected abstract String getVertexShader();
        
        /**
         * 获取片元着色器文件名
         */
        protected abstract String getFragmentShader();
        
        /**
         * 更新动画进度
         */
        protected void updateProgress() {
            mProgress = (float) mFrames / mMaxFrames;
            if (mProgress > 1.0f) {
                mProgress = 0.0f;
            }
            
            mFrames++;
            if (mFrames > mMaxFrames + mSkipFrames) {
                mFrames = 0;
            }
        }
        
        /**
         * 从 Assets 加载着色器
         */
        protected String loadShaderFromAssets(String filename) {
            StringBuilder sb = new StringBuilder();
            try {
                InputStream is = mContext.getAssets().open(filename);
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                reader.close();
                Log.d(TAG, "Loaded shader: " + filename);
            } catch (IOException e) {
                Log.e(TAG, "Failed to load shader: " + filename, e);
            }
            return sb.toString();
        }
        
        /**
         * 创建着色器程序
         */
        protected int createProgram(String vertexSource, String fragmentSource) {
            int vertexShader = loadShader(GLES30.GL_VERTEX_SHADER, vertexSource);
            if (vertexShader == 0) {
                Log.e(TAG, "Failed to load vertex shader");
                return 0;
            }
            
            int fragmentShader = loadShader(GLES30.GL_FRAGMENT_SHADER, fragmentSource);
            if (fragmentShader == 0) {
                Log.e(TAG, "Failed to load fragment shader");
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
                Log.e(TAG, "Program link error: " + log);
                GLES30.glDeleteProgram(program);
                return 0;
            }
            
            Log.d(TAG, "Program created successfully");
            return program;
        }
        
        /**
         * 加载着色器
         */
        protected int loadShader(int type, String shaderSource) {
            int shader = GLES30.glCreateShader(type);
            GLES30.glShaderSource(shader, shaderSource);
            GLES30.glCompileShader(shader);
            
            int[] compiled = new int[1];
            GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, compiled, 0);
            if (compiled[0] == 0) {
                String log = GLES30.glGetShaderInfoLog(shader);
                Log.e(TAG, "Shader compile error: " + log);
                GLES30.glDeleteShader(shader);
                return 0;
            }
            
            return shader;
        }
        
        /**
         * 创建纹理
         */
        protected int createTexture() {
            int[] textures = new int[1];
            GLES30.glGenTextures(1, textures, 0);
            
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textures[0]);
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR);
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE);
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE);
            
            return textures[0];
        }
        
        /**
         * 加载纹理
         */
        protected void loadTexture(Bitmap bitmap) {
            if (bitmap != null && !bitmap.isRecycled()) {
                GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, mTextureId);
                android.opengl.GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bitmap, 0);
            }
        }
        
        /**
         * 加载默认纹理
         */
        protected void loadDefaultTexture() {
            int width = 512;
            int height = 512;
            Bitmap defaultBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
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
