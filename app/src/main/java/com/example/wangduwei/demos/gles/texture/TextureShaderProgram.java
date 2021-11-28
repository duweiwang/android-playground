package com.example.wangduwei.demos.gles.texture;

import android.content.Context;
import android.opengl.GLES20;

import com.example.wangduwei.demos.R;

/**
 * @desc: 纹理着色器
 * @auther:duwei
 * @date:2018/10/18
 */

public class TextureShaderProgram extends ShaderProgram {
    private final int uMatrixLocation;
    private final int uTextureUnitLocation;

    private final int aPositionLocation;
    private final int aTextureCoordinatesLocation;


    public TextureShaderProgram(Context context) {
        super(context, R.raw.texture_vertex_shader, R.raw.texture_fragment_shader);

        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX);
        uTextureUnitLocation = GLES20.glGetUniformLocation(program, U_TEXTURE_UNIT);

        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
        aTextureCoordinatesLocation = GLES20.glGetAttribLocation(program, A_TEXTURE_COORDINATES);
    }


    public void setUniforms(float[] matrix, int textureId) {
        //传递矩阵给shader program
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
        //活动的纹理单元设置为0号单元
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        //纹理绑定到这个单元
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
        //把纹理单元传递给片段着色器
        GLES20.glUniform1i(uTextureUnitLocation, 0);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

    public int getTextureCoordinatesAttributeLocation() {
        return aTextureCoordinatesLocation;
    }

}
