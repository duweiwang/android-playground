package com.example.lib_gles.render;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.example.lib_gles.Constant;
import com.example.lib_gles.MatrixHelper;
import com.example.lib_gles.ShaderHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * 空气曲棍球项目的代码
 * 1、三角形扇
 * 2、画线
 * 3、画2个点
 */
public class OpenGLRender implements GLSurfaceView.Renderer {
    //002:used for triangle fan
    /* private float[] vertex = {
            0f, 0f,
            -0.5f, -0.5f,
            0.5f, -0.5f,
            0.5f, 0.5f,
            -0.5f, 0.5f,
            -0.5f, -0.5f,

            -0.5f, 0f,
            0.5f, 0f,

            0f, -0.25f,
            0f, 0.25f
    };*/


    //001:used for triangle
   /* private float[] vertex = {
            -0.5f, -0.5f,
            0.5f,0.5f,
            -0.5f, 0.5f,

            -0.5f, -0.5f,
            0.5f, -0.5f,
            0.5f, 0.5f,

            -0.5f, 0f,
            0.5f, 0f,

            0f, -0.25f,
            0f, 0.25f
    };*/
    //003:X,Y,Z,W,R,G,B
    private float[] vertex = {
            0f, 0f, 0f, /*1.5f,*/ 1f, 1f, 1f,
            -0.5f, -0.5f, 0f, /*1f,*/ 0.7f, 0.7f, 0.7f,
            0.5f, -0.5f, 0f, /*1f,*/ 0.7f, 0.7f, 0.7f,
            0.5f, 0.5f, 0f, /*2f,*/ 0.7f, 0.7f, 0.7f,
            -0.5f, 0.5f, 0f, /*2f,*/ 0.7f, 0.7f, 0.7f,
            -0.5f, -0.5f, 0f, /*1f,*/ 0.7f, 0.7f, 0.7f,

            -0.5f, 0f, 0f, /*1.5f,*/ 1f, 0f, 0f,
            0.5f, 0f, 0f, /*1.5f,*/ 1f, 0f, 0f,

            0f, -0.25f, 0f, /*1.25f,*/ 0f, 0f, 1f,
            0f, 0.25f, 0f, /*1.75f,*/ 1f, 0f, 0f
    };

    private final FloatBuffer vertexData;

    private int program;

    private int aPositionLocation;
    private static final String A_POSITION = "a_Position";
    private int aColorLocation;
    private static final String A_COLOR = "a_Color";
    private int uMatrixLocation;
    private static final String U_MATRIX = "u_Matrix";
    //    private int uColorLocation;
//    private static final String U_COLOR = "u_Color";
    //投影矩阵
    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16];


    public OpenGLRender(Context context) {
        vertexData = ByteBuffer.allocateDirect(vertex.length * Constant.BYTE_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();

        vertexData.put(vertex);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        GLES20.glClearColor(0f, 0f, 0f, 0f);

        String vertexShader = "uniform mat4 u_Matrix;\n" +
                "attribute vec4 a_Position;\n" +
                "attribute vec4 a_Color;\n" +
                "\n" +
                "varying vec4 v_Color;\n" +
                "\n" +
                "void main(){\n" +
                "    v_Color = a_Color;\n" +
                "\n" +
                "    gl_Position = u_Matrix * a_Position;\n" +
                "    gl_PointSize = 10.0;\n" +
                "}";
        String fragmentShader = "precision mediump float;\n" +
                "\n" +
                "varying vec4 v_Color;\n" +
                "\n" +
                "\n" +
                "void main(){\n" +
                "    gl_FragColor = v_Color;\n" +
                "}";

        int vertexShaderC = ShaderHelper.compileVertexShader(vertexShader);
        int fragmentShaderC = ShaderHelper.compileFragmentShader(fragmentShader);


        program = ShaderHelper.linkProgram(vertexShaderC, fragmentShaderC);

        GLES20.glUseProgram(program);

        //链接程序后，每个属性会对应一个位置坐标，我们获取它
//        uColorLocation = GLES20.glGetUniformLocation(program, U_COLOR);
        aColorLocation = GLES20.glGetAttribLocation(program, A_COLOR);
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX);
//位置
        vertexData.position(0);
        GLES20.glVertexAttribPointer(aPositionLocation, //属性位置
                Constant.POSITION_COMPONENT_COUNT,//对于每个属性，有多少个分量与每一个顶点相关联
                GLES20.GL_FLOAT, //数据类型
                false, //使用整型数据才有意义，
//                0, //只有当一个数组存储多于一个属性它才有意义
                Constant.STRIDE,
                vertexData);//告诉openGl去哪里读数据
        GLES20.glEnableVertexAttribArray(aPositionLocation);
//颜色
        vertexData.position(Constant.POSITION_COMPONENT_COUNT);//从2位置开始读颜色
        GLES20.glVertexAttribPointer(aColorLocation,
                Constant.COLOR_COMPONENT_COUNT,
                GLES20.GL_FLOAT,
                false,
                Constant.STRIDE,
                vertexData);
        GLES20.glEnableVertexAttribArray(aColorLocation);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
     /*   final float aspectRatio = width > height ?
                (float) width / (float) height : (float) height / (float) width;

        if (width > height) {
            Matrix.orthoM(projectionMatrix,
                    0,
                    -aspectRatio,//left
                    aspectRatio,//right
                    -1f,
                    1f,
                    -1f,
                    1f);
        } else {
            Matrix.orthoM(projectionMatrix,
                    0,
                    -1f,
                    1f,
                    -aspectRatio,
                    aspectRatio,
                    -1f,
                    1f);
        }*/

        //创建透视投影，视椎体从Z = -1，到Z = -10.
        MatrixHelper.perspectiveM(projectionMatrix,
                45,
                (float) width / (float) height,
                1f,
                10f);

        Matrix.setIdentityM(modelMatrix, 0);//模型矩阵设置为单位矩阵
        Matrix.translateM(modelMatrix, 0, 0f, 0f, -2.5f);//Z轴平移-2.5
        Matrix.rotateM(modelMatrix, 0, -60f, 1f, 0f, 0f);

        final float[] temp = new float[16];//临时矩阵
        Matrix.multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);//相乘投影矩阵和模型矩阵存入临时矩阵
        System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);//结果存回projectionMatrix
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glUniformMatrix4fv(uMatrixLocation,
                1,
                false,
                projectionMatrix,
                0);

        //画两个三角形
//        GLES20.glUniform4f(uColorLocation, 1f, 1f, 1f, 1f);//设置u_Color的值为白色
//        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);//画三角形，从0位置开始，读6个点
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6);//画三角形，从0位置开始，读6个点

        //画线
//        GLES20.glUniform4f(uColorLocation, 1f, 0f, 0f, 1f);
        GLES20.glDrawArrays(GLES20.GL_LINES, 6, 2);

//        GLES20.glUniform4f(uColorLocation, 0f, 0f, 1f, 1f);
        GLES20.glDrawArrays(GLES20.GL_POINTS, 8, 1);

//        GLES20.glUniform4f(uColorLocation, 1f, 0f, 0f, 1f);
        GLES20.glDrawArrays(GLES20.GL_POINTS, 9, 1);
    }
}
