package com.example.lib_gles.texture;

import android.content.Context;
import android.opengl.GLES20;

import com.example.lib_gles.ShaderHelper;
import com.example.lib_gles.TextResourceReader;


/**
 * @desc:
 * @auther:duwei
 * @date:2018/10/18
 */

public class ShaderProgram {

    //uniform
    protected static final String U_MATRIX = "u_Matrix";
    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";

    //Attribute
    protected static final String A_POSITION = "a_Position";
    protected static final String A_COLOR = "a_Color";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";


    protected final int program;

    protected ShaderProgram(Context context, int vertexShaderResId, int fragmentShaderResId) {
        program = ShaderHelper.buildProgram(
                TextResourceReader.readTextFileFromResource(context,vertexShaderResId),
                TextResourceReader.readTextFileFromResource(context,fragmentShaderResId));
    }

    public void useProgram() {
        GLES20.glUseProgram(program);
    }


}
