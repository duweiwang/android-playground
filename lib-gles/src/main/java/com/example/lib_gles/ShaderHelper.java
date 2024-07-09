package com.example.lib_gles;

import android.util.Log;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.GL_VALIDATE_STATUS;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glGetProgramInfoLog;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glGetShaderInfoLog;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;
import static android.opengl.GLES20.glValidateProgram;

public class ShaderHelper {

    public static int compileVertexShader(String shaderSource) {
        return compileShader(GL_VERTEX_SHADER, shaderSource);
    }

    public static int compileFragmentShader(String fragmentSource) {
        return compileShader(GL_FRAGMENT_SHADER, fragmentSource);
    }


    private static int compileShader(int shaderType, String shaderSource) {
        final int shaderId = glCreateShader(shaderType); //create empty shader

        if (shaderId == 0) {
            Log.d("wdw", "create shader fail");
            return 0;
        }

        glShaderSource(shaderId, shaderSource);//set source

        glCompileShader(shaderId);//compile it


        final int[] compileStatus = new int[1];
        glGetShaderiv(shaderId, GL_COMPILE_STATUS, compileStatus, 0);//get status code to array
        if (compileStatus[0] == 0) {
            glDeleteShader(shaderId);
            Log.d("wdw", "compile shader fail" + glGetShaderInfoLog(shaderId));
            return 0;
        }
        return shaderId;

    }

    public static int linkProgram(int vertexShader, int fragmentShader) {
        final int programId = glCreateProgram();
        if (programId == 0) {
            Log.d("wdw", "create program fail");
            return 0;
        }

        glAttachShader(programId, vertexShader);
        glAttachShader(programId, fragmentShader);

        glLinkProgram(programId);

        final int[] linkStatus = new int[1];
        glGetProgramiv(programId, GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] == 0) {
            glDeleteProgram(programId);
            Log.d("wdw", "link program fail " + glGetProgramInfoLog(programId));
            return 0;
        }
        return programId;
    }


    public static boolean validateProgram(int programObjectId){
        glValidateProgram(programObjectId);

        final int[] validateSatus = new int[1];
        glGetProgramiv(programObjectId,GL_VALIDATE_STATUS,validateSatus,0);

        return validateSatus[0] != 0;

    }


    public static int buildProgram(String vertexShaderSource,String fragmentShaderSource){
        int program;

        int vertexShader = compileVertexShader(vertexShaderSource);
        int fragmentShader = compileFragmentShader(fragmentShaderSource);

        program = linkProgram(vertexShader,fragmentShader);

        validateProgram(program);

        return program;

    }

}
