package com.example.lib_gles.texture;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;


import com.example.lib_gles.MatrixHelper;
import com.example.lib_gles.R;
import com.example.lib_gles.TextureHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


/**
 * 第七章；纹理
 */
public class OpenGLTextureRender implements GLSurfaceView.Renderer {

    private final Context context;
    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16];

    private Table table;
    private Mallet mallet;

    private TextureShaderProgram textureShaderProgram;
    private ColorShaderProgram colorShaderProgram;

    private int texture;

    public OpenGLTextureRender(Context context) {
        this.context = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        //清屏
        GLES20.glClearColor(0f, 0f, 0f, 0f);
        //初始化对象
        table = new Table();
        mallet = new Mallet();

        textureShaderProgram = new TextureShaderProgram(context);
        colorShaderProgram = new ColorShaderProgram(context);
        //加载纹理，返回纹理ID
        texture = TextureHelper.loadTexture(context, R.drawable.air_hockey_surface);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        MatrixHelper.perspectiveM(projectionMatrix,
                45,
                (float) width / (float) height,
                1f,
                10f);

        Matrix.setIdentityM(modelMatrix, 0);//模型矩阵设置为单位矩阵
        Matrix.translateM(modelMatrix, 0, 0f, 0f, -2.5f);//Z轴平移-2.5
        Matrix.rotateM(modelMatrix, 0, -60f, 1f, 0f, 0f);

        final float[] temp = new float[16];//临时矩阵
        //相乘投影矩阵和模型矩阵存入临时矩阵
        Matrix.multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);

        System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);//结果存回projectionMatrix
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        //TABLE
        textureShaderProgram.useProgram();
        textureShaderProgram.setUniforms(projectionMatrix, texture);
        table.bindData(textureShaderProgram);
        table.draw();

        //mallets
        colorShaderProgram.useProgram();
        colorShaderProgram.setUniforms(projectionMatrix);
        mallet.bindData(colorShaderProgram);
        mallet.draw();

    }
}
