package com.example.wangduwei.demos.gles.shape.circle;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.example.wangduwei.demos.gles.shape.IShape;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;

/**
 * 打算画一个圆，用TRIANGLE_FAN,边缘的顶点数决定圆的润滑程度
 */
public class Circle implements IShape {
    private static final int BYTES_PER_FLOAT = 4;
    /**
     * 每个点有两个坐标，X,Y
     */
    private static final int NUM_PER_POINT = 2;
    /**
     * 圆边上默认有DEFAULT_SIDE_NUM个点
     */
    private static final int DEFAULT_SIDE_NUM = 20;

    private static final String VERTEX_SHADER = "attribute vec4 a_Position;\n" +
            "uniform mat4 u_Matrix;" +
            "\n" +
            "void main(){\n" +
            "    gl_Position = u_Matrix * a_Position;\n" +
            "    gl_PointSize = 10.0;\n" +
            "}";
    private static final String FRAGMENT_SHADER = "precision mediump float;\n" +
            "\n" +
            "uniform vec4 u_Color;\n" +
            "\n" +
            "void main(){\n" +
            "    gl_FragColor = u_Color;\n" +
            "}";

    private static final String A_POSITION = "a_Position";
    private static final String U_COLOR = "u_Color";
    private static final String U_MATRIX = "u_Matrix";
    private int a_PositionLocation;
    private int u_ColorLocation;
    private int u_MatrixLocation;
    //圆的圆心和半径
    private float x;
    private float y;
    private float r;
    private float[] vertex;
    private final float[] projectionMatrix = new float[16];//正交投影
    private final float[] modelMatrix = new float[16];//模型矩阵
    private final float[] temp = new float[16];//临时矩阵

    private FloatBuffer vertexData;

    public Circle() {
        x = 0;
        y = 0;
        r = 0.3f;

        vertex = new float[(DEFAULT_SIDE_NUM + 2) * NUM_PER_POINT];
        int offset = 0;
        //圆心坐标
        vertex[offset++] = x;
        vertex[offset++] = y;
        //边缘点的坐标生成
        for (int i = 0; i <= DEFAULT_SIDE_NUM; i++) {
            //360分成十分
            float angleInRadians = ((float) i / (float) DEFAULT_SIDE_NUM) * ((float) Math.PI * 2f);

            vertex[offset++] = x + r * (float) Math.cos(angleInRadians);
            vertex[offset++] = y + r * (float) Math.sin(angleInRadians);
        }

        //将生成的数据保存到本地native
        vertexData = ByteBuffer.allocateDirect(vertex.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(vertex);
        vertexData.position(0);

        Matrix.setIdentityM(modelMatrix, 0);

        int vertexShader = compileVertexShader();
        int fragmentShader = compileFragmentShader();
        int program = linkProgram(vertexShader, fragmentShader);

        glUseProgram(program);

        setData2Program(program);

    }


    @Override
    public void draw() {
        GLES20.glUniformMatrix4fv(u_MatrixLocation, 1, false, projectionMatrix, 0);
        GLES20.glUniform4f(u_ColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, DEFAULT_SIDE_NUM + 2);
    }


    private int compileVertexShader() {
        int shader = glCreateShader(GL_VERTEX_SHADER);
        if (shader == 0) {
            return 0;
        }
        glShaderSource(shader, VERTEX_SHADER);
        glCompileShader(shader);

        final int[] status = new int[1];
        glGetShaderiv(shader, GL_COMPILE_STATUS, status, 0);
        if (status[0] == 0) {
            glDeleteShader(shader);
            return 0;
        }
        return shader;
    }

    private int compileFragmentShader() {
        int shader = glCreateShader(GL_FRAGMENT_SHADER);
        if (shader == 0) {
            return 0;
        }
        glShaderSource(shader, FRAGMENT_SHADER);
        glCompileShader(shader);

        final int[] status = new int[1];
        glGetShaderiv(shader, GL_COMPILE_STATUS, status, 0);
        if (status[0] == 0) {
            glDeleteShader(shader);
            return 0;
        }
        return shader;
    }

    private int linkProgram(int vertexShader, int fragmentShader) {
        int program = glCreateProgram();
        if (program == 0) {
            return 0;
        }

        glAttachShader(program, vertexShader);
        glAttachShader(program, fragmentShader);

        glLinkProgram(program);

        final int[] status = new int[1];
        glGetProgramiv(program, GL_LINK_STATUS, status, 0);
        if (status[0] == 0) {
            glDeleteProgram(program);
            return 0;
        }
        return program;

    }

    private void setData2Program(int program) {
        u_ColorLocation = glGetUniformLocation(program, U_COLOR);
        u_MatrixLocation = glGetUniformLocation(program, U_MATRIX);

        a_PositionLocation = glGetAttribLocation(program, A_POSITION);
        glVertexAttribPointer(a_PositionLocation,
                NUM_PER_POINT,
                GL_FLOAT,
                false,
                0,
                vertexData);
        glEnableVertexAttribArray(a_PositionLocation);
    }

    public float[] getProjectionMatrix() {
        return projectionMatrix;
    }


    public void translateX(float x) {
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.setIdentityM(projectionMatrix, 0);
        Matrix.translateM(modelMatrix, 0, x, 0f, 0f);
        Matrix.multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);//相乘存入temp
        System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);//结果写回projectionMatrix
    }

    public void translateY(float y) {
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, 0f, y, 0f);
        Matrix.multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);
        System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);
    }
}
