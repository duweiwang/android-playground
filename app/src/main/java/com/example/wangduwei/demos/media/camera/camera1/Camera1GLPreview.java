package com.example.wangduwei.demos.media.camera.camera1;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.lib_processor.PageInfo;
import com.example.wangduwei.demos.R;
import com.example.wangduwei.demos.main.BaseSupportFragment;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @desc: 使用openGL纹理预览相机, 产生黑白预览
 * 相机不是重点，所以直接打开了，重点是黑白预览
 * @auther:duwei
 * @date:2019/3/6
 */
@PageInfo(description = "Camera1外部纹理", navigationId = R.id.fragment_camera_one_glpreview)
public class Camera1GLPreview extends BaseSupportFragment implements GLSurfaceView.Renderer, View.OnClickListener {
    private Camera mCamera;
    private SurfaceTexture mSurfaceTexture;
    private GLSurfaceView mGLSurfaceView;
    private float[] transformMatrix = new float[16];
    private int mOESTextureId;
    private boolean mIsPreviewStarted;
    private FloatBuffer mVertextBuffer;
    //每行前两个值为顶点坐标，后两个为纹理坐标
    private static final float[] vertexData = {
            1f, 1f, 1f, 1f,
            -1f, 1f, 0f, 1f,
            -1f, -1f, 0f, 0f,
            1f, 1f, 1f, 1f,
            -1f, -1f, 0f, 0f,
            1f, -1f, 1f, 0f
    };
    private Camera1GLPreviewShader camera1GLPreviewShader;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_filter, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mGLSurfaceView = view.findViewById(R.id.filter_surfaceview);
        mGLSurfaceView.setEGLContextClientVersion(2);
        mGLSurfaceView.setRenderer(this);
        view.findViewById(R.id.filter_style1).setOnClickListener(this);
        view.findViewById(R.id.filter_style2).setOnClickListener(this);
        view.findViewById(R.id.filter_style3).setOnClickListener(this);


        mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        mCamera.setDisplayOrientation(90);

        mVertextBuffer = createBuffer(vertexData);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0f, 0f, 0f, 0f);

        mOESTextureId = createOESTexture();

        camera1GLPreviewShader = new Camera1GLPreviewShader(getActivity());
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if (mSurfaceTexture != null) {

            mSurfaceTexture.updateTexImage();  //更新纹理图像
            //获取外部纹理的矩阵，用来确定纹理的采样位置，没有此矩阵可能导致图像翻转等问题
            mSurfaceTexture.getTransformMatrix(transformMatrix);
        }
        if (!mIsPreviewStarted) {
            mIsPreviewStarted = initSurfaceTexture();
            mIsPreviewStarted = true;
            return;
        }

        camera1GLPreviewShader.setUniforms(transformMatrix, mOESTextureId);

        //将顶点和纹理坐标传给顶点着色器
        if (mVertextBuffer != null) {
            //顶点坐标从位置0开始读取
            mVertextBuffer.position(0);
            //使能顶点属性
            GLES20.glEnableVertexAttribArray(camera1GLPreviewShader.getaPositionLocation());
            //顶点坐标每次读取两个顶点值，之后间隔16（每行4个值 * 4个字节）的字节继续读取两个顶点值
            GLES20.glVertexAttribPointer(camera1GLPreviewShader.getaPositionLocation(),
                    2, GLES20.GL_FLOAT, false, 16, mVertextBuffer);

            //纹理坐标从位置2开始读取
            mVertextBuffer.position(2);
            GLES20.glEnableVertexAttribArray(camera1GLPreviewShader.getaTextureCoordLocation());
            //纹理坐标每次读取两个顶点值，之后间隔16（每行4个值 * 4个字节）的字节继续读取两个顶点值
            GLES20.glVertexAttribPointer(camera1GLPreviewShader.getaTextureCoordLocation(),
                    2, GLES20.GL_FLOAT, false, 16, mVertextBuffer);
        }

        //绘制两个三角形（6个顶点）
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
    }


    private int createOESTexture() {
        int[] tex = new int[1];
        GLES20.glGenTextures(1, tex, 0);
        //将此纹理绑定到外部纹理上
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, tex[0]);
        //设置纹理过滤参数
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
        //解除纹理绑定
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0);
        return tex[0];
    }

    public FloatBuffer createBuffer(float[] vertexData) {
        FloatBuffer buffer = ByteBuffer.allocateDirect(vertexData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        buffer.put(vertexData, 0, vertexData.length).position(0);
        return buffer;
    }


    //在onDrawFrame方法中调用此方法
    public boolean initSurfaceTexture() {
        mSurfaceTexture = new SurfaceTexture(mOESTextureId); //根据外部纹理ID创建SurfaceTexture
        mSurfaceTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
            @Override
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                //每获取到一帧数据时请求OpenGL ES进行渲染
                mGLSurfaceView.requestRender();
            }
        });
        //讲此SurfaceTexture作为相机预览输出
        try {
            mCamera.setPreviewTexture(mSurfaceTexture);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //开启预览
        mCamera.startPreview();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.filter_style1:

                break;
            case R.id.filter_style2:

                break;
            case R.id.filter_style3:

                break;
            default:
                break;

        }
    }
}
