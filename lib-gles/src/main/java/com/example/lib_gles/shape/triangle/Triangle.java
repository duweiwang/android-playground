package com.example.lib_gles.shape.triangle;


import com.example.lib_gles.shape.IShape;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.*;

/**
 * 三角形对象
 */
public class Triangle implements IShape {
    private static final int POINTS_PER_COORDINATE = 3;
    private static final int BYTES_PER_FLOAT = 4;
    private static final String A_POSITION = "a_Position";
    private static final String U_COLOR = "u_Color";
    private int aPositionLocation;
    private int uColorLocation;

    private static final String VERTEX_SHADER = "attribute vec4 a_Position;\n" +
            "\n" +
            "void main(){\n" +
            "    gl_Position = a_Position;\n" +
            "}";
    private static final String FRAGMENT_SHADER = "precision mediump float;\n" +
            "\n" +
            "uniform vec4 u_Color;\n" +
            "\n" +
            "void main(){\n" +
            "    gl_FragColor = u_Color;\n" +
            "}";

    private float[] vertex = new float[]{
//            0f, 0f,
//            9f, 14f,
//            0f, 14f

            0.0f,  1.0f, 0.0f, // 顶点
            -1.0f, -0.0f, 0.0f, // 左下角
            1.0f, -0.0f, 0.0f  // 右下角

//            -0.5f, -0.5f,
//            0.5f,0.5f,
//            -0.5f, 0.5f

    };
    private FloatBuffer floatBuffer;

    public Triangle() {
        floatBuffer = ByteBuffer.allocateDirect(vertex.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        floatBuffer.put(vertex);

        int vertexShader = compileVertexShader();
        int fragmentShader = compileFragmentShader();
        int program = linkShaderToProgram(vertexShader, fragmentShader);

        glUseProgram(program);

        attachData(program);
    }

    private int compileVertexShader() {
        int shaderId = glCreateShader(GL_VERTEX_SHADER);
        if (shaderId == 0) {
            return 0;
        }

        glShaderSource(shaderId, VERTEX_SHADER);
        glCompileShader(shaderId);

        final int[] status = new int[1];
        glGetShaderiv(shaderId, GL_COMPILE_STATUS, status, 0);

        if (status[0] == 0) {
            glDeleteShader(shaderId);
            return 0;
        }
        return shaderId;
    }


    private int compileFragmentShader() {
        int fragmentShaderId = glCreateShader(GL_FRAGMENT_SHADER);
        if (fragmentShaderId == 0) {
            return 0;
        }

        glShaderSource(fragmentShaderId, FRAGMENT_SHADER);
        glCompileShader(fragmentShaderId);

        final int[] status = new int[1];
        glGetShaderiv(fragmentShaderId, GL_COMPILE_STATUS, status, 0);

        if (status[0] == 0) {
            glDeleteShader(fragmentShaderId);
            return 0;
        }
        return fragmentShaderId;

    }


    private int linkShaderToProgram(int vertexShader, int fragmentShader) {
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

    private void attachData(int program) {
        uColorLocation = glGetUniformLocation(program, U_COLOR);

        aPositionLocation = glGetAttribLocation(program, A_POSITION);
        floatBuffer.position(0);
        glEnableVertexAttribArray(aPositionLocation);
        glVertexAttribPointer(aPositionLocation,
                POINTS_PER_COORDINATE,
                GL_FLOAT,
                false,
                POINTS_PER_COORDINATE * BYTES_PER_FLOAT,
                floatBuffer);

    }


    @Override
    public void draw() {
        glUniform4f(uColorLocation, 0f, 0f, 1f, 1f);
        glDrawArrays(GL_TRIANGLES, 0, 3);
    }
}
