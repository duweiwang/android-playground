package com.example.wangduwei.demos.gles.texture2;

import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.wangduwei.demos.R;
import com.example.wangduwei.demos.gles.ShaderHelper;
import com.example.wangduwei.demos.gles.TextureHelper;
import com.example.wangduwei.demos.main.BaseSupportFragment;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * @desc: 纹理使用拆解
 * @auther:duwei
 * @date:2019/3/4
 */
public class Texture2Fragment extends BaseSupportFragment implements GLSurfaceView.Renderer {
    //变换矩阵
    //顶点坐标
    //纹理坐标
    private static final String VERTEX_SHADER = "uniform mat4 u_Matrix;\n" +
            "\n" +
            "attribute vec4 a_Position;\n" +
            "attribute vec2 a_TextureCoordinates;\n" +
            "\n" +
            "varying vec2 v_TextureCoordinates;\n" +
            "\n" +
            "void main(){\n" +
            "\n" +
            "    v_TextureCoordinates = a_TextureCoordinates;\n" +
            "    gl_Position  = u_Matrix * a_Position;\n" +
            "\n" +
            "}";
    private static final String FRAGMENT_SHADER = "precision mediump float;\n" +
            "\n" +
            "uniform sampler2D u_TextureUnit;\n" +
            "varying vec2 v_TextureCoordinates;\n" +
            "\n" +
            "void main(){\n" +
            "    gl_FragColor = texture2D(u_TextureUnit , v_TextureCoordinates);\n" +
            "\n" +
            "}";

    private final float[] projectionMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];

    private FloatBuffer bPos;
    private FloatBuffer bCoord;

    //顶点坐标
    private final float[] sPos = {
            -1.0f, 1.0f,
            -1.0f, -1.0f,
            1.0f, 1.0f,
            1.0f, -1.0f
    };

    private final float[] sCoord = {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
    };


    private int textureId;
    private int mProgram;

    private  int uMatrixLocation;
    private  int uTextureUnitLocation;

    private  int aPositionLocation;
    private  int aTextureCoordinatesLocation;
    //uniform
    protected static final String U_MATRIX = "u_Matrix";
    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";

    //Attribute
    protected static final String A_POSITION = "a_Position";
    protected static final String A_COLOR = "a_Color";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";

    int bitmapWidth,bitmapHeight;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        GLSurfaceView glSurfaceView = new GLSurfaceView(getActivity());

        glSurfaceView.setEGLContextClientVersion(2);
        glSurfaceView.setRenderer(this);

       return glSurfaceView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(),R.drawable.five_start_mark1,options);
        bitmapHeight = options.outHeight;
        bitmapWidth = options.outWidth;

        //本地化顶点坐标
        ByteBuffer bb = ByteBuffer.allocateDirect(sPos.length * 4);
        bb.order(ByteOrder.nativeOrder());
        bPos = bb.asFloatBuffer();
        bPos.put(sPos);
        bPos.position(0);

        //本地化纹理坐标
        ByteBuffer cc = ByteBuffer.allocateDirect(sCoord.length * 4);
        cc.order(ByteOrder.nativeOrder());
        bCoord = cc.asFloatBuffer();
        bCoord.put(sCoord);
        bCoord.position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(1f, 1f, 1f, 1f);

        textureId = TextureHelper.loadTexture(getActivity(), R.drawable.five_start_mark1);

        mProgram = ShaderHelper.buildProgram(VERTEX_SHADER, FRAGMENT_SHADER);

        uMatrixLocation = GLES20.glGetUniformLocation(mProgram, U_MATRIX);
        uTextureUnitLocation = GLES20.glGetUniformLocation(mProgram, U_TEXTURE_UNIT);

        aPositionLocation = GLES20.glGetAttribLocation(mProgram, A_POSITION);
        aTextureCoordinatesLocation = GLES20.glGetAttribLocation(mProgram, A_TEXTURE_COORDINATES);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        int w = bitmapWidth;
        int h = bitmapHeight;
        float sWH = w / (float) h;//图片的宽高比
        float sWidthHeight = width / (float) height;//视图的宽高比

//        uXY = sWidthHeight;
        if (width > height) {//视图是横着的，top，bottom是不变的，需要调整左右
            if (sWH > sWidthHeight) {
                Matrix.orthoM(projectionMatrix, 0,
                        -sWidthHeight * sWH, sWidthHeight * sWH,
                        -1, 1,
                        3, 5);
            } else {
                Matrix.orthoM(projectionMatrix, 0,
                        -sWidthHeight / sWH,
                        sWidthHeight / sWH,
                        -1, 1,
                        3, 5);
            }
        } else {//视图是竖着的，需要调整上下
            if (sWH > sWidthHeight) {
                Matrix.orthoM(projectionMatrix, 0,
                        -1, 1,
                        -1 / sWidthHeight * sWH, 1 / sWidthHeight * sWH,
                        3, 5);
            } else {
                Matrix.orthoM(projectionMatrix, 0,
                        -1, 1,
                        -sWH / sWidthHeight, sWH / sWidthHeight,
                        3, 5);
            }
        }
        //设置相机位置
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 5.0f,
                0f, 0f, 0f,
                0f, 1.0f, 0.0f);//摄像头的up向上
        //计算变换矩阵
        Matrix.multiplyMM(mMVPMatrix, 0, projectionMatrix, 0, mViewMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glUseProgram(mProgram);

        //传递矩阵给shader program
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0);
        //活动的纹理单元设置为0号单元
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        //纹理绑定到这个单元
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        //把纹理单元传递给片段着色器
        GLES20.glUniform1i(uTextureUnitLocation, 0);

        bPos.position(0);
        GLES20.glVertexAttribPointer(aPositionLocation,
                2,
                GLES20.GL_FLOAT,
                false,
                16,
                bPos);

        GLES20.glEnableVertexAttribArray(aPositionLocation);

        bPos.position(0);

//-----------------
        bCoord.position(2);
        GLES20.glVertexAttribPointer(aTextureCoordinatesLocation,
                2,
                GLES20.GL_FLOAT,
                false,
                16,
                bCoord);

        GLES20.glEnableVertexAttribArray(aTextureCoordinatesLocation);

        bCoord.position(0);


        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }
}
